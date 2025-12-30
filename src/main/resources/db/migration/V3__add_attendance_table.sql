-- =====================================================
-- HRIS Database Schema (V3) - Attendance Table
-- =====================================================

CREATE TABLE attendances (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    karyawan_id UUID NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    tanggal DATE NOT NULL,
    jam_masuk TIME,
    jam_keluar TIME,
    status VARCHAR(50) NOT NULL, -- HADIR, TERLAMBAT, IZIN, SAKIT, ALPHA
    keterlambatan_menit INT DEFAULT 0,
    keterangan TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(karyawan_id, tanggal)
);

-- Index for efficient queries
CREATE INDEX idx_attendances_karyawan_id ON attendances(karyawan_id);
CREATE INDEX idx_attendances_tanggal ON attendances(tanggal);
CREATE INDEX idx_attendances_status ON attendances(status);
