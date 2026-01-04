package org.example.hris.application.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserCreateRequest {
    @NotBlank(message = "Username wajib diisi")
    @Size(min = 3, message = "Username minimal 3 karakter")
    private String username;

    @NotBlank(message = "Password wajib diisi")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    private UUID roleId;
    
    private UUID karyawanId;
}
