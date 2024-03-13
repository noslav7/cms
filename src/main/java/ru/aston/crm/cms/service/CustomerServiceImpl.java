package ru.aston.crm.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.Customer;
import ru.aston.crm.cms.repository.CustomerRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Customer not found for the id " + id));
    }

    @Override
    public List<Customer> findByIndustry(String industry) {
        return customerRepository.findByIndustry(industry);
    }

    @Override
    @Transactional
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void update(int id, Customer updatedCustomer) {
        customerRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Customer not found for the id " + id));
        updatedCustomer.setCustomerId(id);
        customerRepository.save(updatedCustomer);
    }

    @Override
    @Transactional
    public void delete(int id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Customer not found for the id " + id));
        customerRepository.deleteById(id);
    }
}
