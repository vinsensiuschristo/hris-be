package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.security.model.Role;
import org.example.hris.domain.security.model.User;
import org.example.hris.domain.security.model.UserRole;
import org.example.hris.infrastructure.persistence.entity.UserRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, RoleMapper.class}
)
public interface UserRoleMapper {

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "role", source = "roleId")
    UserRole toDomain(UserRoleEntity entity);

    @Mapping(target = "userId", source = "user")
    @Mapping(target = "roleId", source = "role")
    UserRoleEntity toEntity(UserRole domain);

    default User map(UUID userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    // UUID → Role
    default Role mapRole(UUID roleId) {
        if (roleId == null) return null;
        Role role = new Role();
        role.setId(roleId);
        return role;
    }

    // User → UUID
    default UUID map(User user) {
        return (user != null) ? user.getId() : null;
    }

    // Role → UUID
    default UUID map(Role role) {
        return (role != null) ? role.getId() : null;
    }
}
