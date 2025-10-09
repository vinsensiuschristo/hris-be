package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionEntity {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String namaJabatan;

    @OneToMany(mappedBy = "jabatan", fetch = FetchType.LAZY)
    private List<EmployeeEntity> employees;
}
