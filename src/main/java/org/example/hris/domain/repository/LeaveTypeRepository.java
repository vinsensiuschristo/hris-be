package org.example.hris.domain.repository;

import org.example.hris.domain.model.LeaveType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaveTypeRepository {
    LeaveType save(LeaveType leaveType);

    Optional<LeaveType> findById(UUID id);

    List<LeaveType> findAll();

    void deleteById(UUID id);

    Optional<LeaveType> findByNamaJenis(String namaJenis);
}