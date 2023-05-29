package com.ya.pf.auditable.bill;

import com.ya.pf.auditable.discount.entity.DiscountService;
import com.ya.pf.auditable.product.ProductService;
import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final ProductService productService;

    private final CustomerTransactionService customerTransactionService;

    private final OwnerTransactionService ownerTransactionService;

    private final DiscountService discountService;

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
    public BillEntity createBill(BillEntity billEntity) {

        if (billEntity.getId() != null) {
            billEntity.setId(null);
        }

        boolean exists = billRepository.existsByNumberAndSupplierEntity_Id(billEntity.getNumber(), billEntity.getSupplierEntity().getId());
        if (exists) {
            throw new EntityExistsException("This bill number exists for this supplier");
        } else {
            long productId = billEntity.getProductEntity().getId();
            double productPrice = productService.getProductCustomerPrice(productId);
            long customerId = billEntity.getCustomerEntity().getId();
            double billAmount;

            try {
                double discount = 1 - (discountService.getCustomerDiscountForProduct(customerId, productId) / 100);
                billAmount = productPrice * billEntity.getQuantity() * discount;
            } catch (EntityNotFoundException e) {
                billAmount = productPrice * billEntity.getQuantity();
            }

            billEntity.setAmount(billAmount);

            BillEntity bill = billRepository.save(billEntity);
            long billId = bill.getId();
            java.util.Date billDate = bill.getDate();

            customerTransactionService.createCustomerTransaction(customerId,
                                                                 billAmount * -1, null, billId, billDate);

            ownerTransactionService.createOwnerTransaction(bill.getSupplierEntity().getId(),
                                                           billAmount * -1, null, billId, billDate);

            return bill;
        }
    }

    @Override
    @Transactional
    public void deleteBill(long id) {

        if (billRepository.existsById(id)) {
            BillEntity bill = billRepository.getReferenceById(id);
            double billAmount = bill.getAmount();

            billRepository.deleteById(id);
            customerTransactionService.deleteCustomerTransactionByBillId(id, billAmount);
            ownerTransactionService.deleteOwnerTransactionByBillId(id, billAmount);
        } else {
            throw new EntityNotFoundException("Bill with ID " + id + " not found");
        }
    }

}
