package org.example.hris.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "File upload APIs for evidence attachments")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/overtime/{overtimeRequestId}/evidence", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload evidence for overtime request (image only, max 3MB)")
    public ResponseEntity<?> uploadOvertimeEvidence(
            @PathVariable UUID overtimeRequestId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String filePath = fileStorageService.uploadOvertimeEvidence(overtimeRequestId, file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "filePath", filePath,
                    "message", "File berhasil diupload"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", "Gagal membaca file: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", "Gagal mengupload file: " + e.getMessage()
            ));
        }
    }

    @PostMapping(value = "/leave/{leaveRequestId}/evidence", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload evidence for leave request (image or PDF, max 3-5MB)")
    public ResponseEntity<?> uploadLeaveEvidence(
            @PathVariable UUID leaveRequestId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String filePath = fileStorageService.uploadLeaveEvidence(leaveRequestId, file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "filePath", filePath,
                    "message", "File berhasil diupload"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", "Gagal membaca file: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", "Gagal mengupload file: " + e.getMessage()
            ));
        }
    }
}
