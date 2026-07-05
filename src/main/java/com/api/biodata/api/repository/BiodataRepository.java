package com.api.biodata.api.repository;


import com.api.biodata.api.entity.Biodata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiodataRepository extends JpaRepository<Biodata, String> {
    Optional<Biodata> findByEmail(String email);
    Optional<Biodata> findByNim(String nim);
}