-- =====================================================
-- HRIS Database Schema (V2) - Seed Master Data
-- =====================================================

-- Seed data untuk tabel Roles (sesuai Use Case)
-- UUID akan digenerate otomatis oleh default uuid_generate_v4()
INSERT INTO roles (nama_role)
VALUES ('KARYAWAN'),
       ('MANAJER'),
       ('ADMIN'),
       ('FINANCE');

-- Seed data untuk tabel Request Statuses
-- Ini bisa dipakai untuk Cuti dan Lembur Request
INSERT INTO request_statuses (nama_status)
VALUES ('MENUNGGU_PERSETUJUAN'),
       ('DISETUJUI'),
       ('DITOLAK'),
       ('DIBATALKAN');

-- Status khusus untuk Overtime Payment
INSERT INTO request_statuses (nama_status)
VALUES ('BELUM_DIPROSES'),
       ('SEDANG_DIPROSES'),
       ('SUDAH_DICAIRKAN');