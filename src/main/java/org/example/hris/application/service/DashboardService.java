package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.dashboard.DashboardStatsResponse;
import org.example.hris.domain.model.*;
import org.example.hris.domain.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final OvertimeRequestRepository overtimeRequestRepository;

    private static final String STATUS_DISETUJUI = "DISETUJUI";
    private static final String STATUS_DITOLAK = "DITOLAK";
    private static final String STATUS_MENUNGGU = "MENUNGGU_PERSETUJUAN";

    public DashboardStatsResponse getDashboardStats(LocalDate startDate, LocalDate endDate, UUID departmentId) {
        // Default to current month if not specified
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // Get all employees count
        List<Employee> allEmployees = employeeRepository.findAll();
        int totalKaryawan = allEmployees.size();

        // Get attendances in date range
        List<Attendance> attendances;
        if (departmentId != null) {
            attendances = attendanceRepository.findByDepartmentAndDateRange(departmentId, startDate, endDate);
        } else {
            attendances = attendanceRepository.findByDateRange(startDate, endDate);
        }

        // Calculate attendance summary
        DashboardStatsResponse.AttendanceSummary attendanceSummary = calculateAttendanceSummary(attendances);

        // Get leave requests
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll().stream()
                .filter(lr -> !lr.getTglMulai().isBefore(startDate) && !lr.getTglMulai().isAfter(endDate))
                .toList();
        DashboardStatsResponse.LeaveSummary leaveSummary = calculateLeaveSummary(leaveRequests);

        // Get overtime requests
        List<OvertimeRequest> overtimeRequests = overtimeRequestRepository.findAll().stream()
                .filter(or -> !or.getTglLembur().isBefore(startDate) && !or.getTglLembur().isAfter(endDate))
                .toList();
        DashboardStatsResponse.OvertimeSummary overtimeSummary = calculateOvertimeSummary(overtimeRequests);

        // Calculate rankings
        List<DashboardStatsResponse.LateEmployee> topLate = getTopLateEmployees(attendances, 10);
        List<DashboardStatsResponse.OvertimeEmployee> topOvertime = getTopOvertimeEmployees(overtimeRequests, 10);
        List<DashboardStatsResponse.LeaveEmployee> topLeave = getTopLeaveEmployees(leaveRequests, 10);

        return DashboardStatsResponse.builder()
                .totalKaryawan(totalKaryawan)
                .attendanceSummary(attendanceSummary)
                .leaveSummary(leaveSummary)
                .overtimeSummary(overtimeSummary)
                .topLateEmployees(topLate)
                .topOvertimeEmployees(topOvertime)
                .topLeaveEmployees(topLeave)
                .build();
    }

    private DashboardStatsResponse.AttendanceSummary calculateAttendanceSummary(List<Attendance> attendances) {
        long totalHadir = attendances.stream().filter(a -> "HADIR".equals(a.getStatus())).count();
        long totalTerlambat = attendances.stream().filter(a -> "TERLAMBAT".equals(a.getStatus())).count();
        long totalIzin = attendances.stream().filter(a -> "IZIN".equals(a.getStatus())).count();
        long totalSakit = attendances.stream().filter(a -> "SAKIT".equals(a.getStatus())).count();
        long totalAlpha = attendances.stream().filter(a -> "ALPHA".equals(a.getStatus())).count();
        long totalKeterlambatan = attendances.stream()
                .filter(a -> a.getKeterlambatanMenit() != null)
                .mapToLong(Attendance::getKeterlambatanMenit)
                .sum();

        return DashboardStatsResponse.AttendanceSummary.builder()
                .totalHadir(totalHadir + totalTerlambat) // TERLAMBAT juga dihitung hadir
                .totalTerlambat(totalTerlambat)
                .totalIzin(totalIzin)
                .totalSakit(totalSakit)
                .totalAlpha(totalAlpha)
                .totalKeterlambatanMenit(totalKeterlambatan)
                .build();
    }

    private DashboardStatsResponse.LeaveSummary calculateLeaveSummary(List<LeaveRequest> leaveRequests) {
        long disetujui = leaveRequests.stream()
                .filter(lr -> lr.getStatus() != null && STATUS_DISETUJUI.equals(lr.getStatus().getNamaStatus()))
                .count();
        long ditolak = leaveRequests.stream()
                .filter(lr -> lr.getStatus() != null && STATUS_DITOLAK.equals(lr.getStatus().getNamaStatus()))
                .count();
        long menunggu = leaveRequests.stream()
                .filter(lr -> lr.getStatus() != null && STATUS_MENUNGGU.equals(lr.getStatus().getNamaStatus()))
                .count();

        return DashboardStatsResponse.LeaveSummary.builder()
                .totalPengajuan((long) leaveRequests.size())
                .disetujui(disetujui)
                .ditolak(ditolak)
                .menunggu(menunggu)
                .build();
    }

    private DashboardStatsResponse.OvertimeSummary calculateOvertimeSummary(List<OvertimeRequest> overtimeRequests) {
        long disetujui = overtimeRequests.stream()
                .filter(or -> or.getStatus() != null && STATUS_DISETUJUI.equals(or.getStatus().getNamaStatus()))
                .count();
        long ditolak = overtimeRequests.stream()
                .filter(or -> or.getStatus() != null && STATUS_DITOLAK.equals(or.getStatus().getNamaStatus()))
                .count();
        long menunggu = overtimeRequests.stream()
                .filter(or -> or.getStatus() != null && STATUS_MENUNGGU.equals(or.getStatus().getNamaStatus()))
                .count();

        BigDecimal totalBiaya = overtimeRequests.stream()
                .filter(or -> or.getStatus() != null && STATUS_DISETUJUI.equals(or.getStatus().getNamaStatus()))
                .map(or -> or.getEstimasiBiaya() != null ? or.getEstimasiBiaya() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalJam = overtimeRequests.stream()
                .filter(or -> or.getStatus() != null && STATUS_DISETUJUI.equals(or.getStatus().getNamaStatus()))
                .mapToInt(or -> or.getDurasi() != null ? or.getDurasi() : 0)
                .sum();

        return DashboardStatsResponse.OvertimeSummary.builder()
                .totalPengajuan((long) overtimeRequests.size())
                .disetujui(disetujui)
                .ditolak(ditolak)
                .menunggu(menunggu)
                .totalBiaya(totalBiaya)
                .totalJamLembur(totalJam)
                .build();
    }

    private List<DashboardStatsResponse.LateEmployee> getTopLateEmployees(List<Attendance> attendances, int limit) {
        // Group by employee and calculate totals
        Map<UUID, List<Attendance>> byEmployee = attendances.stream()
                .filter(a -> "TERLAMBAT".equals(a.getStatus()) && a.getKaryawan() != null)
                .collect(Collectors.groupingBy(a -> a.getKaryawan().getId()));

        return byEmployee.entrySet().stream()
                .map(entry -> {
                    List<Attendance> empAttendances = entry.getValue();
                    Attendance first = empAttendances.get(0);
                    Employee emp = first.getKaryawan();

                    int jumlahTerlambat = empAttendances.size();
                    int totalMenit = empAttendances.stream()
                            .mapToInt(a -> a.getKeterlambatanMenit() != null ? a.getKeterlambatanMenit() : 0)
                            .sum();

                    return DashboardStatsResponse.LateEmployee.builder()
                            .id(emp.getId())
                            .nama(emp.getNama())
                            .nik(emp.getNik())
                            .departemen(emp.getDepartemen() != null ? emp.getDepartemen().getNamaDepartement() : null)
                            .jumlahTerlambat(jumlahTerlambat)
                            .totalMenitTerlambat(totalMenit)
                            .build();
                })
                .sorted((a, b) -> b.getTotalMenitTerlambat().compareTo(a.getTotalMenitTerlambat()))
                .limit(limit)
                .toList();
    }

    private List<DashboardStatsResponse.OvertimeEmployee> getTopOvertimeEmployees(List<OvertimeRequest> overtimeRequests, int limit) {
        // Only count approved overtime
        Map<UUID, List<OvertimeRequest>> byEmployee = overtimeRequests.stream()
                .filter(or -> or.getStatus() != null && STATUS_DISETUJUI.equals(or.getStatus().getNamaStatus()) && or.getKaryawan() != null)
                .collect(Collectors.groupingBy(or -> or.getKaryawan().getId()));

        return byEmployee.entrySet().stream()
                .map(entry -> {
                    List<OvertimeRequest> empOvertimes = entry.getValue();
                    OvertimeRequest first = empOvertimes.get(0);
                    Employee emp = first.getKaryawan();

                    int jumlahHari = empOvertimes.size();
                    int totalJam = empOvertimes.stream()
                            .mapToInt(or -> or.getDurasi() != null ? or.getDurasi() : 0)
                            .sum();
                    BigDecimal totalBiaya = empOvertimes.stream()
                            .map(or -> or.getEstimasiBiaya() != null ? or.getEstimasiBiaya() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return DashboardStatsResponse.OvertimeEmployee.builder()
                            .id(emp.getId())
                            .nama(emp.getNama())
                            .nik(emp.getNik())
                            .departemen(emp.getDepartemen() != null ? emp.getDepartemen().getNamaDepartement() : null)
                            .jumlahHariLembur(jumlahHari)
                            .totalJamLembur(totalJam)
                            .totalBiayaLembur(totalBiaya)
                            .build();
                })
                .sorted((a, b) -> b.getTotalBiayaLembur().compareTo(a.getTotalBiayaLembur()))
                .limit(limit)
                .toList();
    }

    private List<DashboardStatsResponse.LeaveEmployee> getTopLeaveEmployees(List<LeaveRequest> leaveRequests, int limit) {
        // Only count approved leaves
        Map<UUID, List<LeaveRequest>> byEmployee = leaveRequests.stream()
                .filter(lr -> lr.getStatus() != null && STATUS_DISETUJUI.equals(lr.getStatus().getNamaStatus()) && lr.getKaryawan() != null)
                .collect(Collectors.groupingBy(lr -> lr.getKaryawan().getId()));

        return byEmployee.entrySet().stream()
                .map(entry -> {
                    List<LeaveRequest> empLeaves = entry.getValue();
                    LeaveRequest first = empLeaves.get(0);
                    Employee emp = first.getKaryawan();

                    int jumlahCuti = empLeaves.size();
                    int totalHari = empLeaves.stream()
                            .mapToInt(lr -> (int) (ChronoUnit.DAYS.between(lr.getTglMulai(), lr.getTglSelesai()) + 1))
                            .sum();

                    return DashboardStatsResponse.LeaveEmployee.builder()
                            .id(emp.getId())
                            .nama(emp.getNama())
                            .nik(emp.getNik())
                            .departemen(emp.getDepartemen() != null ? emp.getDepartemen().getNamaDepartement() : null)
                            .jumlahCuti(jumlahCuti)
                            .totalHariCuti(totalHari)
                            .build();
                })
                .sorted((a, b) -> b.getTotalHariCuti().compareTo(a.getTotalHariCuti()))
                .limit(limit)
                .toList();
    }
}
