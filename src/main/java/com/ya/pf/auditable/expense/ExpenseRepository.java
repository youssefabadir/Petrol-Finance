package com.ya.pf.auditable.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    void deleteAllByShipment_Id(long shipmentId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e where e.shipment.id = :shipmentId")
    float totalShipmentExpenses(@Param("shipmentId") long shipmentId);

    List<ExpenseEntity> findAllByShipment_Id(long shipmentId);

}
