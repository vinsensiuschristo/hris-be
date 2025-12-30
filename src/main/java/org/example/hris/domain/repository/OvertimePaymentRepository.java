package org.example.hris.domain.repository;

import org.example.hris.domain.model.OvertimePayment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OvertimePaymentRepository {
    OvertimePayment save(OvertimePayment overtimePayment);

    List<OvertimePayment> findAll();

    Optional<OvertimePayment> findById(UUID id);

    Optional<OvertimePayment> findByPengajuanLemburId(UUID pengajuanLemburId);

    List<OvertimePayment> findByStatusNamaStatus(String status);

    void deleteById(UUID id);
}
