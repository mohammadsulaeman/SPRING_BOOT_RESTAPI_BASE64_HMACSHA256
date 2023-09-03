package com.springboot.crud.service.service.impl;

import com.springboot.crud.service.dto.BiodataDto;
import com.springboot.crud.service.entity.Biodata;
import com.springboot.crud.service.exception.EmailAlreadyExistsException;
import com.springboot.crud.service.exception.ResourceNotFoundException;
import com.springboot.crud.service.mapper.AutoBiodataMapper;
import com.springboot.crud.service.repository.BiodataRepository;
import com.springboot.crud.service.service.BiodataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BiodataImplService implements BiodataService {
    BiodataRepository repository;
    @Override
    public List<BiodataDto> fetchAllBiodata() {
        List<Biodata> biodataList = repository.findAll();
        List<BiodataDto> biodataDtoList = new ArrayList<>();
        biodataList.forEach((biodata)->{
            BiodataDto biodataDto = AutoBiodataMapper.MAPPER.mapToBiodataDto(biodata);
            biodataDtoList.add(biodataDto);
        });
        return biodataDtoList;
    }

    @Override
    public BiodataDto fetchBiodataById(String nim) {
        Biodata optionalBiodata = repository.findByNim(nim).orElseThrow(
                ()-> new ResourceNotFoundException("Biodata List","ID",Long.valueOf(nim))
        );
        return AutoBiodataMapper.MAPPER.mapToBiodataDto(optionalBiodata);
    }

    @Override
    public BiodataDto saveBiodata(BiodataDto biodataDto) {
        Biodata biodata = AutoBiodataMapper.MAPPER.mapToBiodata(biodataDto);
        Optional<Biodata> cekEmail = repository.findByEmail(biodata.getEmail());
        if (cekEmail.isPresent()){
            throw new EmailAlreadyExistsException("Email ALready Exists for User");
        }
        Biodata saveBiodata = repository.save(biodata);
        return AutoBiodataMapper.MAPPER.mapToBiodataDto(saveBiodata);
    }

    @Override
    public BiodataDto updateBiodata(BiodataDto biodataDto) {
        Biodata biodata = repository.findByNim(biodataDto.getNim()).orElseThrow(
                () -> new ResourceNotFoundException("Biodata","Nim", Long.valueOf(biodataDto.getNim()))
        );
        biodata.setPhoto(biodataDto.getPhoto());
        biodata.setFullName(biodataDto.getFullName());
        biodata.setAddress(biodataDto.getAddress());
        biodata.setEmail(biodataDto.getEmail());
        biodata.setAddress(biodataDto.getAddress());
        Biodata updateBiodata = repository.save(biodata);
        return AutoBiodataMapper.MAPPER.mapToBiodataDto(updateBiodata);
    }

    @Override
    public void deleteBiodata(Long id) {
        repository.deleteById(id);
    }
}
