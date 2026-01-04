package org.example.hris.application.dto.leaveRequest;

import lombok.Data;

@Data
public class ApprovalRequest {
    private String komentar;
    private String reason;  // Used for rejection reason
}

