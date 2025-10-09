package org.example.hris.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentEntity {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String namaDepartemen;

    @OneToMany(mappedBy = "departemen", fetch = FetchType.LAZY)
    private List<EmployeeEntity> employees;
}
