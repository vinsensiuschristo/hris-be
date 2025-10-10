package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.leaveType.LeaveTypeCreateRequest;
import org.example.hris.application.dto.leaveType.LeaveTypeResponse;
import org.example.hris.application.dto.leaveType.LeaveTypeUpdateRequest;
import org.example.hris.application.service.LeaveTypeService;
import org.example.hris.domain.model.LeaveType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public ResponseEntity<LeaveTypeResponse> create(@Valid @RequestBody LeaveTypeCreateRequest request) {
        LeaveType leaveType = LeaveType.builder()
                .namaJenis(request.getNamaJenis())
                .build();

        LeaveType created = leaveTypeService.create(leaveType);

        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<LeaveTypeResponse>> findAll() {
        List<LeaveTypeResponse> responses = leaveTypeService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveTypeResponse> findById(@PathVariable UUID id) {
        LeaveType leaveType = leaveTypeService.findById(id);
        return ResponseEntity.ok(toResponse(leaveType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveTypeResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveTypeUpdateRequest request
    ) {
        LeaveType updated = LeaveType.builder()
                .namaJenis(request.getNamaJenis())
                .build();

        LeaveType saved = leaveTypeService.update(id, updated);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        leaveTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private LeaveTypeResponse toResponse(LeaveType leaveType) {
        return LeaveTypeResponse.builder()
                .id(leaveType.getId())
                .namaJenis(leaveType.getNamaJenis())
                .build();
    }
}