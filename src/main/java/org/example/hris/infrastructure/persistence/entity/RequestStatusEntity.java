package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "request_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestStatusEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String namaStatus;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<LeaveRequestEntity> leaveRequests;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<OvertimeRequestEntity> overtimeRequests;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<OvertimePaymentEntity> overtimePayments;
}
