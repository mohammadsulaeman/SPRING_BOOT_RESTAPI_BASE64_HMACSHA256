package com.api.biodata.api.controller;

import com.api.biodata.api.dto.BiodataDto;
import com.api.biodata.api.service.BiodataService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/biodata")
@RequiredArgsConstructor
public class BiodataApiController {

    private final BiodataService biodataService;

    @Value("${file.upload-dir}")
    private String uploadPathFile;

    @Value("${file.url.upload-dir}")
    private String urlPathImages;

    @GetMapping("images/{nim}")
    public String getUrlImagesPerson(
            @PathVariable String nim
    ){
        String urlImages = biodataService.urlImagesPerson(nim);
        if (!urlImages.isEmpty())
            return urlImages;
        else
            return "";
    }

    @PostMapping("save")
    @SecurityRequirement(name = "token")
    public ResponseEntity<Map<String,Object>> saveBiodata(
            @RequestHeader  String token,
            @Valid @RequestBody BiodataDto biodataDto
            )
    {
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        int statusCodeKey;
        if (biodataService.cekToken(biodataDto.getFullName(), token)){
            BiodataDto saveBiodataDto = biodataService.saveBiodata(biodataDto);
            if (saveBiodataDto.getId() != null){
                statusCode = HttpStatus.CREATED;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("message","Data Berhasil Tersimpan");
                hasil.put("status","success");
                hasil.put("data",saveBiodataDto);
            }else {
                statusCode = HttpStatus.BAD_REQUEST;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("message","Data tidak Berhasil Di Simpan");
                hasil.put("status","failed");
                hasil.put("data",null);
            }
        }else {
            statusCode = HttpStatus.UNAUTHORIZED;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode", statusCodeKey);
            hasil.put("status","Unauthorized");
            hasil.put("message","invalid token");
        }
        return new ResponseEntity<>(hasil,statusCode);
    }


    @GetMapping("getAllBiodata")
    public ResponseEntity<Map<String, Object>> fetchAllBiodata()
    {
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        int statusCodeKey;
        List<BiodataDto> biodataDtoList = biodataService.fetchAllBiodata();
        if (biodataDtoList.isEmpty()){
            statusCode = HttpStatus.ACCEPTED;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode",statusCodeKey);
            hasil.put("message","dataList tidak ditemukan");
            hasil.put("status","failed");
            hasil.put("data",biodataDtoList);
        }else {
            statusCode = HttpStatus.ACCEPTED;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode",statusCodeKey);
            hasil.put("message","datalist ditemukan");
            hasil.put("status","success");
            hasil.put("data",biodataDtoList);
        }
        return new ResponseEntity<>(hasil,statusCode);
    }


    @GetMapping("search")
    public  ResponseEntity<Map<String,Object>> searchDataByKeyword(
            @RequestParam String keywords
    ){
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        int statusCodeKey;
        List<BiodataDto> biodataDtoList = biodataService.searchDataByKewords(keywords);
        if (biodataDtoList.isEmpty()){
            statusCode = HttpStatus.BAD_REQUEST;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode",statusCodeKey);
            hasil.put("message","dataList tidak ditemukan");
            hasil.put("status","failed");
            hasil.put("data",biodataDtoList);
        }else {
            statusCode = HttpStatus.ACCEPTED;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode",statusCodeKey);
            hasil.put("message","datalist ditemukan");
            hasil.put("status","success");
            hasil.put("data",biodataDtoList);
        }
        return new ResponseEntity<>(hasil,statusCode);
    }


    /**
     * create method list data by id
     */
    @GetMapping("getBiodata/{nim}")
    @SecurityRequirement(name = "token")
    public ResponseEntity<Map<String,Object>> getBiodataById(
            @PathVariable String nim,
            @RequestHeader  String token)
    {
        BiodataDto biodataDto = biodataService.fetchBiodataById(nim);
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        int statusCodeKey;
        if (biodataService.cekToken(biodataDto.getFullName(), token)){
            if (!biodataDto.getFullName().isEmpty()){
                statusCode = HttpStatus.ACCEPTED;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("message","Berhasil");
                hasil.put("data",biodataDto);
                hasil.put("status","success");
            }else {
                statusCode = HttpStatus.ACCEPTED;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("status","failed");
                hasil.put("message","data tidak ditemukan");
                hasil.put("data",biodataDto);
            }
        }else{
            statusCode = HttpStatus.UNAUTHORIZED;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode", statusCodeKey);
            hasil.put("status","Unauthorized");
            hasil.put("message","Invalid Token");
        }


        return new ResponseEntity<>(hasil,statusCode);
    }
    /**
     * create method update
     */
    @PutMapping("update/{nim}")
    @SecurityRequirement(name = "token")
    public ResponseEntity<Map<String,Object>> updateBiodata(
            @RequestHeader String token,
            @PathVariable String nim,
            @RequestBody BiodataDto biodataDto) throws IOException
    {
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        int statusCodeKey;

        if (biodataService.cekToken(biodataDto.getFullName(), token)){

            BiodataDto biodataDto1 = biodataService.fetchBiodataById(nim);
            String fotoLama = biodataDto1.getPhoto().replaceAll(urlPathImages,"");
            Path pathPublic = Path.of(uploadPathFile);
            if (Files.exists(pathPublic)){

                Path filePhoto = pathPublic.resolve(fotoLama);
                if (Files.exists(filePhoto)){
                    Files.delete(filePhoto);
                }

                BiodataDto update = biodataService.updateBiodata(biodataDto);
                if (!update.getFullName().isEmpty()){
                    statusCode = HttpStatus.CREATED;
                    statusCodeKey = statusCode.value();
                    hasil.put("statusCode",statusCodeKey);
                    hasil.put("message","Data Berhasil Di Update");
                    hasil.put("status","success");
                    hasil.put("data",update);
                    //hasil.put("data",getData);
                }else {
                    statusCode = HttpStatus.ACCEPTED;
                    statusCodeKey = statusCode.value();
                    hasil.put("statusCode",statusCodeKey);
                    hasil.put("message","Data tidak berhasil di update");
                    hasil.put("status","failed");
                    hasil.put("data",update.toString());
                }
            }else{
                statusCode = HttpStatus.ACCEPTED;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("status","failed");
                hasil.put("message","Foto Lama gagal di hapus");
            }



        }else{
            statusCode = HttpStatus.UNAUTHORIZED;
            statusCodeKey = statusCode.value();
            hasil.put("statusCode", statusCodeKey);
            hasil.put("status","Unauthorized");
            hasil.put("message","Invalid Token");
        }
        return  new ResponseEntity<>(hasil,statusCode);
    }

    /**
     * create method delete data
     */
    @DeleteMapping("delete/{nim}")
    @SecurityRequirement(name = "token")
    public ResponseEntity<Map<String,Object>> deleteBiodata(@PathVariable String nim,@RequestHeader String token) throws IOException {
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode = HttpStatus.FOUND;
        int statusCodeKey = statusCode.value();
        BiodataDto biodataDto1 = biodataService.fetchBiodataById(nim);
        if (biodataService.cekToken(biodataDto1.getFullName(),token)){
            String fotoLama = biodataDto1.getPhoto().replaceAll(urlPathImages,"");
            Path pathFotoLama = Path.of(uploadPathFile);
            if (Files.exists(pathFotoLama)){
                Path fileName = pathFotoLama.resolve(fotoLama);
                if (Files.exists(fileName)){
                    Files.delete(fileName);
                }

                biodataService.deleteBiodata(biodataDto1.getId());
                statusCode = HttpStatus.ACCEPTED;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("status","success");
                hasil.put("message","Delete Data Biodata Berhasil Dilakukan");
            }else{
                statusCode = HttpStatus.ACCEPTED;
                statusCodeKey = statusCode.value();
                hasil.put("statusCode",statusCodeKey);
                hasil.put("status","failed");
                hasil.put("message","Delete Data Biodata tidak berhasil dilakukan");
            }
        }

        return new ResponseEntity<>(hasil,statusCode);
    }

    /**
     * create method delete file
     */
    @GetMapping("deletGambar/{nim}")
    public ResponseEntity<Map<String,Object>> deleteGambar(@PathVariable String nim) throws IOException {
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus status;
        int statusCodeKey;
        BiodataDto biodataDto = biodataService.fetchBiodataById(nim);
        String namaFile = biodataDto.getPhoto();

        Path publicPath = Path.of(uploadPathFile);
        if (Files.exists(publicPath))
        {
            Path fileName = publicPath.resolve(namaFile);
            Files.delete(fileName);
            status = HttpStatus.ACCEPTED;
            statusCodeKey = status.value();
            hasil.put("statusCode",statusCodeKey);
            hasil.put("message","berhasil hapus gambar");
        }else {
            status = HttpStatus.ACCEPTED;
            statusCodeKey = status.value();
            hasil.put("statusCode",statusCodeKey);
            hasil.put("message","gagal hapus gambar");
        }

        return new ResponseEntity<>(hasil,status);
    }
}