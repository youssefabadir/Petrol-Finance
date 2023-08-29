package com.ya.pf.auditable.shipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, Long> {

    Page<ShipmentEntity> findByBillEntity_Number(String billNumber, Pageable pageable);

    @Modifying
    @Query("UPDATE ShipmentEntity s SET s.truckBalance = s.truckBalance + :amount WHERE s.id > :id AND s.truckEntity.id = :truckId")
    void updateTruckBalance(@Param("id") long id, @Param("amount") float amount, @Param("truckId") long truckId);

    ShipmentEntity findByBillEntity_Id(long billId);

}
