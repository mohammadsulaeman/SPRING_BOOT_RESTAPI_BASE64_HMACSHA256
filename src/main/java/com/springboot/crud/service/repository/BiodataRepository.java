package com.springboot.crud.service.repository;

import com.springboot.crud.service.entity.Biodata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiodataRepository extends JpaRepository<Biodata,Long> {
    Optional<Biodata> findByEmail(String email);
    Optional<Biodata> findByNim(String nim);
}
