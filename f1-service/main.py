import os
import fastf1
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from routes.schedule import router as schedule_router
from routes.results import router as results_router
from routes.telemetry import router as telemetry_router
from routes.fastest_laps import router as fastest_laps_router
from routes.track_map import router as track_map_router

cache_dir = os.path.join(os.path.dirname(__file__), "cache")
os.makedirs(cache_dir, exist_ok=True)
fastf1.Cache.enable_cache(cache_dir)

app = FastAPI(title="F1 Data Service", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(schedule_router, prefix="/api")
app.include_router(results_router, prefix="/api")
app.include_router(telemetry_router, prefix="/api")
app.include_router(fastest_laps_router, prefix="/api")
app.include_router(track_map_router, prefix="/api")


@app.get("/health")
def health():
    return {"status": "ok", "service": "F1 Data Service"}
