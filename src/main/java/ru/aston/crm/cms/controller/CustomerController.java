package ru.aston.crm.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.crm.cms.model.Customer;
import ru.aston.crm.cms.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/cms/customers")
@Tag(name = "CustomerController",
        description = "Controller for customers' objects")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Adds new contact information object",
            description = "Provide all necessary fields for creating new customer object!",
            responses = {
                    @ApiResponse(description = "Customer created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Customer.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Parameter(description = "A customer object to be stored in a database table", required = true)
            @RequestBody Customer customer) {
        Customer createdCustomer = customerService.save(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Customer by Id",
            responses = {
                    @ApiResponse(description = "Customer retrieved", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Customer.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @Parameter(description = "Customer ID to retrieve", required = true) @PathVariable int id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @Operation(
            summary = "Get customers by industry",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of Customers",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class)))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping("/{industry}")
    public ResponseEntity<List<Customer>> getByIndustryName(
            @Parameter(description = "Customer's industry", required = true) @PathVariable String industry) {
        List<Customer> customers = customerService.findByIndustry(industry);
        return ResponseEntity.ok(customers);
    }

    @Operation(
            summary = "Get all Customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of all Customers",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class)))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @Operation(
            summary = "Updates an existing Customer by Id",
            responses = {
                    @ApiResponse(description = "Customer updated", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Customer.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @Parameter(description = "ID of a customer to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated customer", required = true) @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.update(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "Deletes an existing Customer by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of a customer to be deleted", required = true) @PathVariable int id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
