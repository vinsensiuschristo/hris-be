-- V5: Add work_hours_settings table for configurable work hours
-- Default: 08:30-17:45, 2 minute tolerance

CREATE TABLE IF NOT EXISTS work_hours_settings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    jam_masuk TIME NOT NULL,
    jam_keluar TIME NOT NULL,
    toleransi_menit INTEGER NOT NULL DEFAULT 2,
    is_active BOOLEAN NOT NULL DEFAULT true,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed default work hours settings
INSERT INTO work_hours_settings (id, jam_masuk, jam_keluar, toleransi_menit, is_active, description)
VALUES (
    gen_random_uuid(),
    '08:30:00',
    '17:45:00',
    2,
    true,
    'Default work hours settings'
);
