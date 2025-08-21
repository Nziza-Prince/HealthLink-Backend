# HealthLink

A Spring Boot application with PostgreSQL database running on Docker.

## Prerequisites

- Docker and Docker Compose installed
- Java 24 (as specified in pom.xml)
- Maven

## Quick Start

### Option 1: Using Spring Boot with Docker Compose (Recommended)

The application is configured to automatically start the PostgreSQL database using Docker Compose when you run the Spring Boot application.

```bash
# Start the application (this will automatically start PostgreSQL)
./mvnw spring-boot:run
```

Your app will be available at: http://localhost:8081

### Option 2: Manual Docker Compose

If you prefer to manage the database separately:

```bash
# Start only the PostgreSQL database
docker-compose up postgres -d

# Start the Spring Boot application
./mvnw spring-boot:run
```

### Option 3: Start Everything with Docker Compose First

```bash
# Start PostgreSQL database
docker-compose up postgres -d

# Wait a few seconds for the database to be ready, then start the app
./mvnw spring-boot:run
```

## Database Configuration

- **Host**: localhost
- **Port**: 5432
- **Database**: healthlink
- **Username**: healthlink_user
- **Password**: healthlink123

## Docker Services

- **PostgreSQL 15**: Running on port 5432 with persistent data storage
- **Health checks**: Configured to ensure database is ready before accepting connections

## Useful Commands

```bash
# View running containers
docker ps

# View logs
docker-compose logs postgres

# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: This will delete all data)
docker-compose down -v

# Restart PostgreSQL
docker-compose restart postgres
```

## Development

The application is configured with:
- **Flyway migrations** for database schema management
- JPA/Hibernate with schema validation (Flyway handles schema changes)
- Connection pooling with HikariCP
- SQL logging enabled for development
- UUID extension for PostgreSQL

### Flyway Migrations

Database schema changes are managed through Flyway migrations located in `src/main/resources/db/migration/`:

- **V1__Create_initial_schema.sql**: Creates the initial database schema with users, profiles, health_records, and appointments tables
- All migrations run automatically when the application starts
- Migrations are version-controlled and run in order
- Never modify existing migration files - create new ones for changes

### Adding New Migrations

To add a new database migration:

1. Create a new file in `src/main/resources/db/migration/`
2. Use the naming convention: `V{version}__{description}.sql`
3. Example: `V2__Add_user_roles.sql`
4. Restart the application to run the migration

## Troubleshooting

If you encounter connection issues:
1. Ensure Docker is running
2. Check if PostgreSQL container is healthy: `docker-compose ps`
3. Verify the database is accessible: `docker-compose logs postgres`
4. Check if port 5432 is available on your system 