package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.OvertimeEvidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OvertimeEvidenceRepository extends JpaRepository<OvertimeEvidenceEntity, UUID> {
    List<OvertimeEvidenceEntity> findByOvertimeRequest_Id(UUID overtimeRequestId);
}