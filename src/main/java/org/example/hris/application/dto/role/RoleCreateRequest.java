package org.example.hris.application.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateRequest {

    @NotBlank(message = "Nama Rolet wajib diisi")
    private String namaRole;
}
