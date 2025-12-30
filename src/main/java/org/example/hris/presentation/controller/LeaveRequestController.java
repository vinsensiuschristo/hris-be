package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.leaveRequest.ApprovalRequest;
import org.example.hris.application.dto.leaveRequest.LeaveRequestCreateRequest;
import org.example.hris.application.dto.leaveRequest.LeaveRequestResponse;
import org.example.hris.application.dto.leaveType.LeaveTypeResponse;
import org.example.hris.application.service.LeaveRequestService;
import org.example.hris.domain.model.LeaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Request", description = "Leave request management and approval APIs")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping
    @Operation(summary = "Get all leave requests")
    public ResponseEntity<List<LeaveRequestResponse>> findAll() {
        List<LeaveRequestResponse> responses = leaveRequestService.getAllLeaveRequests()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave request by ID")
    public ResponseEntity<LeaveRequestResponse> findById(@PathVariable UUID id) {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(id);
        return ResponseEntity.ok(toResponse(leaveRequest));
    }

    @GetMapping("/karyawan/{karyawanId}")
    @Operation(summary = "Get leave requests by employee ID")
    public ResponseEntity<List<LeaveRequestResponse>> findByKaryawanId(@PathVariable UUID karyawanId) {
        List<LeaveRequestResponse> responses = leaveRequestService.getLeaveRequestsByKaryawanId(karyawanId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending leave requests (for approval)")
    public ResponseEntity<List<LeaveRequestResponse>> findPending() {
        List<LeaveRequestResponse> responses = leaveRequestService.getPendingLeaveRequests()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Submit new leave request")
    public ResponseEntity<LeaveRequestResponse> create(@Valid @RequestBody LeaveRequestCreateRequest request) {
        LeaveRequest created = leaveRequestService.createLeaveRequest(
                request.getKaryawanId(),
                request.getJenisCutiId(),
                request.getTglMulai(),
                request.getTglSelesai(),
                request.getAlasan()
        );
        return ResponseEntity.ok(toResponse(created));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve leave request (Supervisor/HR)")
    public ResponseEntity<LeaveRequestResponse> approve(
            @PathVariable UUID id,
            @RequestBody(required = false) ApprovalRequest request
    ) {
        LeaveRequest approved = leaveRequestService.approveLeaveRequest(id);
        return ResponseEntity.ok(toResponse(approved));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject leave request (Supervisor/HR)")
    public ResponseEntity<LeaveRequestResponse> reject(
            @PathVariable UUID id,
            @RequestBody(required = false) ApprovalRequest request
    ) {
        LeaveRequest rejected = leaveRequestService.rejectLeaveRequest(id);
        return ResponseEntity.ok(toResponse(rejected));
    }

    private LeaveRequestResponse toResponse(LeaveRequest leaveRequest) {
        long jumlahHari = ChronoUnit.DAYS.between(leaveRequest.getTglMulai(), leaveRequest.getTglSelesai()) + 1;

        LeaveRequestResponse.LeaveRequestResponseBuilder builder = LeaveRequestResponse.builder()
                .id(leaveRequest.getId())
                .tglMulai(leaveRequest.getTglMulai())
                .tglSelesai(leaveRequest.getTglSelesai())
                .alasan(leaveRequest.getAlasan())
                .jumlahHari(jumlahHari)
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt());

        if (leaveRequest.getKaryawan() != null) {
            builder.karyawan(LeaveRequestResponse.EmployeeSummary.builder()
                    .id(leaveRequest.getKaryawan().getId())
                    .nama(leaveRequest.getKaryawan().getNama())
                    .nik(leaveRequest.getKaryawan().getNik())
                    .email(leaveRequest.getKaryawan().getEmail())
                    .sisaCuti(leaveRequest.getKaryawan().getSisaCuti())
                    .build());
        }

        if (leaveRequest.getJenisCuti() != null) {
            builder.jenisCuti(LeaveTypeResponse.builder()
                    .id(leaveRequest.getJenisCuti().getId())
                    .namaJenis(leaveRequest.getJenisCuti().getNamaJenis())
                    .build());
        }

        if (leaveRequest.getStatus() != null) {
            builder.status(LeaveRequestResponse.StatusResponse.builder()
                    .id(leaveRequest.getStatus().getId())
                    .namaStatus(leaveRequest.getStatus().getNamaStatus())
                    .build());
        }

        return builder.build();
    }
}
