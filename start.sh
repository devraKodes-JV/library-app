#!/bin/bash
cd "$(dirname "$0")"
pkill -f "bootstrap-1.0.0.jar" 2>/dev/null || true
sleep 1
rm -f app.pid
rm -rf data
nohup java -jar bootstrap/target/bootstrap-1.0.0.jar > app.log 2>&1 &
echo $! > app.pid
echo "Started PID: $(cat app.pid)"
sleep 8
if ps -p $(cat app.pid) > /dev/null 2>&1; then
    echo "App is RUNNING"
    tail -5 app.log
else
    echo "App STOPPED"
    tail -20 app.log
fi
