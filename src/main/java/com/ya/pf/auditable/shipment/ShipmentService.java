package com.ya.pf.auditable.shipment;

import org.springframework.data.domain.Page;

public interface ShipmentService {

    Page<ShipmentEntity> getShipments(String billNumber, int pageNo, int pageSize, String sortBy, String order);

    void createShipment(long billId, long truckId, float profit);

    void updateShipmentRevenue(long shipmentId, float amount);

    void deleteShipment(long shipmentId);

    void deleteShipmentByBillId(long billId);

}
