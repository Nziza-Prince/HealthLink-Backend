CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     first_name VARCHAR(255),
    last_name VARCHAR(255),
    national_id VARCHAR(50),
    email VARCHAR(255) UNIQUE NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    phone_number VARCHAR(50),
    otp VARCHAR(10),
    address VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('PATIENT', 'DOCTOR'))
    );

CREATE TABLE IF NOT EXISTS refresh_tokens (
                                              id SERIAL PRIMARY KEY,
                                              user_id BIGINT NOT NULL REFERENCES users(id),
    token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL
    );

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO healthlink_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO healthlink_user;
GRANT USAGE ON SCHEMA public TO healthlink_user;