package org.example.hris.application.dto.attendance;

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
public class AttendanceResponse {
    private UUID id;
    private EmployeeSummary karyawan;
    private LocalDate tanggal;
    private LocalTime jamMasuk;
    private LocalTime jamKeluar;
    private String status;
    private Integer keterlambatanMenit;
    private String keterangan;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeSummary {
        private UUID id;
        private String nama;
        private String nik;
        private String email;
        private String departemen;
        private String jabatan;
    }
}
