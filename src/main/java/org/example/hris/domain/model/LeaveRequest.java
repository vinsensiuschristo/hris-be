package org.example.hris.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    private UUID id;
    private Employee karyawan;
    private LeaveType jenisCuti;
    private RequestStatus status;
    private LocalDate tglMulai;
    private LocalDate tglSelesai;
    private String alasan;
    private String alasanPenolakan;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}