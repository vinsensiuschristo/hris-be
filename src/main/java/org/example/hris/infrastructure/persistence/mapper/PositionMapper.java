package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Position;
import org.example.hris.infrastructure.persistence.entity.PositionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    Position toDomain(PositionEntity entity);

    PositionEntity toEntity(Position domain);
}