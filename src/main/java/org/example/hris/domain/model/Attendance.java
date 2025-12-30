package org.example.hris.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    private UUID id;
    private Employee karyawan;
    private LocalDate tanggal;
    private LocalTime jamMasuk;
    private LocalTime jamKeluar;
    private String status; // HADIR, TERLAMBAT, IZIN, SAKIT, ALPHA
    private Integer keterlambatanMenit;
    private String keterangan;
    private Instant createdAt;
    private Instant updatedAt;
}
