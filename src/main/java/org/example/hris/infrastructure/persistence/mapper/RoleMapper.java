package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Role;
import org.example.hris.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toDomain(RoleEntity entity);

    RoleEntity toEntity(Role domain);
}