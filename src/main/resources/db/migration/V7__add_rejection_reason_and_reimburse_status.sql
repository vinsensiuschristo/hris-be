-- Add rejection reason column to leave_requests
ALTER TABLE leave_requests ADD COLUMN IF NOT EXISTS alasan_penolakan TEXT;

-- Add rejection reason column to overtime_requests
ALTER TABLE overtime_requests ADD COLUMN IF NOT EXISTS alasan_penolakan TEXT;

-- Insert new statuses for overtime reimburse flow (only if not exists)
INSERT INTO request_statuses (id, nama_status)
SELECT gen_random_uuid(), 'MENUNGGU_REIMBURSE'
WHERE NOT EXISTS (SELECT 1 FROM request_statuses WHERE nama_status = 'MENUNGGU_REIMBURSE');

INSERT INTO request_statuses (id, nama_status)
SELECT gen_random_uuid(), 'DIBAYAR'
WHERE NOT EXISTS (SELECT 1 FROM request_statuses WHERE nama_status = 'DIBAYAR');

