package org.example.hris.domain.repository;

import org.example.hris.domain.model.LeaveRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaveRequestRepository {
    LeaveRequest save(LeaveRequest leaveRequest);

    List<LeaveRequest> findAll();

    Optional<LeaveRequest> findById(UUID id);

    List<LeaveRequest> findByKaryawanId(UUID karyawanId);

    List<LeaveRequest> findByStatusNamaStatus(String status);

    void deleteById(UUID id);
}
