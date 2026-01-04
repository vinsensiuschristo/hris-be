package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.leaveRequest.ApprovalRequest;
import org.example.hris.application.dto.overtimeRequest.OvertimeRequestCreateRequest;
import org.example.hris.application.dto.overtimeRequest.OvertimeRequestResponse;
import org.example.hris.application.service.FileStorageService;
import org.example.hris.application.service.OvertimeRequestService;
import org.example.hris.domain.model.OvertimeRequest;
import org.example.hris.infrastructure.persistence.entity.OvertimeEvidenceEntity;
import org.example.hris.infrastructure.persistence.repository.OvertimeEvidenceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/overtime-requests")
@RequiredArgsConstructor
@Tag(name = "Overtime Request", description = "Overtime request management and approval APIs")
public class OvertimeRequestController {

    private final OvertimeRequestService overtimeRequestService;
    private final OvertimeEvidenceRepository overtimeEvidenceRepository;
    private final FileStorageService fileStorageService;

    @GetMapping
    @Operation(summary = "Get all overtime requests")
    public ResponseEntity<List<OvertimeRequestResponse>> findAll() {
        List<OvertimeRequestResponse> responses = overtimeRequestService.getAllOvertimeRequests()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get overtime request by ID")
    public ResponseEntity<OvertimeRequestResponse> findById(@PathVariable UUID id) {
        OvertimeRequest overtimeRequest = overtimeRequestService.getOvertimeRequestById(id);
        return ResponseEntity.ok(toResponse(overtimeRequest));
    }

    @GetMapping("/karyawan/{karyawanId}")
    @Operation(summary = "Get overtime requests by employee ID")
    public ResponseEntity<List<OvertimeRequestResponse>> findByKaryawanId(@PathVariable UUID karyawanId) {
        List<OvertimeRequestResponse> responses = overtimeRequestService.getOvertimeRequestsByKaryawanId(karyawanId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending overtime requests (for approval)")
    public ResponseEntity<List<OvertimeRequestResponse>> findPending() {
        List<OvertimeRequestResponse> responses = overtimeRequestService.getPendingOvertimeRequests()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/approved")
    @Operation(summary = "Get all approved overtime requests (for payment processing)")
    public ResponseEntity<List<OvertimeRequestResponse>> findApproved() {
        List<OvertimeRequestResponse> responses = overtimeRequestService.getApprovedOvertimeRequests()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Submit new overtime request")
    public ResponseEntity<OvertimeRequestResponse> create(@Valid @RequestBody OvertimeRequestCreateRequest request) {
        OvertimeRequest created = overtimeRequestService.createOvertimeRequest(
                request.getKaryawanId(),
                request.getTglLembur(),
                request.getJamMulai(),
                request.getJamSelesai()
        );
        return ResponseEntity.ok(toResponse(created));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve overtime request (Supervisor/HR)")
    public ResponseEntity<OvertimeRequestResponse> approve(
            @PathVariable UUID id,
            @RequestBody(required = false) ApprovalRequest request
    ) {
        OvertimeRequest approved = overtimeRequestService.approveOvertimeRequest(id);
        return ResponseEntity.ok(toResponse(approved));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject overtime request (Supervisor/HR)")
    public ResponseEntity<OvertimeRequestResponse> reject(
            @PathVariable UUID id,
            @RequestBody(required = false) ApprovalRequest request
    ) {
        OvertimeRequest rejected = overtimeRequestService.rejectOvertimeRequest(id);
        return ResponseEntity.ok(toResponse(rejected));
    }

    private OvertimeRequestResponse toResponse(OvertimeRequest overtimeRequest) {
        // Fetch evidences for this overtime request and generate signed URLs
        List<OvertimeEvidenceEntity> evidenceEntities = overtimeEvidenceRepository.findByOvertimeRequest_Id(overtimeRequest.getId());
        List<OvertimeRequestResponse.EvidenceSummary> evidences = evidenceEntities.stream()
                .map(e -> OvertimeRequestResponse.EvidenceSummary.builder()
                        .id(e.getId())
                        .filePath(fileStorageService.getSignedUrl(e.getFilePath()))  // Generate signed URL
                        .fileType(e.getFileType())
                        .uploadedAt(e.getUploadedAt() != null ? e.getUploadedAt().atZone(ZoneId.systemDefault()).toLocalDateTime() : null)
                        .build())
                .toList();

        OvertimeRequestResponse.OvertimeRequestResponseBuilder builder = OvertimeRequestResponse.builder()
                .id(overtimeRequest.getId())
                .tglLembur(overtimeRequest.getTglLembur())
                .jamMulai(overtimeRequest.getJamMulai())
                .jamSelesai(overtimeRequest.getJamSelesai())
                .durasi(overtimeRequest.getDurasi())
                .estimasiBiaya(overtimeRequest.getEstimasiBiaya())
                .createdAt(overtimeRequest.getCreatedAt())
                .updatedAt(overtimeRequest.getUpdatedAt())
                .evidences(evidences);

        if (overtimeRequest.getKaryawan() != null) {
            builder.karyawan(OvertimeRequestResponse.EmployeeSummary.builder()
                    .id(overtimeRequest.getKaryawan().getId())
                    .nama(overtimeRequest.getKaryawan().getNama())
                    .nik(overtimeRequest.getKaryawan().getNik())
                    .email(overtimeRequest.getKaryawan().getEmail())
                    .build());
        }

        if (overtimeRequest.getStatus() != null) {
            builder.status(OvertimeRequestResponse.StatusResponse.builder()
                    .id(overtimeRequest.getStatus().getId())
                    .namaStatus(overtimeRequest.getStatus().getNamaStatus())
                    .build());
        }

        return builder.build();
    }
}
