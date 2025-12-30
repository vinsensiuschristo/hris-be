package org.example.hris.application.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    
    // Summary counts
    private Integer totalKaryawan;
    private AttendanceSummary attendanceSummary;
    private LeaveSummary leaveSummary;
    private OvertimeSummary overtimeSummary;
    
    // Rankings for charts
    private List<LateEmployee> topLateEmployees;
    private List<OvertimeEmployee> topOvertimeEmployees;
    private List<LeaveEmployee> topLeaveEmployees;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendanceSummary {
        private Long totalHadir;
        private Long totalTerlambat;
        private Long totalIzin;
        private Long totalSakit;
        private Long totalAlpha;
        private Long totalKeterlambatanMenit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveSummary {
        private Long totalPengajuan;
        private Long disetujui;
        private Long ditolak;
        private Long menunggu;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OvertimeSummary {
        private Long totalPengajuan;
        private Long disetujui;
        private Long ditolak;
        private Long menunggu;
        private BigDecimal totalBiaya;
        private Integer totalJamLembur;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LateEmployee {
        private UUID id;
        private String nama;
        private String nik;
        private String departemen;
        private Integer jumlahTerlambat;
        private Integer totalMenitTerlambat;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OvertimeEmployee {
        private UUID id;
        private String nama;
        private String nik;
        private String departemen;
        private Integer jumlahHariLembur;
        private Integer totalJamLembur;
        private BigDecimal totalBiayaLembur;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveEmployee {
        private UUID id;
        private String nama;
        private String nik;
        private String departemen;
        private Integer jumlahCuti;
        private Integer totalHariCuti;
    }
}
