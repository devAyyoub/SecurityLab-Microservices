#!/bin/bash

# Script to rebuild and restart all services
# Usage: ./scripts/rebuild-all.sh

set -e

echo "Stopping all services..."
docker-compose stop

echo "Rebuilding all images..."
docker-compose build

echo "Starting all services..."
docker-compose up -d

echo "All services have been rebuilt and restarted"
echo ""
echo "View status with: docker-compose ps"
echo "View logs with: docker-compose logs -f"
