package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.infrastructure.persistence.entity.LeaveEvidenceEntity;
import org.example.hris.infrastructure.persistence.entity.LeaveRequestEntity;
import org.example.hris.infrastructure.persistence.entity.OvertimeEvidenceEntity;
import org.example.hris.infrastructure.persistence.entity.OvertimeRequestEntity;
import org.example.hris.infrastructure.persistence.repository.LeaveEvidenceRepository;
import org.example.hris.infrastructure.persistence.repository.LeaveRequestJpaRepository;
import org.example.hris.infrastructure.persistence.repository.OvertimeEvidenceRepository;
import org.example.hris.infrastructure.persistence.repository.OvertimeRequestJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final OvertimeEvidenceRepository overtimeEvidenceRepository;
    private final OvertimeRequestJpaRepository overtimeRequestJpaRepository;
    private final LeaveEvidenceRepository leaveEvidenceRepository;
    private final LeaveRequestJpaRepository leaveRequestJpaRepository;
    private final RestTemplate restTemplate;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket:evidences}")
    private String bucketName;

    private static final long MAX_IMAGE_SIZE = 3 * 1024 * 1024; // 3MB
    private static final long MAX_PDF_SIZE = 5 * 1024 * 1024;   // 5MB for PDF
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
    private static final List<String> ALLOWED_LEAVE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp", "application/pdf");

    // Default expiry time for signed URLs (24 hours in seconds)
    private static final int SIGNED_URL_EXPIRY_SECONDS = 86400;

    /**
     * Upload evidence for overtime request - only images allowed, max 3MB
     */
    public String uploadOvertimeEvidence(UUID overtimeRequestId, MultipartFile file) throws IOException {
        // Validate file type - only images allowed
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("File harus berupa gambar (JPG, PNG, WEBP). Tipe: " + contentType);
        }

        // Validate file size - max 3MB
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Ukuran file maksimal 3MB. Ukuran file: " + (file.getSize() / 1024 / 1024) + "MB");
        }

        // Find overtime request
        OvertimeRequestEntity overtimeRequest = overtimeRequestJpaRepository.findById(overtimeRequestId)
                .orElseThrow(() -> new RuntimeException("Overtime request not found: " + overtimeRequestId));

        // Upload to Supabase Storage - returns relative path
        String relativePath = generateFileName("overtime", overtimeRequestId, getFileExtension(file.getOriginalFilename()));
        uploadToSupabase(file.getBytes(), relativePath, contentType);

        // Save evidence record with relative path (not full URL)
        OvertimeEvidenceEntity evidence = OvertimeEvidenceEntity.builder()
                .overtimeRequest(overtimeRequest)
                .filePath(relativePath)  // Store relative path only
                .fileType(contentType)
                .uploadedAt(Instant.now())
                .build();
        overtimeEvidenceRepository.save(evidence);

        return relativePath;
    }

    /**
     * Upload evidence for leave request (sick leave) - images and PDF allowed
     */
    public String uploadLeaveEvidence(UUID leaveRequestId, MultipartFile file) throws IOException {
        // Validate file type - images and PDF allowed
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_LEAVE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("File harus berupa gambar (JPG, PNG, WEBP) atau PDF. Tipe: " + contentType);
        }

        // Validate file size
        long maxSize = contentType.equals("application/pdf") ? MAX_PDF_SIZE : MAX_IMAGE_SIZE;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Ukuran file maksimal " + (maxSize / 1024 / 1024) + "MB. Ukuran file: " + (file.getSize() / 1024 / 1024) + "MB");
        }

        // Find leave request
        LeaveRequestEntity leaveRequest = leaveRequestJpaRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found: " + leaveRequestId));

        // Upload to Supabase Storage - returns relative path
        String relativePath = generateFileName("leave", leaveRequestId, getFileExtension(file.getOriginalFilename()));
        uploadToSupabase(file.getBytes(), relativePath, contentType);

        // Save evidence record with relative path (not full URL)
        LeaveEvidenceEntity evidence = LeaveEvidenceEntity.builder()
                .leaveRequest(leaveRequest)
                .filePath(relativePath)  // Store relative path only
                .fileType(contentType)
                .uploadedAt(Instant.now())
                .build();
        leaveEvidenceRepository.save(evidence);

        return relativePath;
    }

    /**
     * Generate a signed URL for accessing a private file
     * @param relativePath The relative path of the file in the bucket (e.g., "leave/uuid/file.png")
     * @return The signed URL that can be used to access the file
     */
    public String getSignedUrl(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }

        // If it's already a full URL (legacy data), extract the relative path
        if (relativePath.startsWith("http")) {
            // Extract relative path from full URL
            // Example: https://xxx.supabase.co/storage/v1/object/public/hris-bucket/leave/xxx/yyy.png
            // -> leave/xxx/yyy.png
            String marker = bucketName + "/";
            int index = relativePath.indexOf(marker);
            if (index != -1) {
                relativePath = relativePath.substring(index + marker.length());
            } else {
                // Can't extract, return as-is
                return relativePath;
            }
        }

        String signUrl = supabaseUrl + "/storage/v1/object/sign/" + bucketName + "/" + relativePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request body with expiry time
        Map<String, Object> body = Map.of("expiresIn", SIGNED_URL_EXPIRY_SECONDS);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    signUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String signedURL = (String) response.getBody().get("signedURL");
                if (signedURL != null) {
                    // Supabase returns relative path starting with /object/sign/...
                    // We need to prepend the base URL with /storage/v1
                    return supabaseUrl + "/storage/v1" + signedURL;
                }
            }
            
            // Fallback to public URL if signing fails
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + relativePath;
        } catch (Exception e) {
            // Log error and return public URL as fallback
            System.err.println("Error generating signed URL: " + e.getMessage());
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + relativePath;
        }
    }

    private void uploadToSupabase(byte[] fileBytes, String fileName, String contentType) {
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.parseMediaType(contentType));

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to upload file to Supabase: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Supabase: " + e.getMessage(), e);
        }
    }

    private String generateFileName(String prefix, UUID requestId, String extension) {
        return prefix + "/" + requestId + "/" + UUID.randomUUID() + extension;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
