package org.example.hris.application.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeCreateRequest {

    @NotBlank(message = "Nama wajib diisi")
    private String nama;

    @NotBlank(message = "NIK wajib diisi")
    private String nik;

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    private String email;

    private UUID jabatanId;

    private UUID departemenId;

    private Integer sisaCuti;
}
