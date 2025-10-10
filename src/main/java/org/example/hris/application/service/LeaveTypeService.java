package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.LeaveType;
import org.example.hris.domain.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveType create(LeaveType leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    public LeaveType update(UUID id, LeaveType updated) {
        LeaveType existing = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
        existing.setNamaJenis(updated.getNamaJenis());
        return leaveTypeRepository.save(existing);
    }

    public List<LeaveType> findAll() {
        return leaveTypeRepository.findAll();
    }

    public LeaveType findById(UUID id) {
        return leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
    }

    public void delete(UUID id) {
        leaveTypeRepository.deleteById(id);
    }
}