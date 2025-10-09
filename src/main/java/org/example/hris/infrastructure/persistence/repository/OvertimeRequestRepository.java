package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.OvertimeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequestEntity, UUID> {
    List<OvertimeRequestEntity> findByKaryawan_Id(UUID karyawanId);
}