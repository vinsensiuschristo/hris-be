package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "overtime_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OvertimeRequestEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karyawan_id")
    private EmployeeEntity karyawan;

    private LocalDate tglLembur;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private Integer durasi;

    @Column(name = "estimasi_biaya", precision = 15, scale = 2)
    private BigDecimal estimasiBiaya;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private RequestStatusEntity status;

    @Column(name = "alasan_penolakan", columnDefinition = "text")
    private String alasanPenolakan;

    @OneToMany(mappedBy = "overtimeRequest", fetch = FetchType.LAZY)
    private List<OvertimeEvidenceEntity> evidences;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
