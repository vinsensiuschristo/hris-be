package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "work_hours_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkHoursSettingsEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private LocalTime jamMasuk;  // 08:30

    @Column(nullable = false)
    private LocalTime jamKeluar; // 17:45

    @Column(nullable = false)
    @Builder.Default
    private Integer toleransiMenit = 2; // 2 minutes grace period

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
