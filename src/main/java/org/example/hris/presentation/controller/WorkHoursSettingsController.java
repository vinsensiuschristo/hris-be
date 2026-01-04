package org.example.hris.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.settings.WorkHoursSettingsResponse;
import org.example.hris.application.dto.settings.WorkHoursSettingsUpdateRequest;
import org.example.hris.application.service.WorkHoursSettingsService;
import org.example.hris.infrastructure.persistence.entity.WorkHoursSettingsEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "System settings management APIs")
public class WorkHoursSettingsController {

    private final WorkHoursSettingsService workHoursSettingsService;

    @GetMapping("/work-hours")
    @Operation(summary = "Get current work hours settings")
    public ResponseEntity<WorkHoursSettingsResponse> getWorkHoursSettings() {
        WorkHoursSettingsEntity settings = workHoursSettingsService.getActiveSettings();
        return ResponseEntity.ok(toResponse(settings));
    }

    @PutMapping("/work-hours")
    @Operation(summary = "Update work hours settings")
    public ResponseEntity<WorkHoursSettingsResponse> updateWorkHoursSettings(
            @RequestBody WorkHoursSettingsUpdateRequest request
    ) {
        WorkHoursSettingsEntity settings = workHoursSettingsService.updateSettings(
                request.getJamMasuk(),
                request.getJamKeluar(),
                request.getToleransiMenit()
        );
        return ResponseEntity.ok(toResponse(settings));
    }

    private WorkHoursSettingsResponse toResponse(WorkHoursSettingsEntity entity) {
        return WorkHoursSettingsResponse.builder()
                .id(entity.getId())
                .jamMasuk(entity.getJamMasuk())
                .jamKeluar(entity.getJamKeluar())
                .toleransiMenit(entity.getToleransiMenit())
                .isActive(entity.getIsActive())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
