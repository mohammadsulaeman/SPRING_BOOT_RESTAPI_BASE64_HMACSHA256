package com.api.biodata.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "biodata")
public class Biodata implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
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