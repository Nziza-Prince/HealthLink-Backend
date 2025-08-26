# HealthLink Backend

A Spring Boot application for managing healthcare appointments, inventory, and financial records.

## Prerequisites

- Java 24 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for database)

## Quick Start

### 1. Start the Database

```bash
docker-compose up -d
```

This will start a PostgreSQL database with the following credentials:
- Database: healthlink
- Username: healthlink_user
- Password: healthlink123
- Port: 5432

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start on port 8081.

### 3. Test the Application

Visit: http://localhost:8081/api/test/health

You should see: "HealthLink Backend is running successfully!"

## API Endpoints

- `GET /api/test/health` - Health check endpoint
- Additional endpoints will be available as controllers are implemented

# View logs
docker-compose logs postgres

The application uses JPA entities with the following main tables:
- users - Base user information
- patients - Patient-specific data
- doctors - Doctor information
- appointments - Patient appointments
- medical_inventory - Medical supplies and medications
- payments - Payment records
- transactions - Financial transactions
- wallets - Patient wallet balances
- notifications - System notifications

## Configuration

The application is configured to:
- Use PostgreSQL as the database
- Auto-create database schema on startup
- Run on port 8081
- Use UUIDs for entity IDs
- Include JPA auditing for created/updated timestamps

## Development

- The application uses Spring Boot 3.5.5
- JPA/Hibernate for data persistence
- Spring Security for basic security configuration
- Lombok for reducing boilerplate code
- Flyway migrations (currently disabled for development)

## Troubleshooting

1. **Database Connection Issues**: Ensure PostgreSQL is running and accessible on port 5432
2. **Port Conflicts**: Change the server port in `application.properties` if 8081 is already in use
3. **Java Version**: Ensure you're using Java 24 or higher
4. **Maven Issues**: Try `mvn clean install` before running the application 