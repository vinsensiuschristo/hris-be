package org.example.hris.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hris.application.dto.role.RoleResponse;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private List<RoleResponse> roles;
    private KaryawanInfo karyawan;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KaryawanInfo {
        private UUID id;
        private String nama;
        private String nik;
    }
}
