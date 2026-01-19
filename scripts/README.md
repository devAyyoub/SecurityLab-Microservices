# Build and Deployment Scripts

This folder contains useful scripts to rebuild and restart services when code changes are made.

## Available Scripts

### Rebuild Scripts

#### `rebuild-service.sh`
Generic script to rebuild any service.

**Usage:**
```bash
./scripts/rebuild-service.sh <service-name>
```

**Example:**
```bash
./scripts/rebuild-service.sh student-ms
```

#### `rebuild-all.sh`
Rebuilds and restarts all services.

**Usage:**
```bash
./scripts/rebuild-all.sh
```

#### Service-Specific Scripts

- `rebuild-student-ms.sh` - Rebuilds StudentMS
- `rebuild-reservation-ms.sh` - Rebuilds ReservationMS
- `rebuild-gateway.sh` - Rebuilds Gateway
- `rebuild-university-housing-ms.sh` - Rebuilds UniversityHousingMS

**Usage:**
```bash
./scripts/rebuild-student-ms.sh
```

### Utility Scripts

#### `logs.sh`
View logs of a service.

**Usage:**
```bash
# View last 100 lines
./scripts/logs.sh student-ms

# Follow logs in real-time
./scripts/logs.sh student-ms --follow
```

#### `status.sh`
View the status of all services.

**Usage:**
```bash
./scripts/status.sh
```

## Typical Workflow

1. **Make code changes** in a microservice (e.g., StudentMS)

2. **Rebuild the service:**
   ```bash
   ./scripts/rebuild-student-ms.sh
   ```

3. **Check logs:**
   ```bash
   ./scripts/logs.sh student-ms --follow
   ```

4. **Check status:**
   ```bash
   ./scripts/status.sh
   ```

## Available Services

- `student-ms` - Student Microservice
- `reservation-ms` - Reservation Microservice
- `university-housing-ms` - University Housing Microservice
- `gateway` - API Gateway
- `eureka-server` - Service Discovery
- `keycloak` - Authentication Server

## Notes

- Scripts stop the service before rebuilding
- Images are rebuilt from scratch (no cache)
- Services are automatically started after build
- Keycloak and Eureka typically don't need rebuild (they are base images)
