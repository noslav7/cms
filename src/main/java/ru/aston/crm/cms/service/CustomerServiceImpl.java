package ru.aston.crm.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.Customer;
import ru.aston.crm.cms.repository.CustomerRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Customer> findAll() {
        kafkaTemplate.send("cms", "FIND ALL CUSTOMERS");
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(int id) {
        kafkaTemplate.send("cms", "FIND BY CUSTOMER BY ID: " + id);
        return customerRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Customer not found for the id " + id));
    }

    @Override
    public List<Customer> findByIndustry(String industry) {
        kafkaTemplate.send("cms", "FIND CUSTOMER BY INDUSTRY: " + industry);
        return customerRepository.findByIndustry(industry);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        kafkaTemplate.send("cms", "SAVE CUSTOMER: " +
                customer.getOrganisation() + " " + customer.getCity() + " " +
                customer.getIndustry());
        customerRepository.save(customer);
        return customer;
    }

    @Override
    @Transactional
    public Customer update(int id, Customer updatedCustomer) {
        customerRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Customer not found for the id " + id));
        kafkaTemplate.send("cms", "UPDATE CUSTOMER: " +
                updatedCustomer.getOrganisation() + " " + updatedCustomer.getCity() + " " +
                updatedCustomer.getIndustry());
        updatedCustomer.setCustomerId(id);
        customerRepository.save(updatedCustomer);
        return updatedCustomer;
    }

    @Override
    @Transactional
    public void delete(int id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Customer not found for the id " + id));
        kafkaTemplate.send("cms", "DELETE CUSTOMER BY ID: " + id);
        customerRepository.deleteById(id);
    }
}
