CREATE TABLE doctors (
                         id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         license_number VARCHAR(100) UNIQUE,
                         specialization VARCHAR(255),
                         date_of_birth DATE,
                         gender VARCHAR(50),
                         department_id UUID,
                         hospital_id UUID,
                         is_available BOOLEAN DEFAULT true
);