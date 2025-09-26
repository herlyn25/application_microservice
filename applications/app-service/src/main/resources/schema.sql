-- Crear tabla states
CREATE TABLE IF NOT EXISTS states (
    id SERIAL PRIMARY KEY,          -- id autoincrementable
    name VARCHAR(255) UNIQUE NOT NULL,  -- name debe ser único y no nulo
    description TEXT               -- description puede ser texto largo
    );

-- Insertar en tabla states
insert into states (name,description)
values ('PPV','Pendiente'),
    ('APROB', 'Aprobado'),
    ('RCHZ', 'Rechazado'),
    ('RVM', 'Revision manual')
ON CONFLICT(name) DO NOTHING;

-- Crear tabla de tipos de préstamo primero (por las dependencias)
CREATE TABLE IF NOT EXISTS loan_type (
    id BIGSERIAL PRIMARY KEY,
    unique_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    minimum_amount NUMERIC(19,2) NOT NULL CHECK (minimum_amount >= 0),
    maximum_amount NUMERIC(19,2) NOT NULL CHECK (maximum_amount >= minimum_amount),
    interest_rate NUMERIC(5,2) NOT NULL CHECK (interest_rate >= 0 AND interest_rate <= 100),
    automatic_validation BOOLEAN NOT NULL DEFAULT FALSE
    );

-- Insertar los tipos de préstamo predefinidos
INSERT INTO loan_type (id, unique_code, name, minimum_amount, maximum_amount, interest_rate, automatic_validation)
VALUES
    (1, 'MORT-001', 'Mortgage Loan', 50000, 500000, 0.045, false),
    (2, 'PERS-001', 'Personal Loan', 1000, 50000, 0.12, true),
    (3, 'VEH-001', 'Vehicle Loan', 5000, 100000, 0.085, true),
    (4, 'STUD-001', 'Student Loan', 500, 40000, 0.05, false)
    ON CONFLICT (unique_code) DO NOTHING;

-- Crear tabla de solicitudes de crédito
CREATE TABLE IF NOT EXISTS credit_requests (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(15,2) NOT NULL,
    terms INT NOT NULL,
    email VARCHAR(150) NOT NULL,
    states VARCHAR(50),
    loan_type VARCHAR(120) NOT NULL,  -- Cambiado para almacenar el nombre del loan type
    document_id VARCHAR(50) NOT NULL,
    created DATE NOT NULL
    );

-- Índices útiles
CREATE INDEX IF NOT EXISTS idx_credit_requests_email ON credit_requests(email);
CREATE INDEX IF NOT EXISTS idx_credit_requests_document ON credit_requests(document_id);
CREATE INDEX IF NOT EXISTS idx_credit_requests_states ON credit_requests(states);
CREATE INDEX IF NOT EXISTS idx_loan_type_name ON loan_type (name);
CREATE INDEX IF NOT EXISTS idx_loan_type_code ON loan_type (unique_code);