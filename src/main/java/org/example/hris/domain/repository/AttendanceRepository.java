package org.example.hris.domain.repository;

import org.example.hris.domain.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository {
    Attendance save(Attendance attendance);

    List<Attendance> findAll();

    Optional<Attendance> findById(UUID id);

    List<Attendance> findByKaryawanId(UUID karyawanId);

    Optional<Attendance> findByKaryawanIdAndTanggal(UUID karyawanId, LocalDate tanggal);

    List<Attendance> findByTanggal(LocalDate tanggal);

    List<Attendance> findByDateRange(LocalDate startDate, LocalDate endDate);

    List<Attendance> findByStatus(String status);

    List<Attendance> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String status);

    List<Attendance> findByDepartmentAndDateRange(UUID departmentId, LocalDate startDate, LocalDate endDate);

    void deleteById(UUID id);
}
