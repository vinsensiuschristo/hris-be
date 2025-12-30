package org.example.hris.domain.repository;

import org.example.hris.domain.model.OvertimeRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OvertimeRequestRepository {
    OvertimeRequest save(OvertimeRequest overtimeRequest);

    List<OvertimeRequest> findAll();

    Optional<OvertimeRequest> findById(UUID id);

    List<OvertimeRequest> findByKaryawanId(UUID karyawanId);

    List<OvertimeRequest> findByStatusNamaStatus(String status);

    void deleteById(UUID id);
}
