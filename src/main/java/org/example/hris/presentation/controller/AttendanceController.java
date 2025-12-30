package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.attendance.AttendanceResponse;
import org.example.hris.application.dto.attendance.CheckInRequest;
import org.example.hris.application.dto.attendance.CheckOutRequest;
import org.example.hris.application.service.AttendanceService;
import org.example.hris.domain.model.Attendance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Attendance tracking and check-in/out APIs")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    @Operation(summary = "Get all attendances with optional filters")
    public ResponseEntity<List<AttendanceResponse>> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID departmentId
    ) {
        List<Attendance> attendances;

        if (departmentId != null && startDate != null && endDate != null) {
            attendances = attendanceService.getAttendancesByDepartmentAndDateRange(departmentId, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            attendances = attendanceService.getAttendancesByDateRange(startDate, endDate);
        } else if (status != null) {
            attendances = attendanceService.getAttendancesByStatus(status);
        } else {
            attendances = attendanceService.getAllAttendances();
        }

        List<AttendanceResponse> responses = attendances.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attendance by ID")
    public ResponseEntity<AttendanceResponse> findById(@PathVariable UUID id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(toResponse(attendance));
    }

    @GetMapping("/karyawan/{karyawanId}")
    @Operation(summary = "Get attendances by employee ID")
    public ResponseEntity<List<AttendanceResponse>> findByKaryawanId(@PathVariable UUID karyawanId) {
        List<AttendanceResponse> responses = attendanceService.getAttendancesByKaryawanId(karyawanId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get attendances by specific date")
    public ResponseEntity<List<AttendanceResponse>> findByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<AttendanceResponse> responses = attendanceService.getAttendancesByDate(date)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/check-in")
    @Operation(summary = "Employee check-in (clock in)")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        Attendance attendance = attendanceService.checkIn(
                request.getKaryawanId(),
                request.getJamMasuk(),
                request.getKeterangan()
        );
        return ResponseEntity.ok(toResponse(attendance));
    }

    @PostMapping("/check-out")
    @Operation(summary = "Employee check-out (clock out)")
    public ResponseEntity<AttendanceResponse> checkOut(@Valid @RequestBody CheckOutRequest request) {
        Attendance attendance = attendanceService.checkOut(
                request.getKaryawanId(),
                request.getJamKeluar()
        );
        return ResponseEntity.ok(toResponse(attendance));
    }

    @PostMapping("/absence")
    @Operation(summary = "Record employee absence (IZIN, SAKIT, ALPHA)")
    public ResponseEntity<AttendanceResponse> recordAbsence(
            @RequestParam UUID karyawanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggal,
            @RequestParam String status,
            @RequestParam(required = false) String keterangan
    ) {
        Attendance attendance = attendanceService.recordAbsence(karyawanId, tanggal, status, keterangan);
        return ResponseEntity.ok(toResponse(attendance));
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        AttendanceResponse.AttendanceResponseBuilder builder = AttendanceResponse.builder()
                .id(attendance.getId())
                .tanggal(attendance.getTanggal())
                .jamMasuk(attendance.getJamMasuk())
                .jamKeluar(attendance.getJamKeluar())
                .status(attendance.getStatus())
                .keterlambatanMenit(attendance.getKeterlambatanMenit())
                .keterangan(attendance.getKeterangan())
                .createdAt(attendance.getCreatedAt())
                .updatedAt(attendance.getUpdatedAt());

        if (attendance.getKaryawan() != null) {
            var emp = attendance.getKaryawan();
            builder.karyawan(AttendanceResponse.EmployeeSummary.builder()
                    .id(emp.getId())
                    .nama(emp.getNama())
                    .nik(emp.getNik())
                    .email(emp.getEmail())
                    .departemen(emp.getDepartemen() != null ? emp.getDepartemen().getNamaDepartement() : null)
                    .jabatan(emp.getJabatan() != null ? emp.getJabatan().getNamaJabatan() : null)
                    .build());
        }

        return builder.build();
    }
}
