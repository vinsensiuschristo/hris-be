package org.example.hris.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeRequest {
    private UUID id;
    private Employee karyawan;
    private LocalDate tglLembur;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private Integer durasi;
    private BigDecimal estimasiBiaya;
    private RequestStatus status;
    private String alasanPenolakan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
