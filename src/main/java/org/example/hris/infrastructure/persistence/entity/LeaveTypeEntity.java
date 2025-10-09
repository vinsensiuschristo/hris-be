package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "leave_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveTypeEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String namaJenis;

    @OneToMany(mappedBy = "jenisCuti", fetch = FetchType.LAZY)
    private List<LeaveRequestEntity> leaveRequests;
}
