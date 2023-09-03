package com.springboot.crud.service.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BiodataDto {
    private Long id;
    private String nim;
    private String fullName;
    private String photo;
    private String email;
    private String phone;
    private String dob;
    private String address;
}
