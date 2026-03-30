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
    color = str(color_val).strip() if color_val else "FFFFFF"
    if not color.startswith("#"):
        color = "#" + color
    return color


@router.get("/drivers/{year}/{round_num}/{session_type}")
def get_session_drivers(year: int, round_num: int, session_type: str):
    try:
        session = fastf1.get_session(year, round_num, session_type)
        session.load(laps=False, telemetry=False, weather=False, messages=False)

        drivers = []

        results_ok = (session.results is not None and not session.results.empty
                      and "Abbreviation" in session.results.columns)

        if results_ok:
            for _, row in session.results.iterrows():
                drivers.append({
                    "abbreviation": str(row.get("Abbreviation", "")),
                    "full_name": f"{row.get('FirstName', '')} {row.get('LastName', '')}".strip(),
                    "team": str(row.get("TeamName", "")),
                    "team_color": get_team_color(row.get("TeamColor")),
                })
        else:
            # Fallback for FP/old sessions without results: use driver list from session
            for drv_num in session.drivers:
                try:
                    info = session.get_driver(drv_num)
                    drivers.append({
                        "abbreviation": str(info.get("Abbreviation", drv_num)),
                        "full_name": f"{info.get('FirstName', '')} {info.get('LastName', '')}".strip(),
                        "team": str(info.get("TeamName", "")),
                        "team_color": get_team_color(info.get("TeamColor")),
                    })
                except Exception:
                    drivers.append({
                        "abbreviation": drv_num,
                        "full_name": drv_num,
                        "team": "",
                        "team_color": "#FFFFFF",
                    })

        return {"year": year, "round": round_num, "session_type": session_type, "drivers": drivers}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/telemetry/{year}/{round_num}/{session_type}/{driver}")
def get_telemetry(year: int, round_num: int, session_type: str, driver: str, driver2: str = None):
    try:
        session = fastf1.get_session(year, round_num, session_type)
        session.load(laps=True, telemetry=True, weather=False, messages=False)

        def extract_driver_telemetry(drv):
            laps = session.laps[session.laps["Driver"] == drv]
            if laps.empty:
                raise HTTPException(status_code=404, detail=f"Driver {drv} not found")

            fastest = laps.pick_fastest()
            if fastest is None or (hasattr(fastest, "empty") and fastest.empty):
                raise HTTPException(status_code=404, detail=f"No fastest lap for {drv}")

            tel = fastest.get_telemetry()
            if tel is None or tel.empty:
                raise HTTPException(status_code=404, detail=f"No telemetry for {drv}")

            # Sample to max 600 points for manageable JSON size
            step = max(1, len(tel) // 600)
            tel = tel.iloc[::step]

            lap_time = fastest["LapTime"]

            # Get team color — results may be empty for FP sessions
            team_color = "#FF0000"
            try:
                if (session.results is not None and not session.results.empty
                        and "Abbreviation" in session.results.columns):
                    driver_row = session.results[session.results["Abbreviation"] == drv]
                    if not driver_row.empty:
                        team_color = get_team_color(driver_row.iloc[0]["TeamColor"])
                if team_color == "#FF0000" and "TeamColor" in laps.columns:
                    team_color = get_team_color(laps.iloc[0].get("TeamColor", "#FF0000"))
            except Exception:
                pass

            return {
                "driver": drv,
                "lap_number": int(fastest["LapNumber"]),
                "lap_time": format_timedelta(lap_time),
                "compound": str(fastest.get("Compound", "N/A")),
                "team_color": team_color,
                "telemetry": {
                    "distance": tel["Distance"].round(1).tolist(),
                    "speed": tel["Speed"].round(1).tolist(),
                    "throttle": tel["Throttle"].round(1).tolist(),
                    "brake": tel["Brake"].astype(int).tolist(),
                    "gear": tel["nGear"].tolist(),
                    "rpm": tel["RPM"].round(0).astype(int).tolist(),
                },
            }

        driver1_data = extract_driver_telemetry(driver)
        result = {"driver1": driver1_data}

        if driver2 and driver2.strip():
            driver2_data = extract_driver_telemetry(driver2.strip())
            result["driver2"] = driver2_data

        return result

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
