package org.example.hris.application.dto.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentCreateRequest {

    @NotBlank(message = "Nama department wajib diisi")
    private String namaDepartment;
}

