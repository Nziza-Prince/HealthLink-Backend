-- Initialize the healthlink database
-- This script runs when the PostgreSQL container starts up

-- Create the database if it doesn't exist (though it should already exist from environment variables)
-- SELECT 'CREATE DATABASE healthlink' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'healthlink')\gexec

-- Connect to the healthlink database
\c healthlink;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- You can add any initial tables or data here
-- For example:
-- CREATE TABLE IF NOT EXISTS users (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     username VARCHAR(255) NOT NULL UNIQUE,
--     email VARCHAR(255) NOT NULL UNIQUE,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- Grant necessary permissions
GRANT ALL PRIVILEGES ON DATABASE healthlink TO healthlink_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO healthlink_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO healthlink_user; 