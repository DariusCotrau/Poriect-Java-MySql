import fastf1
import pandas as pd
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
            norm(row["X"], row["Y"])
            for _, row in track_pos.iloc[::step].iterrows()
        ]

        # Get driver positions at the specified lap
        laps_at_lap = session.laps[session.laps["LapNumber"] == lap]

        drivers_data = []
        for _, drv_lap in laps_at_lap.iterrows():
            try:
                pos_data = drv_lap.get_pos_data().dropna(subset=["X", "Y"])
                if pos_data.empty:
                    continue

                # Take midpoint of the lap as snapshot position
                mid = pos_data.iloc[len(pos_data) // 2]
                nx, ny = norm(mid["X"], mid["Y"])

                drv_num = str(drv_lap.get("DriverNumber", ""))
                try:
                    info = session.get_driver(drv_num)
                    abbreviation = str(info.get("Abbreviation", drv_num))
                    team_color = str(info.get("TeamColor", "FFFFFF")).strip()
                    if not team_color.startswith("#"):
                        team_color = "#" + team_color
                except Exception:
                    abbreviation = drv_num
                    team_color = "#FFFFFF"

                compound = str(drv_lap.get("Compound", "?"))
                position = drv_lap.get("Position", 0)
                try:
                    position = int(position) if pd.notna(position) else 0
                except Exception:
                    position = 0

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

        drivers_data.sort(key=lambda d: d["position"] if d["position"] > 0 else 999)

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
