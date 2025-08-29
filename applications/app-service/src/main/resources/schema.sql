CREATE TABLE credit_requests (
     id BIGSERIAL PRIMARY KEY,            -- Identificador único
     amount NUMERIC(15,2) NOT NULL,       -- Monto solicitado
     terms INT NOT NULL,                  -- Plazo en meses, por ejemplo
     email VARCHAR(150) NOT NULL,         -- Correo del solicitante
     states VARCHAR(50) NOT NULL,         -- Enum States (guardado como texto)
     loan_type VARCHAR(50) NOT NULL,      -- Enum LoanType (guardado como texto)
     document_id VARCHAR(50) NOT NULL     -- Documento de identidad
);

-- Optional: índices útiles
-- CREATE INDEX idx_credit_requests_email ON credit_requests(email);
-- CREATE INDEX idx_credit_requests_document ON credit_requests(document_id);
-- CREATE INDEX idx_credit_requests_states ON credit_requests(states);

CREATE TABLE IF NOT EXISTS loan_type (
    id  BIGSERIAL PRIMARY KEY,
    unique_code VARCHAR(50)  NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    minimum_amount NUMERIC(19,2) NOT NULL CHECK (minimum_amount >= 0),
    maximum_amount NUMERIC(19,2) NOT NULL CHECK (maximum_amount >= minimum_amount),
    interest_rate NUMERIC(5,2)  NOT NULL CHECK (interest_rate >= 0 AND interest_rate <= 100),
    automatic_validation BOOLEAN       NOT NULL DEFAULT FALSE
    );

-- Índices opcionales si consultas por código y nombre
CREATE INDEX IF NOT EXISTS idx_loan_type_name ON loan_type (name);