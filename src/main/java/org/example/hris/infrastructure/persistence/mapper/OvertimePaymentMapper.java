package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.OvertimePayment;
import org.example.hris.infrastructure.persistence.entity.OvertimePaymentEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {OvertimeRequestMapper.class, EmployeeMapper.class, RequestStatusMapper.class, DateTimeMapper.class}
)
public interface OvertimePaymentMapper {

    @Mapping(target = "pengajuanLembur", source = "pengajuanLembur")
    @Mapping(target = "finance", source = "finance")
    @Mapping(target = "status", source = "status")
    OvertimePayment toDomain(OvertimePaymentEntity entity);

    @InheritInverseConfiguration
    OvertimePaymentEntity toEntity(OvertimePayment domain);
}
