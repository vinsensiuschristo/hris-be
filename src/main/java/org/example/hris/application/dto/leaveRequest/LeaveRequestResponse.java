package org.example.hris.application.dto.leaveRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hris.application.dto.leaveType.LeaveTypeResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponse {
    private UUID id;
    private EmployeeSummary karyawan;
    private LeaveTypeResponse jenisCuti;
    private StatusResponse status;
    private LocalDate tglMulai;
    private LocalDate tglSelesai;
    private String alasan;
    private long jumlahHari;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeSummary {
        private UUID id;
        private String nama;
        private String nik;
        private String email;
        private Integer sisaCuti;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusResponse {
        private UUID id;
        private String namaStatus;
    }
}
