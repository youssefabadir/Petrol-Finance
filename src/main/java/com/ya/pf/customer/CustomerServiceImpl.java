package com.ya.pf.customer;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerEntity> getCustomers(String name, int pageNo, int pageSize,
                                             String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (name.isEmpty()) {
            return customerRepository.findAll(pageable);
        } else {
            return customerRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {

        return customerRepository.save(customerEntity);
    }

    @Override
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {

        return customerRepository.save(customerEntity);
    }

    @Override
    public void deleteCustomer(long id) {

        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerEntity> searchCustomer(String name) {

        return customerRepository.findByNameContaining(name);
    }

}
