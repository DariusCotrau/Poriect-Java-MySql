import fastf1
import pandas as pd
from fastapi import APIRouter, HTTPException

router = APIRouter()


def format_timedelta(td):
    if td is None or pd.isna(td):
        return None
    total = td.total_seconds()
    mins = int(total // 60)
    secs = total % 60
    if mins > 0:
        return f"{mins}:{secs:06.3f}"
    return f"{secs:.3f}"


@router.get("/results/{year}/{round_num}/{session_type}")
def get_results(year: int, round_num: int, session_type: str):
    try:
        session = fastf1.get_session(year, round_num, session_type)
        session.load(laps=False, telemetry=False, weather=False, messages=False)

        results_list = []
        for _, row in session.results.iterrows():
            time_str = ""
            if session_type == "R":
                t = row.get("Time")
                if t is not None and not pd.isna(t):
                    time_str = format_timedelta(t) or ""
                else:
                    time_str = str(row.get("Status", "DNF"))
            elif session_type in ("Q", "SQ"):
                for q_col in ("Q3", "Q2", "Q1"):
                    val = row.get(q_col)
                    if val is not None and not pd.isna(val):
                        time_str = format_timedelta(val) or ""
                        break
            elif session_type in ("FP1", "FP2", "FP3", "S"):
                t = row.get("Time")
                if t is not None and not pd.isna(t):
                    time_str = format_timedelta(t) or ""

            pos = row.get("Position")
            try:
                pos_int = int(pos) if pos is not None and not pd.isna(pos) else 0
            except (ValueError, TypeError):
                pos_int = 0

            pts = row.get("Points")
            try:
                pts_float = float(pts) if pts is not None and not pd.isna(pts) else 0.0
            except (ValueError, TypeError):
                pts_float = 0.0

            color = str(row.get("TeamColor", "FF0000")).strip()
            if not color.startswith("#"):
                color = "#" + color

            results_list.append({
                "position": pos_int,
                "driver_number": str(row.get("DriverNumber", "")),
                "abbreviation": str(row.get("Abbreviation", "")),
                "full_name": f"{row.get('FirstName', '')} {row.get('LastName', '')}".strip(),
                "team": str(row.get("TeamName", "")),
                "team_color": color,
                "time": time_str,
                "points": pts_float,
                "status": str(row.get("Status", "")),
            })

        results_list.sort(key=lambda x: x["position"] if x["position"] > 0 else 999)

        return {
            "year": year,
            "round": round_num,
            "session_type": session_type,
            "session_name": session.name,
            "event_name": session.event["EventName"],
            "results": results_list,
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
