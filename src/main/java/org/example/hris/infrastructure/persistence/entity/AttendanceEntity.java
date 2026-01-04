package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karyawan_id")
    private EmployeeEntity karyawan;

    private LocalDate tanggal;
    private LocalTime jamMasuk;
    private LocalTime jamKeluar;

    @Column(nullable = false)
    private String status; // HADIR, TERLAMBAT, IZIN, SAKIT, ALPHA

    @Builder.Default
    private Integer keterlambatanMenit = 0;

    @Column(columnDefinition = "text")
    private String keterangan;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}

