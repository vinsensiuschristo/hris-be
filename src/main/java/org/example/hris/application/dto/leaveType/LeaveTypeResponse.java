package org.example.hris.application.dto.leaveType;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LeaveTypeResponse {
    private UUID id;
    private String namaJenis;
}
