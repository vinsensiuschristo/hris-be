package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String nik;

    @Column(nullable = false)
    private String nama;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jabatan_id")
    private PositionEntity jabatan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departemen_id")
    private DepartmentEntity departemen;

    @Column(unique = true, nullable = false)
    private String email;

    private Integer sisaCuti;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

}
