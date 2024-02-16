package com.ya.pf.auditable.bill;

import com.ya.pf.auditable.discount.entity.DiscountService;
import com.ya.pf.auditable.product.ProductEntity;
import com.ya.pf.auditable.shipment.ShipmentService;
import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import com.ya.pf.util.Helper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final CustomerTransactionService customerTransactionService;

    private final OwnerTransactionService ownerTransactionService;

    private final DiscountService discountService;

    private final ShipmentService shipmentService;

    @Override
    public Page<BillEntity> getBills(String number, int pageNo, int pageSize, String sortBy,
                                     String order, LocalDate start, LocalDate end) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (!number.isEmpty() && start != null && end != null) {
            return billRepository.findByNumberContainingAndDateBetween(number, Date.valueOf(start),
                                                                       Date.valueOf(end.plusDays(1)), pageable);
        } else if (number.isEmpty() && start != null && end != null) {
            return billRepository.findByDateBetween(Date.valueOf(start),
                                                    Date.valueOf(end.plusDays(1)), pageable);
        } else if (!number.isEmpty() && start == null && end == null) {
            return billRepository.findByNumberContaining(number, pageable);
        } else {
            return billRepository.findAll(pageable);
        }
    }

    @Override
    @Transactional
    public BillEntity createBill(BillEntity billEntity, long truckId) {

        if (billEntity.getId() != null) {
            billEntity.setId(null);
        }

        boolean exists = billRepository.existsByNumberAndSupplierEntity_Id(billEntity.getNumber(), billEntity.getSupplierEntity().getId());
        if (exists) {
            throw new EntityExistsException("This bill number exists for this supplier");
        } else {
            long customerId = billEntity.getCustomerEntity().getId();
            float billQuantity = billEntity.getQuantity();
            ProductEntity product = billEntity.getProductEntity();
            float supplierAmount = Math.abs(billQuantity * product.getSupplierPrice());
            float customerAmount;
            try {
                customerAmount = Math.abs(billQuantity * discountService.getCustomerDiscountedPrice(customerId, product.getId()));
            } catch (EntityNotFoundException e) {
                customerAmount = Math.abs(billQuantity * product.getCustomerPrice());
            }

            billEntity.setSupplierAmount(supplierAmount);
            billEntity.setCustomerAmount(customerAmount);
            BillEntity bill = billRepository.save(billEntity);

            long billId = bill.getId();
            java.util.Date billDate = bill.getDate();

            customerTransactionService.createCustomerTransaction(customerId, customerAmount * -1, null, billId, billDate);

            ownerTransactionService.createOwnerTransaction(bill.getSupplierEntity().getId(),
                                                           supplierAmount * -1,
                                                           null,
                                                           billId,
                                                           billDate);

            shipmentService.createShipment(billId, truckId, customerAmount - supplierAmount);

            return bill;
        }
    }

    @Override
    @Transactional
    public BillEntity updateBill(BillEntity billEntity, long truckId) {

        long billId = billEntity.getId();
        deleteBill(billId);
        billEntity.setId(null);
        return createBill(billEntity, truckId);
    }

    @Override
    @Transactional
    public void deleteBill(long id) {

        if (billRepository.existsById(id)) {
            BillEntity bill = billRepository.getReferenceById(id);

            billRepository.deleteById(id);
            customerTransactionService.deleteCustomerTransactionByBillId(id, bill.getCustomerAmount());
            ownerTransactionService.deleteOwnerTransactionByBillId(id, bill.getSupplierAmount());
            shipmentService.deleteShipmentByBillId(id);
        } else {
            throw new EntityNotFoundException("Bill with ID " + id + " not found");
        }
    }

}
