package ru.aston.crm.cms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.aston.crm.cms.model.Customer;
import ru.aston.crm.cms.repository.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void whenFindAll_thenCustomersReturned() {
        List<Customer> expectedCustomers = Arrays.asList(new Customer(), new Customer());
        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        List<Customer> actualCustomers = customerService.findAll();

        verify(kafkaTemplate).send(eq("cms"), eq("FIND ALL CUSTOMERS"));
        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    void whenFindByIdWithValidId_thenCustomerReturned() {
        int customerId = 1;
        Customer expectedCustomer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        Customer actualCustomer = customerService.findById(customerId);

        verify(kafkaTemplate).send(eq("cms"), eq("FIND BY CUSTOMER BY ID: " + customerId));
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    void whenFindByIdWithInvalidId_thenThrowException() {
        int customerId = 99;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
                customerService.findById(customerId));

        assertEquals("Customer not found for the id " + customerId, exception.getMessage());
    }

    @Test
    void whenFindByIndustry_thenCustomersReturned() {
        String industry = "Tech";
        List<Customer> expectedCustomers = Arrays.asList(new Customer(), new Customer());
        when(customerRepository.findByIndustry(industry)).thenReturn(expectedCustomers);

        List<Customer> actualCustomers = customerService.findByIndustry(industry);

        verify(kafkaTemplate).send(eq("cms"), eq("FIND CUSTOMER BY INDUSTRY: " + industry));
        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    void whenSave_thenCustomerSaved() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setOrganisation("Org");
        customer.setCity("City");
        customer.setIndustry("Tech");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        verify(kafkaTemplate).send(eq("cms"), contains("SAVE CUSTOMER: "));
        assertEquals(customer, savedCustomer);
    }

    @Test
    void whenUpdateValidCustomer_thenCustomerUpdated() {
        int customerId = 1;
        Customer originalCustomer = new Customer();
        Customer updatedCustomer = new Customer();
        updatedCustomer.setOrganisation("New Org");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(originalCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.update(customerId, updatedCustomer);

        verify(kafkaTemplate).send(eq("cms"), contains("UPDATE CUSTOMER: "));
        assertEquals(updatedCustomer.getOrganisation(), result.getOrganisation());
    }

    @Test
    void whenUpdateInvalidCustomer_thenThrowException() {
        int customerId = 99;
        Customer updatedCustomer = new Customer();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
                customerService.update(customerId, updatedCustomer));

        assertEquals("Customer not found for the id " + customerId, exception.getMessage());
    }

    @Test
    void whenDeleteValidCustomer_thenCustomerDeleted() {
        int customerId = 1;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        customerService.delete(customerId);

        verify(kafkaTemplate).send(eq("cms"), eq("DELETE CUSTOMER BY ID: " + customerId));

        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void whenDeleteInvalidCustomer_thenThrowException() {
        int customerId = 99;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
                customerService.delete(customerId));

        assertEquals("Customer not found for the id " + customerId, exception.getMessage());

        verify(kafkaTemplate, never()).send(anyString(), anyString());
        verify(customerRepository, never()).deleteById(anyInt());
    }
}