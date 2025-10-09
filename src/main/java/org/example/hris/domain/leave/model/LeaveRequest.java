package org.example.hris.domain.leave.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hris.domain.employee.model.Employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}