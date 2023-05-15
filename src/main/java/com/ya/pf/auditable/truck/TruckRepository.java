package com.ya.pf.auditable.truck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckRepository extends JpaRepository<TruckEntity, Long> {

    Page<TruckEntity> findByNumberContaining(String number, Pageable pageable);

    List<TruckEntity> findByNumberContaining(String number);

}
