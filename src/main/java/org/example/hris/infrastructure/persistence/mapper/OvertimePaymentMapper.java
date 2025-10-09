package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.overtime.model.OvertimePayment;
import org.example.hris.infrastructure.persistence.entity.OvertimePaymentEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface OvertimePaymentMapper {
    @Mapping(target = "pengajuanLembur", source = "pengajuanLembur")
    @Mapping(target = "finance", source = "finance")
    @Mapping(target = "status", source = "status")
    OvertimePayment toDomain(OvertimePaymentEntity entity);

    @Mapping(target = "pengajuanLembur", source = "pengajuanLembur")
    @Mapping(target = "finance", source = "finance")
    @Mapping(target = "status", source = "status")
    OvertimePaymentEntity toEntity(OvertimePayment domain);
}