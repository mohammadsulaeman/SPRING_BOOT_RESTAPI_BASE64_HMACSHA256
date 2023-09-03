package com.springboot.crud.service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PropertyConfiguration {

    @Value("${spring.upload.path}")
    private String uploadPath;

    @Bean
    public String getUploadPath(){
        return uploadPath;
    }


    public static byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
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

    public static boolean deleteFile(String filePath){
        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
            return true; // File deleted successfully
        } catch (IOException e) {
            e.printStackTrace();
            return false; // An error occurred while deleting the file
        }
    }
}
