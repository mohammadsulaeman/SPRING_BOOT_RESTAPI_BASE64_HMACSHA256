package com.springboot.crud.service.service;

import com.springboot.crud.service.dto.BiodataDto;

import java.util.List;

public interface BiodataService {

    List<BiodataDto> fetchAllBiodata();

    BiodataDto fetchBiodataById(String nim);
    BiodataDto saveBiodata(BiodataDto biodataDto);

    BiodataDto updateBiodata(BiodataDto biodataDto);
    void deleteBiodata(Long id);
}
