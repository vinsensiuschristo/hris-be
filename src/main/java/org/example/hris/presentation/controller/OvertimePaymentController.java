package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.payment.OvertimePaymentCreateRequest;
import org.example.hris.application.dto.payment.OvertimePaymentResponse;
import org.example.hris.application.service.OvertimePaymentService;
import org.example.hris.domain.model.OvertimePayment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/overtime-payments")
@RequiredArgsConstructor
@Tag(name = "Overtime Payment", description = "Overtime payment processing APIs (Finance)")
public class OvertimePaymentController {

    private final OvertimePaymentService overtimePaymentService;

    @GetMapping
    @Operation(summary = "Get all overtime payments")
    public ResponseEntity<List<OvertimePaymentResponse>> findAll() {
        List<OvertimePaymentResponse> responses = overtimePaymentService.getAllPayments()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get overtime payment by ID")
    public ResponseEntity<OvertimePaymentResponse> findById(@PathVariable UUID id) {
        OvertimePayment payment = overtimePaymentService.getPaymentById(id);
        return ResponseEntity.ok(toResponse(payment));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending payments (BELUM_DIPROSES)")
    public ResponseEntity<List<OvertimePaymentResponse>> findPending() {
        List<OvertimePaymentResponse> responses = overtimePaymentService.getPendingPayments()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/processing")
    @Operation(summary = "Get processing payments (SEDANG_DIPROSES)")
    public ResponseEntity<List<OvertimePaymentResponse>> findProcessing() {
        List<OvertimePaymentResponse> responses = overtimePaymentService.getProcessingPayments()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/completed")
    @Operation(summary = "Get completed payments (SUDAH_DICAIRKAN)")
    public ResponseEntity<List<OvertimePaymentResponse>> findCompleted() {
        List<OvertimePaymentResponse> responses = overtimePaymentService.getCompletedPayments()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Create payment record for approved overtime")
    public ResponseEntity<OvertimePaymentResponse> create(@Valid @RequestBody OvertimePaymentCreateRequest request) {
        OvertimePayment created = overtimePaymentService.createPayment(
                request.getOvertimeRequestId(),
                request.getFinanceId()
        );
        return ResponseEntity.ok(toResponse(created));
    }

    @PutMapping("/{id}/process")
    @Operation(summary = "Start processing payment (Finance)")
    public ResponseEntity<OvertimePaymentResponse> process(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID financeId
    ) {
        OvertimePayment processed = overtimePaymentService.processPayment(id, financeId);
        return ResponseEntity.ok(toResponse(processed));
    }

    @PutMapping("/{id}/paid")
    @Operation(summary = "Mark payment as paid/completed (Finance)")
    public ResponseEntity<OvertimePaymentResponse> markAsPaid(@PathVariable UUID id) {
        OvertimePayment paid = overtimePaymentService.markAsPaid(id);
        return ResponseEntity.ok(toResponse(paid));
    }

    private OvertimePaymentResponse toResponse(OvertimePayment payment) {
        OvertimePaymentResponse.OvertimePaymentResponseBuilder builder = OvertimePaymentResponse.builder()
                .id(payment.getId())
                .tglPembayaran(payment.getTglPembayaran())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt());

        if (payment.getPengajuanLembur() != null) {
            var lembur = payment.getPengajuanLembur();
            OvertimePaymentResponse.OvertimeRequestSummary.OvertimeRequestSummaryBuilder lemburBuilder = 
                    OvertimePaymentResponse.OvertimeRequestSummary.builder()
                            .id(lembur.getId())
                            .tglLembur(lembur.getTglLembur())
                            .durasi(lembur.getDurasi())
                            .estimasiBiaya(lembur.getEstimasiBiaya())
                            .statusLembur(lembur.getStatus() != null ? lembur.getStatus().getNamaStatus() : null);

            if (lembur.getKaryawan() != null) {
                lemburBuilder.karyawan(OvertimePaymentResponse.EmployeeSummary.builder()
                        .id(lembur.getKaryawan().getId())
                        .nama(lembur.getKaryawan().getNama())
                        .nik(lembur.getKaryawan().getNik())
                        .email(lembur.getKaryawan().getEmail())
                        .build());
            }

            builder.pengajuanLembur(lemburBuilder.build());
        }

        if (payment.getFinance() != null) {
            builder.finance(OvertimePaymentResponse.EmployeeSummary.builder()
                    .id(payment.getFinance().getId())
                    .nama(payment.getFinance().getNama())
                    .nik(payment.getFinance().getNik())
                    .email(payment.getFinance().getEmail())
                    .build());
        }

        if (payment.getStatus() != null) {
            builder.status(OvertimePaymentResponse.StatusResponse.builder()
                    .id(payment.getStatus().getId())
                    .namaStatus(payment.getStatus().getNamaStatus())
                    .build());
        }

        return builder.build();
    }
}
