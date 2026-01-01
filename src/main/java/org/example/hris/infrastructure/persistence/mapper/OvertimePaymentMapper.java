package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.OvertimePayment;
import org.example.hris.infrastructure.persistence.entity.OvertimePaymentEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface OvertimePaymentMapper {

    @Mapping(target = "pengajuanLembur.id", source = "pengajuanLembur.id")
    @Mapping(target = "pengajuanLembur.tglLembur", source = "pengajuanLembur.tglLembur")
    @Mapping(target = "pengajuanLembur.jamMulai", source = "pengajuanLembur.jamMulai")
    @Mapping(target = "pengajuanLembur.jamSelesai", source = "pengajuanLembur.jamSelesai")
    @Mapping(target = "pengajuanLembur.durasi", source = "pengajuanLembur.durasi")
    @Mapping(target = "pengajuanLembur.estimasiBiaya", source = "pengajuanLembur.estimasiBiaya")
    @Mapping(target = "pengajuanLembur.karyawan", ignore = true)
    @Mapping(target = "pengajuanLembur.status", ignore = true)
    @Mapping(target = "finance.id", source = "finance.id")
    @Mapping(target = "finance.nama", source = "finance.nama")
    @Mapping(target = "finance.nik", source = "finance.nik")
    @Mapping(target = "finance.jabatan", ignore = true)
    @Mapping(target = "finance.departemen", ignore = true)
    @Mapping(target = "status.id", source = "status.id")
    @Mapping(target = "status.namaStatus", source = "status.namaStatus")
    OvertimePayment toDomain(OvertimePaymentEntity entity);

    @Mapping(target = "pengajuanLembur", ignore = true)
    @Mapping(target = "finance", ignore = true)
    @Mapping(target = "status", ignore = true)
    OvertimePaymentEntity toEntity(OvertimePayment domain);
}
