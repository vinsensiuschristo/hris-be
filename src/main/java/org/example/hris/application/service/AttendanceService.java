package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Attendance;
import org.example.hris.domain.model.Employee;
import org.example.hris.domain.repository.AttendanceRepository;
import org.example.hris.domain.repository.EmployeeRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, @Lazy EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Default work start time (can be moved to configuration)
    private static final LocalTime JAM_KERJA_MULAI = LocalTime.of(8, 0); // 08:00
    private static final int TOLERANSI_MENIT = 15; // 15 minutes grace period

    public static final String STATUS_HADIR = "HADIR";
    public static final String STATUS_TERLAMBAT = "TERLAMBAT";
    public static final String STATUS_IZIN = "IZIN";
    public static final String STATUS_SAKIT = "SAKIT";
    public static final String STATUS_ALPHA = "ALPHA";

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(UUID id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
    }

    public List<Attendance> getAttendancesByKaryawanId(UUID karyawanId) {
        return attendanceRepository.findByKaryawanId(karyawanId);
    }

    public List<Attendance> getAttendancesByDate(LocalDate date) {
        return attendanceRepository.findByTanggal(date);
    }

    public List<Attendance> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateRange(startDate, endDate);
    }

    public List<Attendance> getAttendancesByStatus(String status) {
        return attendanceRepository.findByStatus(status);
    }

    public List<Attendance> getAttendancesByDepartmentAndDateRange(UUID departmentId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDepartmentAndDateRange(departmentId, startDate, endDate);
    }

    @Transactional
    public Attendance checkIn(UUID karyawanId, LocalTime jamMasuk, String keterangan) {
        LocalDate today = LocalDate.now();
        LocalTime actualJamMasuk = jamMasuk != null ? jamMasuk : LocalTime.now();

        // Get employee
        Employee karyawan = employeeRepository.findById(karyawanId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + karyawanId));

        // Check if already checked in today
        if (attendanceRepository.findByKaryawanIdAndTanggal(karyawanId, today).isPresent()) {
            throw new RuntimeException("Karyawan sudah melakukan check-in hari ini");
        }

        // Calculate lateness
        int keterlambatanMenit = 0;
        String status = STATUS_HADIR;
        LocalTime batasWaktu = JAM_KERJA_MULAI.plusMinutes(TOLERANSI_MENIT);

        if (actualJamMasuk.isAfter(batasWaktu)) {
            keterlambatanMenit = (int) ChronoUnit.MINUTES.between(JAM_KERJA_MULAI, actualJamMasuk);
            status = STATUS_TERLAMBAT;
        }

        // Create attendance
        Attendance attendance = Attendance.builder()
                .karyawan(karyawan)
                .tanggal(today)
                .jamMasuk(actualJamMasuk)
                .status(status)
                .keterlambatanMenit(keterlambatanMenit)
                .keterangan(keterangan)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance checkOut(UUID karyawanId, LocalTime jamKeluar) {
        LocalDate today = LocalDate.now();
        LocalTime actualJamKeluar = jamKeluar != null ? jamKeluar : LocalTime.now();

        // Get today's attendance
        Attendance attendance = attendanceRepository.findByKaryawanIdAndTanggal(karyawanId, today)
                .orElseThrow(() -> new RuntimeException("Karyawan belum melakukan check-in hari ini"));

        if (attendance.getJamKeluar() != null) {
            throw new RuntimeException("Karyawan sudah melakukan check-out hari ini");
        }

        // Update with check-out time
        attendance.setJamKeluar(actualJamKeluar);
        attendance.setUpdatedAt(Instant.now());

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance recordAbsence(UUID karyawanId, LocalDate tanggal, String status, String keterangan) {
        // Validate status
        if (!STATUS_IZIN.equals(status) && !STATUS_SAKIT.equals(status) && !STATUS_ALPHA.equals(status)) {
            throw new RuntimeException("Status tidak valid untuk ketidakhadiran. Gunakan: IZIN, SAKIT, atau ALPHA");
        }

        // Get employee
        Employee karyawan = employeeRepository.findById(karyawanId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + karyawanId));

        // Check if already has attendance for that date
        if (attendanceRepository.findByKaryawanIdAndTanggal(karyawanId, tanggal).isPresent()) {
            throw new RuntimeException("Sudah ada data kehadiran untuk tanggal tersebut");
        }

        // Create absence record
        Attendance attendance = Attendance.builder()
                .karyawan(karyawan)
                .tanggal(tanggal)
                .status(status)
                .keterlambatanMenit(0)
                .keterangan(keterangan)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return attendanceRepository.save(attendance);
    }
}
