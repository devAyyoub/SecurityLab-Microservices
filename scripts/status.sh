#!/bin/bash

# Script to view the status of all services
# Usage: ./scripts/status.sh

echo "Service status:"
echo ""
docker-compose ps

echo ""
echo "To view logs of a service: ./scripts/logs.sh <service-name>"
echo "To rebuild a service: ./scripts/rebuild-service.sh <service-name>"
