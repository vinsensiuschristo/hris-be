package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.OvertimePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OvertimePaymentRepository extends JpaRepository<OvertimePaymentEntity, UUID> {
    List<OvertimePaymentEntity> findByPengajuanLembur_Id(UUID overtimeRequestId);
}