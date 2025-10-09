package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karyawan_id")
    private EmployeeEntity karyawan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jenis_cuti_id")
    private LeaveTypeEntity jenisCuti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private RequestStatusEntity status;

    private LocalDate tglMulai;
    private LocalDate tglSelesai;

    @Column(columnDefinition = "text")
    private String alasan;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
