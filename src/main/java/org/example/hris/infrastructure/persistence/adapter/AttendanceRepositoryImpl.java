package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Attendance;
import org.example.hris.domain.repository.AttendanceRepository;
import org.example.hris.infrastructure.persistence.entity.AttendanceEntity;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.mapper.AttendanceMapper;
import org.example.hris.infrastructure.persistence.repository.AttendanceJpaRepository;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public Attendance save(Attendance attendance) {
        AttendanceEntity entity = attendanceMapper.toEntity(attendance);
        
        // Manually set the employee reference if present in domain model
        if (attendance.getKaryawan() != null && attendance.getKaryawan().getId() != null) {
            EmployeeEntity employee = employeeJpaRepository.findById(attendance.getKaryawan().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + attendance.getKaryawan().getId()));
            entity.setKaryawan(employee);
        }
        
        return attendanceMapper.toDomain(attendanceJpaRepository.save(entity));
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceJpaRepository.findAll()
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Attendance> findById(UUID id) {
        return attendanceJpaRepository.findById(id)
                .map(attendanceMapper::toDomain);
    }

    @Override
    public List<Attendance> findByKaryawanId(UUID karyawanId) {
        return attendanceJpaRepository.findByKaryawan_Id(karyawanId)
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Attendance> findByKaryawanIdAndTanggal(UUID karyawanId, LocalDate tanggal) {
        return attendanceJpaRepository.findByKaryawan_IdAndTanggal(karyawanId, tanggal)
                .map(attendanceMapper::toDomain);
    }

    @Override
    public List<Attendance> findByTanggal(LocalDate tanggal) {
        return attendanceJpaRepository.findByTanggal(tanggal)
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Attendance> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceJpaRepository.findByTanggalBetween(startDate, endDate)
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Attendance> findByStatus(String status) {
        return attendanceJpaRepository.findByStatus(status)
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Attendance> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String status) {
        return attendanceJpaRepository.findByDateRangeAndStatus(startDate, endDate, status)
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Attendance> findByDepartmentAndDateRange(UUID departmentId, LocalDate startDate, LocalDate endDate) {
        return attendanceJpaRepository.findByDepartmentAndDateRange(departmentId, startDate, endDate)
                .stream()
                .map(attendanceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        attendanceJpaRepository.deleteById(id);
    }
}
