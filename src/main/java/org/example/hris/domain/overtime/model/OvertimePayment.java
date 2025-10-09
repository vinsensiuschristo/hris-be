package org.example.hris.domain.overtime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hris.domain.employee.model.Employee;
import org.example.hris.domain.leave.model.RequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimePayment {
    private UUID id;
    private OvertimeRequest pengajuanLembur;
    private Employee finance;
    private LocalDate tglPembayaran;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}