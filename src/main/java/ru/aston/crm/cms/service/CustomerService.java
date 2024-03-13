package ru.aston.crm.cms.service;

import ru.aston.crm.cms.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(int id);
    List<Customer> findByIndustry(String industry);
    void save(Customer customer);
    void update(int id, Customer updatedCustomer);
    void delete(int id);
}
