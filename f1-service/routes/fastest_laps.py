import fastf1
import pandas as pd
from fastapi import APIRouter, HTTPException

router = APIRouter()


def format_timedelta(td):
    if td is None or pd.isna(td):
        return "N/A"
    total = td.total_seconds()
    mins = int(total // 60)
    secs = total % 60
    return f"{mins}:{secs:06.3f}"


def get_team_color(color_val):
    color = str(color_val).strip() if color_val else "FF0000"
    if not color.startswith("#"):
        color = "#" + color
    return color


@router.get("/fastest-laps/{year}/{round_num}")
def get_fastest_laps(year: int, round_num: int):
    try:
        session = fastf1.get_session(year, round_num, "R")
        session.load(laps=True, telemetry=False, weather=False, messages=False)

        laps = session.laps.pick_quicklaps()
        if laps.empty:
            laps = session.laps

        # Get fastest clean lap per driver
        fastest_laps = (
            laps.dropna(subset=["LapTime"])
            .groupby("Driver", as_index=False)
            .apply(lambda x: x.nsmallest(1, "LapTime"))
            .reset_index(drop=True)
            .sort_values("LapTime")
        )

        results = []
        for i, (_, lap) in enumerate(fastest_laps.iterrows()):
            drv = lap["Driver"]
            driver_row = session.results[session.results["Abbreviation"] == drv]

            if driver_row.empty:
                full_name = drv
                team = ""
                color = "#FF0000"
            else:
                r = driver_row.iloc[0]
                full_name = f"{r.get('FirstName', '')} {r.get('LastName', '')}".strip()
                team = str(r.get("TeamName", ""))
                color = get_team_color(r.get("TeamColor"))

            results.append({
                "position": i + 1,
                "driver": drv,
                "full_name": full_name,
                "team": team,
                "team_color": color,
                "lap_number": int(lap["LapNumber"]),
                "lap_time": format_timedelta(lap["LapTime"]),
                "lap_time_seconds": round(lap["LapTime"].total_seconds(), 3),
                "compound": str(lap.get("Compound", "N/A")),
            })

        return {
            "year": year,
            "round": round_num,
            "event_name": session.event["EventName"],
            "fastest_laps": results,
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
