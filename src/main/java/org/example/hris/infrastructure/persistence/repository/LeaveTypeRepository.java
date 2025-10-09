package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, UUID> {
}