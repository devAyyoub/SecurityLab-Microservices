#!/bin/bash

# Script to view logs of a service
# Usage: ./scripts/logs.sh <service-name> [--follow]
# Example: ./scripts/logs.sh student-ms --follow

SERVICE_NAME=$1
FOLLOW_FLAG=$2

if [ -z "$SERVICE_NAME" ]; then
    echo "Error: You must specify the service name"
    echo "Usage: ./scripts/logs.sh <service-name> [--follow]"
    exit 1
fi

if [ "$FOLLOW_FLAG" == "--follow" ] || [ "$FOLLOW_FLAG" == "-f" ]; then
    docker-compose logs -f "$SERVICE_NAME"
else
    docker-compose logs --tail=100 "$SERVICE_NAME"
fi
