-- V4__seed_users.sql
-- Seed initial users for authentication

-- NOTE: Password hashes are BCrypt encoded
-- admin123 -> $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
-- hr123 -> $2a$10$xN7qT8PN8VZ3YHwGJJ/O4uPmMHq8AOxAWhV5C.Kg7L0zFcg5V0pTe
-- emp123 -> $2a$10$K.0HwpsoPDGaB/atFBmmXuGhcPc.5c6e9eKMV5VoJGjONTBgqPVi2

-- Insert users
-- Password: admin123
INSERT INTO users (id, username, password_hash) VALUES 
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG')
ON CONFLICT (id) DO NOTHING;

-- Password: hr123
INSERT INTO users (id, username, password_hash) VALUES 
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'hr_manager', '$2a$10$xN7qT8PN8VZ3YHwGJJ/O4uPmMHq8AOxAWhV5C.Kg7L0zFcg5V0pTe')
ON CONFLICT (id) DO NOTHING;

-- Password: emp123
INSERT INTO users (id, username, password_hash) VALUES 
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'employee', '$2a$10$K.0HwpsoPDGaB/atFBmmXuGhcPc.5c6e9eKMV5VoJGjONTBgqPVi2')
ON CONFLICT (id) DO NOTHING;

-- Get role IDs and link users to roles
-- First, we need to get the actual role IDs from the roles table
DO $$
DECLARE
    admin_role_id UUID;
    hr_role_id UUID;
    karyawan_role_id UUID;
BEGIN
    SELECT id INTO admin_role_id FROM roles WHERE nama_role = 'ADMIN' LIMIT 1;
    SELECT id INTO hr_role_id FROM roles WHERE nama_role = 'MANAJER' LIMIT 1;
    SELECT id INTO karyawan_role_id FROM roles WHERE nama_role = 'KARYAWAN' LIMIT 1;
    
    -- Link users to roles
    IF admin_role_id IS NOT NULL THEN
        INSERT INTO user_roles (user_id, role_id) VALUES 
            ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', admin_role_id)
        ON CONFLICT DO NOTHING;
    END IF;
    
    IF hr_role_id IS NOT NULL THEN
        INSERT INTO user_roles (user_id, role_id) VALUES 
            ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', hr_role_id)
        ON CONFLICT DO NOTHING;
    END IF;
    
    IF karyawan_role_id IS NOT NULL THEN
        INSERT INTO user_roles (user_id, role_id) VALUES 
            ('cccccccc-cccc-cccc-cccc-cccccccccccc', karyawan_role_id)
        ON CONFLICT DO NOTHING;
    END IF;
END $$;
