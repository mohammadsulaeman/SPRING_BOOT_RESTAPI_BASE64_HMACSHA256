package com.springboot.crud.service.controller;

import com.springboot.crud.service.configuration.PropertyConfiguration;
import com.springboot.crud.service.dto.BiodataDto;
import com.springboot.crud.service.service.BiodataService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboot.crud.service.configuration.PropertyConfiguration.calcHmacSha256;

@RestController
@RequestMapping("api/v1/biodata")
@AllArgsConstructor
public class BiodataApiController {
    BiodataService biodataService;

    PropertyConfiguration propertyConfiguration;


    /**
     * menampilkan gambar pada browser
     */
    @GetMapping("images/{imagesName}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imagesName") String imagesName) throws IOException {
        System.out.println("imagesName : "+imagesName);
        Path publicPath = Path.of(propertyConfiguration.getUploadPath());
        if (!Files.exists(publicPath)){
            return ResponseEntity.notFound().build();
        }


        Path filePath = publicPath.resolve(imagesName);
        byte[] imagesBytes = Files.readAllBytes(filePath);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG) // Sesuaikan dengan tipe gambar yang Anda gunakan
                .body(imagesBytes);
    }

    /**
     * create method save data
     */
    @PostMapping("save")
    public ResponseEntity<Map<String,Object>> saveBiodata(@RequestHeader("token") String token, @RequestBody BiodataDto biodataDto) throws IOException {

        /**
         * variabel
         */
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        /**
         * create token by hmacsha256 to encode base64
         */
        String message = biodataDto.getFullName();
        System.out.println("message : "+message);
        String secretKey = "BIODATA_2023";
        System.out.println("secretKey : "+secretKey);

        byte[] hmacsha256 = calcHmacSha256(secretKey.getBytes(StandardCharsets.UTF_8),message.getBytes(StandardCharsets.UTF_8));

        String tokens = Base64.getEncoder().encodeToString(hmacsha256);
        System.out.println("tokens : "+tokens);
        System.out.println("token header : "+token);
        if (tokens.equalsIgnoreCase(token)){


            String[] parts = biodataDto.getPhoto().split(",");

            String imageBase64 = parts[0];
            byte[] imagesBytes = Base64.getDecoder().decode(imageBase64);
            String fileName = biodataDto.getNim()+"_"+biodataDto.getFullName()+".jpeg";
            Path publicPath = Path.of(propertyConfiguration.getUploadPath());
            if (!Files.exists(publicPath)){
                Files.createDirectories(publicPath);
            }
            Path filePath = publicPath.resolve(fileName);
            String pathImages = filePath.toString();
            FileUtils.writeByteArrayToFile(new File(pathImages),imagesBytes);
            biodataDto.setPhoto(fileName);
            BiodataDto savedBiodata = biodataService.saveBiodata(biodataDto);
            if (!savedBiodata.getFullName().isEmpty() || !savedBiodata.getFullName().isBlank()){
                BiodataDto getBiodataId = biodataService.fetchBiodataById(savedBiodata.getNim());
                statusCode = HttpStatus.CREATED;
                hasil.put("statusCode",statusCode.toString());
                hasil.put("message","Data Berhasil Tersimpan");
                hasil.put("status","success");
                hasil.put("data",getBiodataId);
            }else {
                statusCode = HttpStatus.BAD_REQUEST;
                hasil.put("statusCode",statusCode.toString());
                hasil.put("message","Data tidak Berhasil Di Simpan");
                hasil.put("status","failed");
                hasil.put("data",savedBiodata);
            }
        }else {
            statusCode = HttpStatus.UNAUTHORIZED;
            hasil.put("statusCode", statusCode.toString());
            hasil.put("status","Unauthorized");
            hasil.put("message","Silakan gunakan tokens : "+tokens);
        }
        return new ResponseEntity<>(hasil,statusCode);
    }

    /**
     * create method list data
     */
    @GetMapping("getAllBiodata")
    public ResponseEntity<Map<String, Object>> fetchAllBiodata(){
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        List<BiodataDto> biodataDtoList = biodataService.fetchAllBiodata();
        if (biodataDtoList.isEmpty()){
            statusCode = HttpStatus.BAD_REQUEST;
            hasil.put("statusCode",statusCode);
            hasil.put("message","dataList tidak ditemukan");
            hasil.put("status","failed");
            hasil.put("data",biodataDtoList);
        }else {
            statusCode = HttpStatus.ACCEPTED;
            hasil.put("statusCode",statusCode);
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
    public ResponseEntity<Map<String,Object>> getBiodataById(@PathVariable("nim") String nim){
        BiodataDto biodataDto = biodataService.fetchBiodataById(nim);
        Map<String,Object> hasil = new HashMap<>();
        HttpStatus statusCode;
        if (!biodataDto.getFullName().isEmpty() || !biodataDto.getFullName().isBlank()){
            statusCode = HttpStatus.ACCEPTED;
            hasil.put("statusCode",statusCode);
            hasil.put("message","Berhasil");
            hasil.put("data",biodataDto);
            hasil.put("status","success");
        }else {
            statusCode = HttpStatus.BAD_REQUEST;
            hasil.put("statusCode",statusCode);
            hasil.put("status","failed");
            hasil.put("message","data tidak ditemukan");
            hasil.put("data",biodataDto);
        }

        return new ResponseEntity<>(hasil,statusCode);
    }
    /**
     * create method update
     */
    @PutMapping("update/{nim}")
    public ResponseEntity<Map<String,String>> updateBiodata(@RequestHeader("token") String token, @PathVariable("nim") String nim,@RequestBody BiodataDto biodataDto) throws IOException {
        Map<String,String> hasil = new HashMap<>();
        HttpStatus statusCode;
        /**
         * create token by hmacsha256 to encode base64
         */
        String message = biodataDto.getFullName();
        System.out.println("message : "+message);
        String secretKey = "BIODATA_2023";
        System.out.println("secretKey : "+secretKey);

        byte[] hmacsha256 = calcHmacSha256(secretKey.getBytes(StandardCharsets.UTF_8),message.getBytes(StandardCharsets.UTF_8));

        String tokens = Base64.getEncoder().encodeToString(hmacsha256);
        System.out.println("tokens Code : "+tokens);
        System.out.println("token param : "+token);
        if (tokens.equalsIgnoreCase(token)){
            /**
             * hapus foto lama
             */
            BiodataDto biodataDto1 = biodataService.fetchBiodataById(nim);
            String fotoLama = biodataDto1.getPhoto();
            Path pathPublic = Path.of(propertyConfiguration.getUploadPath());
            if (Files.exists(pathPublic)){
                /**
                 * delete foto lama
                 */
                Path filePhoto = pathPublic.resolve(fotoLama);
                Files.delete(filePhoto);

                /**
                 * update foto
                 */
                String[] parts = biodataDto.getPhoto().split(",");

                String imageBase64 = parts[0];
                byte[] imagesBytes = Base64.getDecoder().decode(imageBase64);
                String fileName = biodataDto.getNim()+"_"+biodataDto.getFullName()+".jpeg";
                Path publicPath = Path.of(propertyConfiguration.getUploadPath());
                if (!Files.exists(publicPath)){
                    Files.createDirectories(publicPath);
                }
                Path filePath = publicPath.resolve(fileName);
                String pathImages = filePath.toString();
                FileUtils.writeByteArrayToFile(new File(pathImages),imagesBytes);

                biodataDto1.setNim(nim);
                biodataDto1.setFullName(biodataDto.getFullName());
                biodataDto1.setEmail(biodataDto.getEmail());
                biodataDto1.setPhone(biodataDto.getPhone());
                biodataDto1.setAddress(biodataDto.getAddress());
                biodataDto1.setPhoto(fileName);
                BiodataDto update = biodataService.updateBiodata(biodataDto1);
                if (!update.getFullName().isEmpty() || !update.getFullName().isBlank()){
                    statusCode = HttpStatus.CREATED;
                    BiodataDto getData = biodataService.fetchBiodataById(update.getNim());
                    hasil.put("statusCode",statusCode.toString());
                    hasil.put("message","Data Berhasil Di Update");
                    hasil.put("status","success");
                    hasil.put("data",getData.toString());
                    //hasil.put("data",getData);
                }else {
                    statusCode = HttpStatus.BAD_REQUEST;
                    hasil.put("statusCode",statusCode.toString());
                    hasil.put("message","Data tidak berhasil di update");
                    hasil.put("status","failed");
                    hasil.put("data",update.toString());
                }
            }else{
                statusCode = HttpStatus.BAD_REQUEST;
                hasil.put("statusCode",statusCode.toString());
                hasil.put("status","failed");
                hasil.put("message","Foto Lama gagal di hapus");
            }



        }else{
            statusCode = HttpStatus.UNAUTHORIZED;
            hasil.put("statusCode", statusCode.toString());
            hasil.put("status","Unauthorized");
            hasil.put("message","Silakan gunakan tokens : "+tokens);
        }
        return  new ResponseEntity<>(hasil,statusCode);
    }

    /**
     * create method delete data
     */
    @DeleteMapping("delete/{nim}")
    public ResponseEntity<Map<String,String>> deleteBiodata(@PathVariable("nim") String nim) throws IOException {
        Map<String,String> hasil = new HashMap<>();
        HttpStatus statusCode;

        BiodataDto biodataDto1 = biodataService.fetchBiodataById(nim);
        String fotoLama = biodataDto1.getPhoto();
        Path pathFotoLama = Path.of(propertyConfiguration.getUploadPath());
        System.out.println("pathFotoLama : "+pathFotoLama);
        if (Files.exists(pathFotoLama)){
            Path fileName = pathFotoLama.resolve(fotoLama);
            Files.delete(fileName);
            biodataService.deleteBiodata(biodataDto1.getId());
            statusCode = HttpStatus.ACCEPTED;
            hasil.put("statusCode",statusCode.toString());
            hasil.put("status","success");
            hasil.put("message","Delete Data Biodata Berhasil Dilakukan");
        }else{
            statusCode = HttpStatus.BAD_REQUEST;
            hasil.put("statusCode",statusCode.toString());
            hasil.put("status","failed");
            hasil.put("message","Delete Data Biodata tidak berhasil dilakukan");
        }
        return new ResponseEntity<>(hasil,statusCode);
    }

    /**
     * create method delete file
     */
    @GetMapping("deletGambar/{nim}")
    public ResponseEntity<Map<String,String>> deleteGambar(@PathVariable("nim") String nim) throws IOException {
        Map<String,String> hasil = new HashMap<>();
        HttpStatus status;
        BiodataDto biodataDto = biodataService.fetchBiodataById(nim);
        String namaFile = biodataDto.getPhoto();

        Path publicPath = Path.of(propertyConfiguration.getUploadPath());
        if (Files.exists(publicPath))
        {
            Path fileName = publicPath.resolve(namaFile);
            System.out.println("fileName : "+fileName.toString());
            Files.delete(fileName);
            status = HttpStatus.ACCEPTED;
            hasil.put("statusCode",status.toString());
            hasil.put("message","berhasil hapus gambar");
        }else {
            status = HttpStatus.BAD_REQUEST;
            hasil.put("statusCode",status.toString());
            hasil.put("message","gagal hapus gambar");
        }

        return new ResponseEntity<>(hasil,status);
    }
}
