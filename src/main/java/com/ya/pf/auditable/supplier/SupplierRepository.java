package com.ya.pf.auditable.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {

    Page<SupplierEntity> findByNameContaining(String name, Pageable pageable);

    List<SupplierEntity> findByNameContaining(String name);

    @Modifying
    @Query("update SupplierEntity s set s.balance = :supplierBalance where s.id = :supplierId")
    void updateSupplierBalance(@Param("supplierId") long supplierId, @Param("supplierBalance") double supplierBalance);

}
