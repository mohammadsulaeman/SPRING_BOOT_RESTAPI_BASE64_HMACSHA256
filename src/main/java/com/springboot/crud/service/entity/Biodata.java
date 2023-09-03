package com.springboot.crud.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "biodata")
public class Biodata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,length = 300)
    private String nim;
    @Column(length = 5000)
    private String fullName;
    @Column(length = 150)
    private String photo;
    @Column(unique = true,length = 450)
    private String email;
    @Column(unique = true,length = 15)
    private String phone;
    @Column(unique = true,length = 300)
    private String dob;
    @Column(unique = true,length = 65000)
    private String address;

}
