package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Department;
import org.example.hris.infrastructure.persistence.entity.DepartmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDomain(DepartmentEntity entity);

    DepartmentEntity toEntity(Department domain);
}