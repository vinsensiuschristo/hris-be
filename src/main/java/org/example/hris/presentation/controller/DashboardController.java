package org.example.hris.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.dashboard.DashboardStatsResponse;
import org.example.hris.application.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard analytics and statistics APIs for charts")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics with optional filters")
    public ResponseEntity<DashboardStatsResponse> getStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID departmentId
    ) {
        DashboardStatsResponse stats = dashboardService.getDashboardStats(startDate, endDate, departmentId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/monthly")
    @Operation(summary = "Get statistics for specific month and year")
    public ResponseEntity<DashboardStatsResponse> getMonthlyStats(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) UUID departmentId
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        DashboardStatsResponse stats = dashboardService.getDashboardStats(startDate, endDate, departmentId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/today")
    @Operation(summary = "Get today's statistics")
    public ResponseEntity<DashboardStatsResponse> getTodayStats(
            @RequestParam(required = false) UUID departmentId
    ) {
        LocalDate today = LocalDate.now();
        DashboardStatsResponse stats = dashboardService.getDashboardStats(today, today, departmentId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/attendance-summary")
    @Operation(summary = "Get attendance summary for charts")
    public ResponseEntity<DashboardStatsResponse.AttendanceSummary> getAttendanceSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID departmentId
    ) {
        DashboardStatsResponse stats = dashboardService.getDashboardStats(startDate, endDate, departmentId);
        return ResponseEntity.ok(stats.getAttendanceSummary());
    }

    @GetMapping("/late-ranking")
    @Operation(summary = "Get top late employees for charts")
    public ResponseEntity<java.util.List<DashboardStatsResponse.LateEmployee>> getLateRanking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID departmentId
    ) {
        DashboardStatsResponse stats = dashboardService.getDashboardStats(startDate, endDate, departmentId);
        return ResponseEntity.ok(stats.getTopLateEmployees());
    }

    @GetMapping("/overtime-ranking")
    @Operation(summary = "Get top overtime employees for charts")
    public ResponseEntity<java.util.List<DashboardStatsResponse.OvertimeEmployee>> getOvertimeRanking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID departmentId
    ) {
        DashboardStatsResponse stats = dashboardService.getDashboardStats(startDate, endDate, departmentId);
        return ResponseEntity.ok(stats.getTopOvertimeEmployees());
    }

    @GetMapping("/leave-ranking")
    @Operation(summary = "Get top leave employees for charts")
    public ResponseEntity<java.util.List<DashboardStatsResponse.LeaveEmployee>> getLeaveRanking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID departmentId
    ) {
        DashboardStatsResponse stats = dashboardService.getDashboardStats(startDate, endDate, departmentId);
        return ResponseEntity.ok(stats.getTopLeaveEmployees());
    }
}
