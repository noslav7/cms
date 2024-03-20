package ru.aston.crm.cms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.aston.crm.cms.model.Customer;
import ru.aston.crm.cms.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new CustomerController(customerService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createCustomerTest() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setOrganisation("Transneft");
        customer.setCity("Moscow");
        customer.setIndustry("Oil transportation");
        Customer createdCustomer = new Customer();
        customer.setCustomerId(1);
        customer.setOrganisation("Transneft");
        customer.setCity("Moscow");
        customer.setIndustry("Oil transportation");
        when(customerService.save(any(Customer.class))).thenReturn(createdCustomer);

        mockMvc.perform(post("/cms/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdCustomer)));
    }

    @Test
    void getCustomerByIdTest() throws Exception {
        int id = 1;
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setOrganisation("Sakhalin Energo");
        customer.setCity("Sakhalin");
        customer.setIndustry("Electricity generation");
        when(customerService.findById(id)).thenReturn(customer);

        mockMvc.perform(get("/cms/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void getByIndustryNameTest() throws Exception {
        String industry = "Software";
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setOrganisation("Aston");
        customer.setCity("Saint-Petersburg");
        customer.setIndustry("Software");
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerService.findByIndustry(industry)).thenReturn(customers);

        mockMvc.perform(get("/cms/customers/industry/{industry}", industry))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void getAllCustomersTest() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setOrganisation("Naumen");
        customer.setCity("Moscow");
        customer.setIndustry("Software");
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/cms/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void updateCustomerTest() throws Exception {
        int id = 1;
        Customer customerDetails = new Customer(); // Prepare your Customer object with updates
        customerDetails.setCustomerId(5);
        customerDetails.setOrganisation("Vector");
        customerDetails.setCity("Yekaterinburg");
        customerDetails.setIndustry("Electric components production");
        Customer updatedCustomer = new Customer();
        customerDetails.setCustomerId(1);
        customerDetails.setOrganisation("Vector");
        customerDetails.setCity("Yekaterinburg");
        customerDetails.setIndustry("Electric components production");
        when(customerService.update(eq(id), any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/cms/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDetails)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedCustomer)));
    }

    @Test
    void deleteCustomerTest() throws Exception {
        int id = 1;
        doNothing().when(customerService).delete(id);

        mockMvc.perform(delete("/cms/customers/{id}", id))
                .andExpect(status().isNoContent());
    }
}