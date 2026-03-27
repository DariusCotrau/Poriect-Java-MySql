@echo off
echo === F1 Data Service ===
echo Instalare dependinte...
pip install -r requirements.txt
echo.
echo Pornire server pe http://localhost:8000
echo Apasa Ctrl+C pentru a opri.
echo.
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
