package ru.aston.crm.cms.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.aston.crm.cms.model.Customer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setOrganisation("Test Organisation");
        customer.setCity("Test City");
        customer.setIndustry("Technology");
        savedCustomer = entityManager.persistFlushFind(customer);
    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void whenFindByIndustry_thenReturnListOfCustomers() {
        List<Customer> customers = customerRepository.findByIndustry("Technology");

        assertThat(customers).isNotEmpty();
        assertThat(customers.get(0).getOrganisation()).isEqualTo(savedCustomer.getOrganisation());
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        Customer foundCustomer = customerRepository.findById(savedCustomer.getCustomerId())
                .orElse(null);

        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
    }

    @Test
    void whenSave_thenPersistCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setOrganisation("New Organisation");
        newCustomer.setCity("New City");
        newCustomer.setIndustry("Finance");

        Customer saved = customerRepository.save(newCustomer);

        assertThat(saved.getCustomerId()).isNotNull();
        assertThat(saved.getCity()).isEqualTo("New City");
    }

    @Test
    void whenDeleteById_thenRemoveCustomer() {
        customerRepository.deleteById(savedCustomer.getCustomerId());
        Customer deletedCustomer = customerRepository.findById(savedCustomer.getCustomerId())
                .orElse(null);

        assertThat(deletedCustomer).isNull();
    }

    @Test
    void whenFindAll_thenReturnAllCustomers() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setOrganisation("Another Organisation");
        anotherCustomer.setCity("Another City");
        anotherCustomer.setIndustry("Healthcare");
        entityManager.persistAndFlush(anotherCustomer);

        List<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(2);
        assertThat(customers).extracting(Customer::getOrganisation).contains(
                savedCustomer.getOrganisation(), "Another Organisation");
    }

    @Test
    void whenUpdateCustomer_thenChangesArePersisted() {
        String updatedCity = "Updated City";
        String updatedIndustry = "Updated Industry";

        Customer customerToUpdate = entityManager.find(Customer.class, savedCustomer.getCustomerId());
        customerToUpdate.setCity(updatedCity);
        customerToUpdate.setIndustry(updatedIndustry);

        customerRepository.saveAndFlush(customerToUpdate);

        Customer updatedCustomer = entityManager.find(Customer.class, savedCustomer.getCustomerId());

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getCity()).isEqualTo(updatedCity);
        assertThat(updatedCustomer.getIndustry()).isEqualTo(updatedIndustry);
    }
}