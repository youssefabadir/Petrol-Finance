package com.ya.pf.auditable.driver;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {

    Page<DriverEntity> findByNameContaining(String name, Pageable pageable);

    List<DriverEntity> findByNameContaining(String name);

}
