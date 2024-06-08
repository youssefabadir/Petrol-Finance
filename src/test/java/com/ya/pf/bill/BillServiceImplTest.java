package com.ya.pf.bill;

import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.bill.BillRepository;
import com.ya.pf.auditable.bill.BillServiceImpl;
import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.discount.entity.DiscountService;
import com.ya.pf.auditable.product.ProductEntity;
import com.ya.pf.auditable.shipment.ShipmentService;
import com.ya.pf.auditable.supplier.SupplierEntity;
import com.ya.pf.auditable.transaction.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.transaction.owner_transaction.entity.OwnerTransactionService;
import com.ya.pf.util.Helper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private CustomerTransactionService customerTransactionService;

    @Mock
    private OwnerTransactionService ownerTransactionService;

    @Mock
    private ShipmentService shipmentService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private BillServiceImpl billService;

    private BillEntity bill;
    private final String billNumber = "123";
    private final String sortBy = "date";
    private final String order = "desc";

    @BeforeEach
    public void setUp() {

        SupplierEntity supplier = new SupplierEntity();
        supplier.setId(1L);

        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setSupplierPrice(50f);
        product.setCustomerPrice(100f);

        bill = new BillEntity();
        bill.setId(1L);
        bill.setNumber("1");
        bill.setQuantity(2);
        bill.setProductEntity(product);
        bill.setSupplierEntity(supplier);
        bill.setCustomerEntity(customer);
        bill.setDate(new java.util.Date());
    }

    @Test
    public void testGetAllBills() {

        Pageable pageable = Helper.preparePageable(0, 5, sortBy, order);
        Page<BillEntity> page = new PageImpl<>(Collections.singletonList(bill));

        when(billRepository.findAll(pageable)).thenReturn(page);

        Page<BillEntity> result = billService.getBills("", 0, 5, sortBy, order, null, null);

        assertThat(result).isEqualTo(page);
        verify(billRepository).findAll(pageable);
    }

    @Test
    public void testGetBillsWithNumberAndDate() {

        LocalDate startLocalDate = LocalDate.now().minusDays(1);
        LocalDate endLocalDate = LocalDate.now().plusDays(1);
        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Pageable pageable = Helper.preparePageable(0, 5, sortBy, order);
        Page<BillEntity> page = new PageImpl<>(Collections.singletonList(bill));

        when(billRepository.findByNumberContainingAndDateBetween(billNumber, startDate, endDate, pageable)).thenReturn(page);

        Page<BillEntity> result = billService.getBills(billNumber, 0, 5, sortBy, order, startLocalDate, endLocalDate);

        assertThat(result).isEqualTo(page);
        verify(billRepository).findByNumberContainingAndDateBetween(billNumber, startDate, endDate, pageable);
    }

    @Test
    public void testGetBillsWithNumber() {

        Pageable pageable = Helper.preparePageable(0, 5, sortBy, order);
        Page<BillEntity> page = new PageImpl<>(Collections.singletonList(bill));

        when(billRepository.findByNumberContaining(billNumber, pageable)).thenReturn(page);

        Page<BillEntity> result = billService.getBills(billNumber, 0, 5, sortBy, order, null, null);

        assertThat(result).isEqualTo(page);
        verify(billRepository).findByNumberContaining(billNumber, pageable);
    }

    @Test
    public void testGetBillsWithDateBetween() {

        LocalDate startLocalDate = LocalDate.now().minusDays(1);
        LocalDate endLocalDate = LocalDate.now().plusDays(1);
        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Pageable pageable = Helper.preparePageable(0, 5, sortBy, order);
        Page<BillEntity> page = new PageImpl<>(Collections.singletonList(bill));

        when(billRepository.findByDateBetween(startDate, endDate, pageable)).thenReturn(page);

        Page<BillEntity> result = billService.getBills("", 0, 5, sortBy, order, startLocalDate, endLocalDate);

        assertThat(result).isEqualTo(page);
        verify(billRepository).findByDateBetween(startDate, endDate, pageable);
    }

    @Test
    public void testCreateBill() {

        when(billRepository.save(bill)).thenAnswer(invocation -> {
            BillEntity toSave = invocation.getArgument(0);
            toSave.setId(1L);
            return toSave;
        });
        when(discountService.getCustomerDiscountedPrice(bill.getCustomerEntity().getId(), bill.getProductEntity().getId())).thenReturn(1f);

        BillEntity savedBill = billService.createBill(bill, 1);

        verify(billRepository).save(bill);
        assertThat(savedBill).isEqualTo(bill);
    }

    @Test
    public void testCreateBillExists() {

        when(billRepository.existsByNumberAndSupplierEntity_Id(bill.getNumber(), bill.getSupplierEntity().getId())).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> billService.createBill(bill, 1L));
    }

    @Test
    public void testDeleteBill() {

        long id = 1L;
        when(billRepository.existsById(id)).thenReturn(true);
        when(billRepository.getReferenceById(id)).thenReturn(bill);

        billService.deleteBill(id);

        verify(customerTransactionService).deleteCustomerTransactionByBillId(bill.getCustomerEntity().getId(), id, bill.getCustomerAmount(), bill.getDate());
        verify(ownerTransactionService).deleteOwnerTransactionByBillId(bill.getSupplierEntity().getId(), id, bill.getSupplierAmount(), bill.getDate());
        verify(shipmentService).deleteShipmentByBillId(id);
        verify(billRepository).existsById(id);
        verify(billRepository).getReferenceById(id);
        verify(billRepository).deleteById(id);
    }

    @Test
    public void testDeleteBillNotFound() {

        long id = 1L;
        when(billRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> billService.deleteBill(id));
        verify(billRepository).existsById(id);
    }

}
