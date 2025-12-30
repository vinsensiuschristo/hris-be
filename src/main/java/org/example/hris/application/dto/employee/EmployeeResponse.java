package org.example.hris.application.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hris.application.dto.department.DepartmentResponse;
import org.example.hris.application.dto.position.PositionResponse;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private UUID id;
    private String nama;
    private String nik;
    private String email;
    private PositionResponse jabatan;
    private DepartmentResponse departemen;
    private Integer sisaCuti;
    private Instant createdAt;
    private Instant updatedAt;
}
