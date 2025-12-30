package org.example.hris.application.dto.overtimeRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeRequestResponse {
    private UUID id;
    private EmployeeSummary karyawan;
    private LocalDate tglLembur;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private Integer durasi;
    private BigDecimal estimasiBiaya;
    private StatusResponse status;
    private List<EvidenceSummary> evidences;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeSummary {
        private UUID id;
        private String nama;
        private String nik;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusResponse {
        private UUID id;
        private String namaStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvidenceSummary {
        private UUID id;
        private String filePath;
        private String fileType;
        private LocalDateTime uploadedAt;
    }
}
