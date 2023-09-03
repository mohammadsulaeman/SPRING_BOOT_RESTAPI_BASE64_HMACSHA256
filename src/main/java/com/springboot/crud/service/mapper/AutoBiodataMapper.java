package com.springboot.crud.service.mapper;

import com.springboot.crud.service.dto.BiodataDto;
import com.springboot.crud.service.entity.Biodata;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AutoBiodataMapper {
    AutoBiodataMapper MAPPER = Mappers.getMapper(AutoBiodataMapper.class);
    BiodataDto mapToBiodataDto(Biodata biodata);
    Biodata mapToBiodata(BiodataDto biodataDto);
}
