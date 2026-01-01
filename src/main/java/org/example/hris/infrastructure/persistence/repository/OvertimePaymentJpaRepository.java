package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.OvertimePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("overtimePaymentJpaRepository")
public interface OvertimePaymentJpaRepository extends JpaRepository<OvertimePaymentEntity, UUID> {
    List<OvertimePaymentEntity> findByPengajuanLembur_Id(UUID overtimeRequestId);
}
