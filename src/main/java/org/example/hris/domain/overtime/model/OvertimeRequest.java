package org.example.hris.domain.overtime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hris.domain.employee.model.Employee;
import org.example.hris.domain.leave.model.RequestStatus;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
