import fastf1
import pandas as pd
import numpy as np
from fastapi import APIRouter, HTTPException

router = APIRouter()

CANVAS = 800
PAD = 50


def make_normalizer(x_vals, y_vals):
    x_min, x_max = x_vals.min(), x_vals.max()
    y_min, y_max = y_vals.min(), y_vals.max()
    x_range = x_max - x_min or 1
    y_range = y_max - y_min or 1
    scale = max(x_range, y_range)
    draw = CANVAS - 2 * PAD

    def norm(x, y):
        nx = round((x - x_min) / scale * draw + PAD, 1)
        # Flip Y: FastF1 Y increases upward, Canvas Y increases downward
        ny = round(draw - (y - y_min) / scale * draw + PAD, 1)
        return nx, ny

    return norm


@router.get("/track-map/{year}/{round_num}")
def get_track_map(year: int, round_num: int, lap: int = 1):
    try:
        session = fastf1.get_session(year, round_num, "R")
        session.load(laps=True, telemetry=True, weather=False, messages=False)

        total_laps = int(session.laps["LapNumber"].max())
        lap = max(1, min(lap, total_laps))

        # Build track outline from the overall fastest lap
        ref_lap = session.laps.pick_fastest()
        track_pos = ref_lap.get_pos_data().dropna(subset=["X", "Y"])
        norm = make_normalizer(track_pos["X"], track_pos["Y"])

        step = max(1, len(track_pos) // 400)
        track_outline = [
            norm(r["X"], r["Y"])
            for _, r in track_pos.iloc[::step].iterrows()
        ]

        # Find snapshot time: when the race leader crosses the line for lap N
        laps_at_lap = session.laps[session.laps["LapNumber"] == lap]
        if laps_at_lap.empty:
            raise ValueError(f"No data for lap {lap}")

        p1_laps = laps_at_lap[laps_at_lap["Position"] == 1]
        ref_row = p1_laps.iloc[0] if not p1_laps.empty else laps_at_lap.iloc[0]
        ref_session_time = ref_row["LapStartTime"]
        ref_date = session.t0_date + ref_session_time

        drivers_data = []

        for _, drv_lap in laps_at_lap.iterrows():
            drv_num = str(drv_lap.get("DriverNumber", ""))
            if not drv_num or drv_num not in session.pos_data:
                continue

            try:
                pos_df = session.pos_data[drv_num]
                # All drivers shown at the SAME moment in session time
                time_diff = (pos_df["Date"] - ref_date).abs()
                idx = time_diff.idxmin()
                pos_row = pos_df.loc[idx]

                if pd.isna(pos_row["X"]) or pd.isna(pos_row["Y"]):
                    continue

                nx, ny = norm(pos_row["X"], pos_row["Y"])

                # Driver info
                try:
                    info = session.get_driver(drv_num)
                    abbreviation = str(info.get("Abbreviation", drv_num))
                    team_color = str(info.get("TeamColor", "FFFFFF")).strip()
                    if not team_color.startswith("#"):
                        team_color = "#" + team_color
                except Exception:
                    abbreviation = drv_num
                    team_color = "#FFFFFF"

                # Position from lap data
                position = drv_lap.get("Position", None)
                try:
                    position = int(position) if position is not None and pd.notna(position) else 0
                except Exception:
                    position = 0

                compound = str(drv_lap.get("Compound", "?"))

                drivers_data.append({
                    "driver_number": drv_num,
                    "abbreviation": abbreviation,
                    "team_color": team_color,
                    "x": nx,
                    "y": ny,
                    "compound": compound,
                    "position": position,
                })
            except Exception:
                continue

        # Sort by position; fallback to results order if positions are missing
        drivers_data.sort(key=lambda d: d["position"] if d["position"] > 0 else 999)

        # If all positions are 0, use results order
        if all(d["position"] == 0 for d in drivers_data) and not session.results.empty:
            results_order = {}
            if "Abbreviation" in session.results.columns and "Position" in session.results.columns:
                for _, r in session.results.iterrows():
                    try:
                        pos = int(r["Position"]) if pd.notna(r["Position"]) else 99
                    except Exception:
                        pos = 99
                    results_order[str(r.get("Abbreviation", ""))] = pos
            for d in drivers_data:
                d["position"] = results_order.get(d["abbreviation"], 99)
            drivers_data.sort(key=lambda d: d["position"])

        return {
            "event_name": session.event["EventName"],
            "year": year,
            "round": round_num,
            "lap": lap,
            "total_laps": total_laps,
            "track_outline": track_outline,
            "drivers": drivers_data,
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/track-animation/{year}/{round_num}")
def get_track_animation(year: int, round_num: int,
                         lap_start: int = 1, lap_end: int = 10,
                         hz: float = 2.0):
    try:
        session = fastf1.get_session(year, round_num, "R")
        session.load(laps=True, telemetry=True, weather=False, messages=False)

        total_laps = int(session.laps["LapNumber"].max())
        lap_start = max(1, min(lap_start, total_laps))
        lap_end = max(lap_start, min(lap_end, total_laps))

        # Build track outline + normalization bounds from fastest lap
        ref_lap = session.laps.pick_fastest()
        track_pos = ref_lap.get_pos_data().dropna(subset=["X", "Y"])
        norm_fn = make_normalizer(track_pos["X"], track_pos["Y"])

        step = max(1, len(track_pos) // 400)
        track_outline = [
            norm_fn(r["X"], r["Y"])
            for _, r in track_pos.iloc[::step].iterrows()
        ]

        # Precompute normalization constants for bulk numpy conversion
        x_min = float(track_pos["X"].min())
        x_max = float(track_pos["X"].max())
        y_min = float(track_pos["Y"].min())
        y_max = float(track_pos["Y"].max())
        scale = max(x_max - x_min, y_max - y_min) or 1
        draw = CANVAS - 2 * PAD

        t0 = session.t0_date

        # Time boundaries: when do laps lap_start..lap_end happen?
        laps_in_range = session.laps[
            (session.laps["LapNumber"] >= lap_start) &
            (session.laps["LapNumber"] <= lap_end)
        ]
        if laps_in_range.empty:
            raise ValueError("No lap data in range")

        t_start_s = (laps_in_range["LapStartTime"].min()).total_seconds()
        # end = last lap start + its lap time (best estimate)
        last_start = laps_in_range["LapStartTime"].max().total_seconds()
        max_lap_time = laps_in_range["LapTime"].dropna().max()
        t_end_s = last_start + (max_lap_time.total_seconds() if pd.notna(max_lap_time) else 120)

        dt = 1.0 / hz
        time_grid = np.arange(t_start_s, t_end_s + dt, dt)
        n_frames = len(time_grid)

        # Build per-driver position arrays interpolated onto time_grid
        driver_info = {}
        frames = {}

        for drv_num in session.drivers:
            if drv_num not in session.pos_data:
                continue

            pos_df = session.pos_data[drv_num][["Date", "X", "Y"]].copy()
            pos_df = pos_df.dropna(subset=["X", "Y"])
            if pos_df.empty:
                continue

            pos_df["t"] = (pos_df["Date"] - t0).dt.total_seconds()
            pos_df = pos_df.sort_values("t")

            # Only use data within the time range (with margin)
            mask = (pos_df["t"] >= t_start_s - 30) & (pos_df["t"] <= t_end_s + 30)
            pos_df = pos_df[mask]
            if len(pos_df) < 2:
                continue

            t_arr = pos_df["t"].values
            x_raw = np.interp(time_grid, t_arr, pos_df["X"].values)
            y_raw = np.interp(time_grid, t_arr, pos_df["Y"].values)

            # Normalize to canvas coords (flip Y)
            x_norm = ((x_raw - x_min) / scale * draw + PAD).round(1)
            y_norm = (draw - (y_raw - y_min) / scale * draw + PAD).round(1)

            frames[drv_num] = {
                "x": x_norm.tolist(),
                "y": y_norm.tolist(),
            }

            try:
                info = session.get_driver(drv_num)
                abbreviation = str(info.get("Abbreviation", drv_num))
                team_color = str(info.get("TeamColor", "FFFFFF")).strip()
                if not team_color.startswith("#"):
                    team_color = "#" + team_color
            except Exception:
                abbreviation = drv_num
                team_color = "#FFFFFF"

            driver_info[drv_num] = {
                "abbreviation": abbreviation,
                "team_color": team_color,
            }

        # Build lap-to-frame index (for lap counter display)
        lap_frames = {}
        for lap_n in range(lap_start, lap_end + 1):
            lap_rows = session.laps[session.laps["LapNumber"] == lap_n]
            if not lap_rows.empty:
                t_lap = lap_rows["LapStartTime"].min().total_seconds()
                frame_idx = int(np.searchsorted(time_grid, t_lap))
                lap_frames[str(lap_n)] = frame_idx

        return {
            "event_name": session.event["EventName"],
            "year": year,
            "round": round_num,
            "lap_start": lap_start,
            "lap_end": lap_end,
            "total_laps": total_laps,
            "hz": hz,
            "n_frames": n_frames,
            "track_outline": track_outline,
            "drivers": driver_info,
            "frames": frames,
            "lap_frames": lap_frames,
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
