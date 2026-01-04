package org.example.hris.application.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyDashboardStatsResponse {
    
    // Personal attendance stats
    private Integer attendanceThisMonth;
    private Integer lateThisMonth;
    private Integer totalLateMenit;
    
    // Sisa cuti
    private Integer sisaCuti;
    
    // Today's attendance
    private String todayStatus;  // HADIR, TERLAMBAT, BELUM_CHECKIN, SUDAH_CHECKOUT
    private String todayCheckIn;
    private String todayCheckOut;
    
    // Pending requests
    private Integer pendingLeaveRequests;
    private Integer pendingOvertimeRequests;
    
    // Approved requests this month
    private Integer approvedLeaveThisMonth;
    private Integer approvedOvertimeThisMonth;
}
