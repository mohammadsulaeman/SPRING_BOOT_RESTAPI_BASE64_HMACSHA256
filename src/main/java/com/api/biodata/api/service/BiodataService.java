package com.api.biodata.api.service;

import com.api.biodata.api.dto.BiodataDto;

import java.util.List;

public interface BiodataService {
    byte[] calcHmacSha256(byte[] secretKey, byte[] message);
    List<BiodataDto> fetchAllBiodata();

    BiodataDto fetchBiodataById(String nim);
    BiodataDto saveBiodata(BiodataDto biodataDto);

    BiodataDto updateBiodata(BiodataDto biodataDto);
    void deleteBiodata(String id);
    String urlImagesPerson(String nim);
    boolean cekToken(String fullName,String token);
    List<BiodataDto> searchDataByKewords(String keyword);
}