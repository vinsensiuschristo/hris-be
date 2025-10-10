package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.security.model.User;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {EmployeeMapper.class}
)
public interface UserMapper {

    @Mapping(target = "karyawan", source = "karyawan")
    User toDomain(UserEntity entity);

    @InheritInverseConfiguration
    UserEntity toEntity(User domain);
}
