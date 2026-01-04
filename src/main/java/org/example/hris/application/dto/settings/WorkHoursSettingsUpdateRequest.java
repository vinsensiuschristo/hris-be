package org.example.hris.application.dto.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkHoursSettingsUpdateRequest {
    private LocalTime jamMasuk;
    private LocalTime jamKeluar;
    private Integer toleransiMenit;
}
