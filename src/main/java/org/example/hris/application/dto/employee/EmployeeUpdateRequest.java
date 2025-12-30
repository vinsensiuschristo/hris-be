package org.example.hris.application.dto.employee;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeUpdateRequest {

    private String nama;

    @Email(message = "Format email tidak valid")
    private String email;

    private UUID jabatanId;

    private UUID departemenId;

    private Integer sisaCuti;
}
