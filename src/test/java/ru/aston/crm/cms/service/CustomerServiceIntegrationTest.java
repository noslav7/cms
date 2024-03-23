package ru.aston.crm.cms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.Customer;
import ru.aston.crm.cms.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"cms"})
@Transactional
public class CustomerServiceIntegrationTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setOrganisation("Acme Corporation");
        customer.setCity("Cityville");
        customer.setIndustry("Technology");
        savedCustomer = customerRepository.save(customer);
    }

    @Test
    void whenFindAll_thenAllCustomersReturned() {
        List<Customer> customers = customerService.findAll();

        assertThat(customers).isNotEmpty();
        verify(kafkaTemplate).send(eq("cms"), eq("FIND ALL CUSTOMERS"));
    }

    @Test
    void whenFindById_thenCorrectCustomerReturned() {
        Customer customer = customerService.findById(savedCustomer.getCustomerId());

        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
        verify(kafkaTemplate).send(eq("cms"), eq("FIND BY CUSTOMER BY ID: " + customer.getCustomerId()));
    }

    @Test
    void whenFindByIndustry_thenCustomersInThatIndustryReturned() {
        List<Customer> customers = customerService.findByIndustry("Technology");

        assertThat(customers).isNotEmpty();
        verify(kafkaTemplate).send(eq("cms"), eq("FIND CUSTOMER BY INDUSTRY: Technology"));
    }

    @Test
    void whenSave_thenCustomerSaved() {
        Customer newCustomer = new Customer();
        newCustomer.setOrganisation("NewCorp");
        newCustomer.setCity("NewCity");
        newCustomer.setIndustry("Innovation");

        Customer saved = customerService.save(newCustomer);

        assertThat(saved).isNotNull();
        assertThat(saved.getOrganisation()).isEqualTo("NewCorp");
        verify(kafkaTemplate).send(eq("cms"), contains("SAVE CUSTOMER:"));
    }

    @Test
    void whenUpdate_thenCustomerUpdated() {
        savedCustomer.setOrganisation("Updated Organisation");

        Customer updated = customerService.update(savedCustomer.getCustomerId(), savedCustomer);

        assertThat(updated.getOrganisation()).isEqualTo("Updated Organisation");
        verify(kafkaTemplate).send(eq("cms"), contains("UPDATE CUSTOMER:"));
    }

    @Test
    void whenDelete_thenCustomerDeleted() {
        customerService.delete(savedCustomer.getCustomerId());

        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getCustomerId());

        assertThat(deletedCustomer).isEmpty();
        verify(kafkaTemplate).send(eq("cms"), eq("DELETE CUSTOMER BY ID: " + savedCustomer.getCustomerId()));
    }
}