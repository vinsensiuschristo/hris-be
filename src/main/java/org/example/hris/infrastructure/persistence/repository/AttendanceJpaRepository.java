package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, UUID> {
    
    List<AttendanceEntity> findByKaryawan_Id(UUID karyawanId);
    
    Optional<AttendanceEntity> findByKaryawan_IdAndTanggal(UUID karyawanId, LocalDate tanggal);
    
    List<AttendanceEntity> findByTanggal(LocalDate tanggal);
    
    List<AttendanceEntity> findByTanggalBetween(LocalDate startDate, LocalDate endDate);
    
    List<AttendanceEntity> findByStatus(String status);
    
    @Query("SELECT a FROM AttendanceEntity a WHERE a.tanggal BETWEEN :startDate AND :endDate AND a.status = :status")
    List<AttendanceEntity> findByDateRangeAndStatus(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate, 
            @Param("status") String status
    );
    
    @Query("SELECT a FROM AttendanceEntity a WHERE a.karyawan.departemen.id = :departmentId AND a.tanggal BETWEEN :startDate AND :endDate")
    List<AttendanceEntity> findByDepartmentAndDateRange(
            @Param("departmentId") UUID departmentId,
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate
    );
}
