package com.api.biodata.api.service.impl;

import com.api.biodata.api.dto.BiodataDto;
import com.api.biodata.api.entity.Biodata;
import com.api.biodata.api.exception.EmailAlreadyExistsException;
import com.api.biodata.api.exception.ResourceNotFoundException;
import com.api.biodata.api.mapper.AutoBiodataMapper;
import com.api.biodata.api.repository.BiodataRepository;
import com.api.biodata.api.service.BiodataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
@Transactional
public class BiodataServiceImpl implements BiodataService
{
    @Value("${file.upload-dir}")
    private String uploadPathFile;

    @Value("${file.url.upload-dir}")
    private String urlUploadPath;

    private final BiodataRepository repository;


    @Override
    public byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BiodataDto> fetchAllBiodata() {
        List<Biodata> biodataList = repository.findAll();
        return biodataList.stream().map(AutoBiodataMapper.MAPPER::mapToBiodataDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BiodataDto fetchBiodataById(String nim) {
        Biodata biodata = repository.findByNim(nim).orElseThrow(
                ()-> new ResourceNotFoundException("Biodata List","ID",Long.valueOf(nim))
        );
        return AutoBiodataMapper.MAPPER.mapToBiodataDto(biodata);
    }

    @Override
    public BiodataDto saveBiodata(BiodataDto biodataDto) {
        Biodata biodata = AutoBiodataMapper.MAPPER.mapToBiodata(biodataDto);
        String urlImages = urlImage(biodataDto);
        biodata.setPhoto(urlImages);
        Optional<Biodata> cekEmail = repository.findByEmail(biodata.getEmail());
        if (cekEmail.isPresent()){
            throw new EmailAlreadyExistsException("Email ALready Exists for User");
        }
        Optional<Biodata> cekNim = repository.findByNim(biodata.getNim());
        if (cekNim.isPresent()){
            throw new ResourceNotFoundException("Biodata List","ID",Long.valueOf(biodata.getNim()));
        }
        Biodata saveBiodata = repository.saveAndFlush(biodata);
        return AutoBiodataMapper.MAPPER.mapToBiodataDto(saveBiodata);
    }

    @Override
    public BiodataDto updateBiodata(BiodataDto biodataDto) {
        Biodata biodata = AutoBiodataMapper.MAPPER.mapToBiodata(biodataDto);
        String urlImages = urlImage(biodataDto);
        Biodata cekBiodata = repository.findByNim(biodata.getNim()).orElseThrow(
                ()-> new ResourceNotFoundException("Biodata","Nim",Long.valueOf(biodata.getNim()))
        );
        cekBiodata.setPhoto(urlImages);
        cekBiodata.setFullName(biodata.getFullName());
        cekBiodata.setAddress(biodata.getAddress());
        cekBiodata.setEmail(biodata.getEmail());
        Biodata updateBiodata = repository.saveAndFlush(cekBiodata);
        return AutoBiodataMapper.MAPPER.mapToBiodataDto(updateBiodata);
    }

    @Override
    public void deleteBiodata(String id) {
        repository.deleteById(id);
        repository.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public String urlImagesPerson(String nim) {
        String urlImages = "";
        Optional<Biodata> biodata = repository.findByNim(nim);
        if (biodata.isPresent()){
            urlImages = biodata.get().getPhoto();
        }
        return urlImages;
    }

    @Override
    public boolean cekToken(String fullName,String token) {
        String secretKey = "BIODATA_2023";

        byte[] hmacsha256 = calcHmacSha256(secretKey.getBytes(StandardCharsets.UTF_8),fullName.getBytes(StandardCharsets.UTF_8));

        String tokens = Base64.getEncoder().encodeToString(hmacsha256);

        return tokens.equalsIgnoreCase(token);
    }

    @Override
    public List<BiodataDto> searchDataByKewords(String keyword) {

        return  repository.findAll()
                .stream().filter(value -> value.getFullName().contains(keyword))
                .map(AutoBiodataMapper.MAPPER::mapToBiodataDto).toList();
    }

    @Override
    public String tokenSecretKey(String fullName) {
        String secretKey = "BIODATA_2023";

        byte[] hmacsha256 = calcHmacSha256(secretKey.getBytes(StandardCharsets.UTF_8),fullName.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hmacsha256);
    }

    private String urlImage(BiodataDto biodataDto) {

        String fileName = UUID.randomUUID() + "_" + biodataDto.getNim()+"_"+biodataDto.getFullName()+".jpeg";

        String[] parts = biodataDto.getPhoto().split(",");

        String imageBase64 = parts[0];
        byte[] imagesBytes = Base64.getDecoder().decode(imageBase64);
        InputStream inputStream = new ByteArrayInputStream(imagesBytes);
        Path uploadPath = Paths.get(uploadPathFile);
        try {
            // Membuat folder jika belum ada
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    inputStream,
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return urlUploadPath + fileName;
    }
}