package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.security.model.User;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface UserMapper {
    @Mapping(target = "karyawan", source = "karyawan")
    User toDomain(UserEntity entity);

    @Mapping(target = "karyawan", source = "karyawan")
    UserEntity toEntity(User domain);
}