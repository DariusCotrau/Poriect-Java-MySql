import fastf1
import pandas as pd
from fastapi import APIRouter, HTTPException

router = APIRouter()


@router.get("/schedule/{year}")
def get_schedule(year: int):
    try:
        schedule = fastf1.get_event_schedule(year, include_testing=False)
        rounds = []
        for _, event in schedule.iterrows():
            date_val = event.get("EventDate")
            if date_val is not None and not pd.isna(date_val):
                date_str = str(date_val)[:10]
            else:
                date_str = "TBD"

            fmt = str(event.get("EventFormat", "conventional"))
            is_sprint = "sprint" in fmt.lower()

            rounds.append({
                "round": int(event["RoundNumber"]),
                "name": str(event["EventName"]),
                "country": str(event.get("Country", "")),
                "location": str(event.get("Location", "")),
                "date": date_str,
                "format": fmt,
                "is_sprint": is_sprint,
            })

        return {"year": year, "rounds": rounds}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
