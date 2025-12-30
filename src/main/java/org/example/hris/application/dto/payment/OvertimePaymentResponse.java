package org.example.hris.application.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimePaymentResponse {
    private UUID id;
    private OvertimeRequestSummary pengajuanLembur;
    private EmployeeSummary finance;
    private LocalDate tglPembayaran;
    private StatusResponse status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OvertimeRequestSummary {
        private UUID id;
        private EmployeeSummary karyawan;
        private LocalDate tglLembur;
        private Integer durasi;
        private BigDecimal estimasiBiaya;
        private String statusLembur;
    }

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
}
