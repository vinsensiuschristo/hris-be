-- =====================================================
-- HRIS Database Schema (V1) - FINAL & IMMUTABLE
-- =====================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- //////////////////////////////////////////////////
-- Master Data
-- //////////////////////////////////////////////////

CREATE TABLE positions (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           nama_jabatan VARCHAR(100) NOT NULL
);

CREATE TABLE departments (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             nama_departemen VARCHAR(100) NOT NULL
);

CREATE TABLE leave_types (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             nama_jenis VARCHAR(100) NOT NULL
);

CREATE TABLE request_statuses (
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                  nama_status VARCHAR(50) NOT NULL
);

CREATE TABLE employees (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           nama VARCHAR(150) NOT NULL,
                           nik VARCHAR(50) UNIQUE NOT NULL,
                           jabatan_id UUID REFERENCES positions(id) ON DELETE SET NULL,
                           departemen_id UUID REFERENCES departments(id) ON DELETE SET NULL,
                           email VARCHAR(150) UNIQUE NOT NULL,
                           sisa_cuti INT DEFAULT 0,
                           created_at TIMESTAMP DEFAULT NOW(),
                           updated_at TIMESTAMP DEFAULT NOW()
);

-- //////////////////////////////////////////////////
-- Transaksional
-- //////////////////////////////////////////////////

CREATE TABLE leave_requests (
                                id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                karyawan_id UUID NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                                jenis_cuti_id UUID NOT NULL REFERENCES leave_types(id) ON DELETE CASCADE,
                                status_id UUID NOT NULL REFERENCES request_statuses(id) ON DELETE SET NULL,
                                tgl_mulai DATE NOT NULL,
                                tgl_selesai DATE NOT NULL,
                                alasan TEXT,
                                created_at TIMESTAMP DEFAULT NOW(),
                                updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE overtime_requests (
                                   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                   karyawan_id UUID NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                                   tgl_lembur DATE NOT NULL,
                                   jam_mulai TIME NOT NULL,
                                   jam_selesai TIME NOT NULL,
                                   durasi INT NOT NULL,
                                   estimasi_biaya NUMERIC(15,2) DEFAULT 0,
                                   status_id UUID NOT NULL REFERENCES request_statuses(id) ON DELETE SET NULL,
                                   created_at TIMESTAMP DEFAULT NOW(),
                                   updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE overtime_evidences (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    overtime_request_id UUID NOT NULL REFERENCES overtime_requests(id) ON DELETE CASCADE,
                                    file_path TEXT NOT NULL,
                                    file_type VARCHAR(50),
                                    uploaded_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE overtime_payments (
                                   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                   pengajuan_lembur_id UUID NOT NULL REFERENCES overtime_requests(id) ON DELETE CASCADE,
                                   finance_id UUID REFERENCES employees(id) ON DELETE SET NULL,
                                   tgl_pembayaran DATE,
                                   status_id UUID NOT NULL REFERENCES request_statuses(id) ON DELETE SET NULL,
                                   created_at TIMESTAMP DEFAULT NOW(),
                                   updated_at TIMESTAMP DEFAULT NOW()
);

-- //////////////////////////////////////////////////
-- Security & Admin
-- //////////////////////////////////////////////////

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       karyawan_id UUID REFERENCES employees(id) ON DELETE SET NULL
);

CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       nama_role VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            user_id UUID REFERENCES users(id) ON DELETE SET NULL,
                            action VARCHAR(150) NOT NULL,
                            tabel VARCHAR(100) NOT NULL,
                            record_id UUID,
                            timestamp TIMESTAMP DEFAULT NOW()
);
