package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Department;
import org.example.hris.infrastructure.persistence.entity.DepartmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(source = "namaDepartemen", target = "namaDepartement")
    Department toDomain(DepartmentEntity entity);

    @Mapping(source = "namaDepartement", target = "namaDepartemen")
    DepartmentEntity toEntity(Department domain);
}