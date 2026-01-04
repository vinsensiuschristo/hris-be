package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.infrastructure.persistence.entity.WorkHoursSettingsEntity;
import org.example.hris.infrastructure.persistence.repository.WorkHoursSettingsJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkHoursSettingsService {

    private final WorkHoursSettingsJpaRepository repository;

    // Default values
    private static final LocalTime DEFAULT_JAM_MASUK = LocalTime.of(8, 30);  // 08:30
    private static final LocalTime DEFAULT_JAM_KELUAR = LocalTime.of(17, 45); // 17:45
    private static final int DEFAULT_TOLERANSI_MENIT = 2; // 2 minutes

    /**
     * Get active work hours settings. If not found, create default.
     */
    public WorkHoursSettingsEntity getActiveSettings() {
        return repository.findByIsActiveTrue()
                .orElseGet(this::createDefaultSettings);
    }

    /**
     * Create default settings if none exist
     */
    @Transactional
    public WorkHoursSettingsEntity createDefaultSettings() {
        // First check if any settings exist
        var existing = repository.findByIsActiveTrue();
        if (existing.isPresent()) {
            return existing.get();
        }

        WorkHoursSettingsEntity settings = WorkHoursSettingsEntity.builder()
                .id(UUID.randomUUID())
                .jamMasuk(DEFAULT_JAM_MASUK)
                .jamKeluar(DEFAULT_JAM_KELUAR)
                .toleransiMenit(DEFAULT_TOLERANSI_MENIT)
                .isActive(true)
                .description("Default work hours settings")
                .build();

        return repository.save(settings);
    }

    /**
     * Update work hours settings
     */
    @Transactional
    public WorkHoursSettingsEntity updateSettings(LocalTime jamMasuk, LocalTime jamKeluar, Integer toleransiMenit) {
        WorkHoursSettingsEntity settings = getActiveSettings();

        if (jamMasuk != null) {
            settings.setJamMasuk(jamMasuk);
        }
        if (jamKeluar != null) {
            settings.setJamKeluar(jamKeluar);
        }
        if (toleransiMenit != null) {
            settings.setToleransiMenit(toleransiMenit);
        }

        return repository.save(settings);
    }

    /**
     * Get configured jam masuk (work start time)
     */
    public LocalTime getJamMasuk() {
        return getActiveSettings().getJamMasuk();
    }

    /**
     * Get configured jam keluar (work end time)
     */
    public LocalTime getJamKeluar() {
        return getActiveSettings().getJamKeluar();
    }

    /**
     * Get configured tolerance in minutes
     */
    public int getToleransiMenit() {
        return getActiveSettings().getToleransiMenit();
    }
}
