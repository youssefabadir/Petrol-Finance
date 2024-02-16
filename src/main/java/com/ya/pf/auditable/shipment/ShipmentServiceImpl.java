package com.ya.pf.auditable.shipment;

import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.expense.ExpenseService;
import com.ya.pf.auditable.truck.TruckEntity;
import com.ya.pf.auditable.truck.TruckService;
import com.ya.pf.util.Helper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final ExpenseService expenseService;

    private final TruckService truckService;

    @Override
    public Page<ShipmentEntity> getShipments(String billNumber, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (billNumber.trim().isEmpty()) {
            return shipmentRepository.findAll(pageable);
        } else {
            return shipmentRepository.findByBillNumber(billNumber, pageable);
        }
    }

    @Override
    @Transactional
    public void createShipment(long billId, long truckId, float profit) {

        TruckEntity truck = truckService.updateTruckBalance(truckId, profit);

        ShipmentEntity shipmentEntity = new ShipmentEntity();
        shipmentEntity.setBillEntity(new BillEntity().setId(billId));
        shipmentEntity.setTruckEntity(new TruckEntity().setId(truckId));
        shipmentEntity.setRevenue(profit);
        shipmentEntity.setTruckBalance(truck.getBalance());

        shipmentRepository.save(shipmentEntity);
    }

    @Override
    @Transactional
    public void updateShipmentRevenue(long shipmentId, float amount) {

        if (shipmentRepository.existsById(shipmentId)) {
            ShipmentEntity shipmentEntity = shipmentRepository.getReferenceById(shipmentId);
            long truckId = shipmentEntity.getTruckEntity().getId();
            shipmentEntity.setRevenue(shipmentEntity.getRevenue() + amount);
            shipmentRepository.updateTruckBalance(shipmentId, amount, truckId);
            shipmentRepository.save(shipmentEntity);
        } else {
            throw new EntityNotFoundException("Shipment with ID " + shipmentId + " is not found");
        }
    }

    @Override
    @Transactional
    public void deleteShipment(long shipmentId) {

        if (shipmentRepository.existsById(shipmentId)) {
            ShipmentEntity shipment = shipmentRepository.getReferenceById(shipmentId);
            long truckId = shipment.getTruckEntity().getId();
            expenseService.deleteExpensesByShipmentId(shipmentId, truckId);

            shipmentRepository.deleteById(shipmentId);
            truckService.updateTruckBalance(truckId, shipment.getRevenue() * -1);
        } else {
            throw new EntityNotFoundException("Shipment with ID " + shipmentId + " is not found");
        }
    }

    @Override
    @Transactional
    public void deleteShipmentByBillId(long billId) {

        ShipmentEntity shipment = shipmentRepository.findByBillEntity_Id(billId);
        deleteShipment(shipment.getId());
    }

}
