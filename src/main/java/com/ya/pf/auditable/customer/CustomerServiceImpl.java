package com.ya.pf.auditable.customer;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final EntityManager entityManager;

    @Override
    public Page<CustomerEntity> getCustomers(String name, int pageNo, int pageSize,
                                             String sortBy, String order) {

        enableDeletedCustomerFilter();
        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
        if (name.trim().isEmpty()) {
            return customerRepository.findAll(pageable);
        } else {
            return customerRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {

        if (customerEntity.getId() != null) {
            customerEntity.setId(null);
        }

        customerEntity.setStartBalance(customerEntity.getBalance());
        return customerRepository.save(customerEntity);
    }

    @Override
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {

        enableDeletedCustomerFilter();
        if (customerRepository.existsById(customerEntity.getId())) {
            return customerRepository.save(customerEntity);
        } else {
            throw new EntityNotFoundException("Customer with ID " + customerEntity.getId() + " not found");
        }
    }

    @Override
    public void deleteCustomer(long id) {

        enableDeletedCustomerFilter();
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Customer with ID " + id + " not found");
        }
    }

    @Override
    public List<CustomerEntity> searchCustomer(String name) {

        enableDeletedCustomerFilter();
        return customerRepository.findByNameContaining(name);
    }

    @Override
    public CustomerEntity getCustomerById(long id) {

        enableDeletedCustomerFilter();
        return customerRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public void updateCustomerBalance(long customerId, float customerBalance) {

        enableDeletedCustomerFilter();
        customerRepository.updateCustomerBalance(customerId, customerBalance);
    }

    @Override
    public void enableDeletedCustomerFilter() {

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedCustomerFilter");
    }

}
