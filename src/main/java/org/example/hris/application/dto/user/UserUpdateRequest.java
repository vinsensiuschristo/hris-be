package org.example.hris.application.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserUpdateRequest {
    private String username;
    private String password;
    private UUID roleId;
    private UUID karyawanId;
}
