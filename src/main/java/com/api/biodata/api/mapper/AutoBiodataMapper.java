package com.api.biodata.api.mapper;


import com.api.biodata.api.dto.BiodataDto;
import com.api.biodata.api.entity.Biodata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AutoBiodataMapper {
    AutoBiodataMapper MAPPER = Mappers.getMapper(AutoBiodataMapper.class);

    BiodataDto mapToBiodataDto(Biodata biodata);
    @Mapping(target = "id", ignore = true)
    Biodata mapToBiodata(BiodataDto biodataDto);
}