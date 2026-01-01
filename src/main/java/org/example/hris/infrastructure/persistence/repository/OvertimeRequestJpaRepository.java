package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.OvertimeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("overtimeRequestJpaRepository")
public interface OvertimeRequestJpaRepository extends JpaRepository<OvertimeRequestEntity, UUID> {
    List<OvertimeRequestEntity> findByKaryawan_Id(UUID karyawanId);
}
