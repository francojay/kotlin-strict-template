#!/bin/bash

# Kill any process using port 8080
pid=$(lsof -ti:8080)
if [ ! -z "$pid" ]; then
    echo "Killing process $pid using port 8080"
    kill -9 $pid
fi

# Run the application
./gradlew bootRun 