package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.security.model.UserRole;
import org.example.hris.infrastructure.persistence.entity.UserRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "role.id", source = "roleId")
    UserRole toDomain(UserRoleEntity entity);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "roleId", source = "role.id")
    UserRoleEntity toEntity(UserRole domain);
}