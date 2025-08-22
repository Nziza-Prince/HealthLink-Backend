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


## 📱 Complete API Endpoints

### **Authentication Endpoints:**
- `POST /api/auth/patient/signup` - Patient registration
- `POST /api/auth/login` - User login (returns JWT token)
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - User logout

### **Development/Testing Endpoints (with patientId parameter):**
- `GET /api/patient/overview?patientId={id}` - Patient dashboard
- `GET /api/patient/appointments?patientId={id}` - Patient appointments
- `POST /api/patient/appointments` - Create appointment (JSON body with patientId)
- `GET /api/patient/prescriptions?patientId={id}` - Patient prescriptions
- `GET /api/patient/wallet?patientId={id}` - Patient wallet
- `POST /api/patient/wallet/topup` - Top up wallet (JSON body with patientId)
- `GET /api/patient/payments?patientId={id}` - Patient payments
- `GET /api/patient/transactions?patientId={id}` - Patient transactions
- `GET /api/patient/profile?patientId={id}` - Patient profile
- `GET /api/patient/departments` - Get all departments for dropdown

### **Authenticated Endpoints (with JWT token - /me endpoints):**
- `GET /api/patient/overview/me` - Current user's dashboard
- `GET /api/patient/appointments/me` - Current user's appointments
- `POST /api/patient/appointments/me` - Create appointment for current user
- `GET /api/patient/prescriptions/me` - Current user's prescriptions
- `GET /api/patient/wallet/me` - Current user's wallet
- `POST /api/patient/wallet/topup/me` - Top up current user's wallet
- `GET /api/patient/payments/me` - Current user's payments
- `GET /api/patient/transactions/me` - Current user's transactions
- `GET /api/patient/profile/me` - Current user's profile

### **Test Endpoint:**
- `GET /api/patient/test` - Test API connectivity

## 🔐 **JWT Authentication Usage:**

### **1. Login to get JWT token:**
```bash
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

### **2. Use JWT token in authenticated requests:**
```bash
GET /api/patient/overview/me
Authorization: Bearer YOUR_JWT_TOKEN
```

### **3. For development/testing (without JWT):**
```bash
GET /api/patient/overview?patientId=uuid-here
```

## 🧪 **Test Data for JSON-based POST APIs**

### **Create Appointment (with patientId):**
```bash
POST /api/patient/appointments
Content-Type: application/json

{
  "patientId": "550e8400-e29b-41d4-a716-446655440000",
  "departmentName": "Cardiology",
  "reason": "Chest pain and difficulty breathing",
  "preferredDate": "2024-08-25"
}
```

### **Create Appointment (authenticated - /me):**
```bash
POST /api/patient/appointments/me
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "departmentName": "Cardiology",
  "reason": "Severe stomach pain",
  "preferredDate": "2024-08-25"
}
```

### **Top Up Wallet (with patientId):**
```bash
POST /api/patient/wallet/topup
Content-Type: application/json

{
  "patientId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": "50000",
  "paymentMethod": "MTN_MOBILE_MONEY"
}
```

### **Top Up Wallet (authenticated - /me):**
```bash
POST /api/patient/wallet/topup/me
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "amount": "50000",
  "paymentMethod": "MTN_MOBILE_MONEY"
}
```

### **Get Available Departments (for frontend dropdown):**
```bash
GET /api/patient/departments
```

**Response includes:**
- Cardiology, Orthopedics, Pediatrics, General Medicine, Emergency Medicine
- Each with unique UUID, name, description, and hospital info
- **Frontend Usage**: Use department names in appointment requests, not UUIDs