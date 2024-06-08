package com.ya.pf.customer;

import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.customer.CustomerRepository;
import com.ya.pf.auditable.customer.CustomerServiceImpl;
import com.ya.pf.util.Helper;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {


    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Filter filter;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerEntity customerEntity;

    @BeforeEach
    public void setUp() {

        customerEntity = new CustomerEntity();
        customerEntity.setName("Youssef");
        customerEntity.setBalance(0f);
    }

    @AfterEach
    public void tearDown() {

        customerEntity = null;
        entityManager = null;
        session = null;
        filter = null;
    }

    private void enableFilter() {

        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("deletedCustomerFilter")).thenReturn(filter);
    }

    @Test
    public void testCreateCustomer() {

        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);

        CustomerEntity createdCustomer = customerService.createCustomer(customerEntity);

        verify(customerRepository).save(customerEntity);
        assertThat(createdCustomer).isEqualTo(customerEntity);
    }

    @Test
    public void testUpdateCustomer() {

        enableFilter();
        customerEntity.setId(1L);
        customerEntity.setName("Sandra");
        when(customerRepository.existsById(customerEntity.getId())).thenReturn(true);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);

        CustomerEntity updatedCustomer = customerService.updateCustomer(customerEntity);

        verify(customerRepository).save(customerEntity);
        assertThat(updatedCustomer).isEqualTo(customerEntity);
    }

    @Test
    public void testDeleteCustomer() {

        enableFilter();
        long id = 1L;
        when(customerRepository.existsById(id)).thenReturn(true);
        customerService.deleteCustomer(id);
        verify(customerRepository).deleteById(id);
    }

    @Test
    public void testDeleteCustomerNotFound() {

        enableFilter();
        long id = 1L;
        when(customerRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(id));
    }

    @Test
    public void testGetAllCustomers() {

        enableFilter();
        Pageable pageable = Helper.preparePageable(0, 5, "id", "desc");
        Page<CustomerEntity> page = new PageImpl<>(Collections.singletonList(customerEntity));
        when(customerRepository.findAll(pageable)).thenReturn(page);

        Page<CustomerEntity> result = customerService.getCustomers("", 0, 5, "id", "desc");

        assertThat(result).isEqualTo(page);
        verify(customerRepository).findAll(pageable);

    }

    @Test
    public void testGetAllCustomersWithName() {

        enableFilter();
        Pageable pageable = Helper.preparePageable(0, 5, "id", "desc");
        Page<CustomerEntity> page = new PageImpl<>(Collections.singletonList(customerEntity));
        when(customerRepository.findByNameContaining("You", pageable)).thenReturn(page);

        Page<CustomerEntity> result = customerService.getCustomers("You", 0, 5, "id", "desc");

        assertThat(result).isEqualTo(page);
        verify(customerRepository).findByNameContaining("You", pageable);

    }

    @Test
    public void testSearchCustomer() {

        enableFilter();
        List<CustomerEntity> customers = Collections.singletonList(customerEntity);
        when(customerRepository.findByNameContaining("You")).thenReturn(customers);

        List<CustomerEntity> result = customerService.searchCustomer("You");

        assertThat(result).isEqualTo(customers);
        verify(customerRepository).findByNameContaining("You");
    }

    @Test
    public void testGetCustomerById() {

        enableFilter();
        long id = 1L;
        when(customerRepository.getReferenceById(id)).thenReturn(customerEntity);

        customerService.getCustomerById(id);

        verify(customerRepository).getReferenceById(id);
    }

    @Test
    public void testUpdateCustomerBalance() {

        enableFilter();
        long id = 1L;
        float balance = 10f;

        customerService.updateCustomerBalance(id, balance);

        verify(customerRepository).updateCustomerBalance(id, balance);
    }

}
