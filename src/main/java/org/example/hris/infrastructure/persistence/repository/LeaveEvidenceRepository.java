package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.LeaveEvidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LeaveEvidenceRepository extends JpaRepository<LeaveEvidenceEntity, UUID> {
    List<LeaveEvidenceEntity> findByLeaveRequest_Id(UUID leaveRequestId);
}
