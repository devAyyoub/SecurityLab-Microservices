#!/bin/bash

# Script to rebuild and restart a specific service
# Usage: ./scripts/rebuild-service.sh <service-name>
# Example: ./scripts/rebuild-service.sh student-ms

set -e

SERVICE_NAME=$1

if [ -z "$SERVICE_NAME" ]; then
    echo "Error: You must specify the service name"
    echo "Usage: ./scripts/rebuild-service.sh <service-name>"
    echo ""
    echo "Available services:"
    echo "  - student-ms"
    echo "  - reservation-ms"
    echo "  - university-housing-ms"
    echo "  - gateway"
    echo "  - eureka-server"
    exit 1
fi

echo "Stopping service: $SERVICE_NAME"
docker-compose stop "$SERVICE_NAME" || true

echo "Rebuilding image: $SERVICE_NAME"
docker-compose build "$SERVICE_NAME"

echo "Starting service: $SERVICE_NAME"
docker-compose up -d "$SERVICE_NAME"

echo "Service $SERVICE_NAME rebuilt and restarted"
echo ""
echo "View logs with: docker-compose logs -f $SERVICE_NAME"
