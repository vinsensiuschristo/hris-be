package org.example.hris.domain.overtime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeEvidence {
    private UUID id;
    private OvertimeRequest overtimeRequest;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadedAt;
}
