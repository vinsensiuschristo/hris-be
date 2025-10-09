package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, UUID> {
    List<LeaveRequestEntity> findByKaryawan_Id(UUID karyawanId);
}
