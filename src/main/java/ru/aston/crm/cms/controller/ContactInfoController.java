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
import ru.aston.crm.cms.model.ContactInfo;
import ru.aston.crm.cms.service.ContactInfoService;

import java.util.List;

@RestController
@RequestMapping("/cms/contacts")
@Tag(name = "Contact information controller",
        description = "Controller for customers contact information")
public class ContactInfoController {
    private final ContactInfoService contactInfoService;

    @Autowired
    public ContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    @Operation(summary = "Adds new contact information object",
            description = "Provide all necessary fields for creating new contact information object!",
            responses = {
                    @ApiResponse(description = "ContactInfo created", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ContactInfo.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @PostMapping
    public ResponseEntity<ContactInfo> createContactInfo(
            @Parameter(description = "Contact information object to be stored in a database table", required = true)
            @RequestBody ContactInfo contactInfo) {
        ContactInfo createdContactInfo = contactInfoService.save(contactInfo);
        return new ResponseEntity<>(createdContactInfo, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get contact information by Id",
            responses = {
                    @ApiResponse(description = "ContactInfo retrieved", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ContactInfo.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping("/{id}")
    public ResponseEntity<ContactInfo> getContactInfoById(
            @Parameter(description = "Contact information ID to retrieve", required = true)
            @PathVariable int id) {
        ContactInfo contactInfo = contactInfoService.findById(id);
        return ResponseEntity.ok(contactInfo);
    }

    @Operation(
            summary = "Get ContactInfo objects by customer Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of ContactInfo objects",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactInfo.class)))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping("/{customer_id}")
    public ResponseEntity<List<ContactInfo>> getByCustomerId(
            @Parameter(description = "Customer ID", required = true) @PathVariable int customer_id) {
        List<ContactInfo> contactInfos = contactInfoService.findByCustomerId(customer_id);
        return ResponseEntity.ok(contactInfos);
    }

    @Operation(
            summary = "Get all ContactInfo objects",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of all ContactInfo",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactInfo.class)))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping
    public ResponseEntity<List<ContactInfo>> getAllContactInfos() {
        List<ContactInfo> contactInfos = contactInfoService.findAll();
        return ResponseEntity.ok(contactInfos);
    }

    @Operation(
            summary = "Updates an existing ContactInfo by Id",
            responses = {
                    @ApiResponse(description = "ContactInfo updated", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ContactInfo.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @PutMapping("/{id}")
    public ResponseEntity<ContactInfo> updateContactInfo(
            @Parameter(description = "ID of a contact information object to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated contact information", required = true) @RequestBody ContactInfo info) {
        ContactInfo contactInfo = contactInfoService.update(id, info);
        return ResponseEntity.ok(contactInfo);
    }

    @Operation(summary = "Deletes an existing ContactInfo object by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInfo(
            @Parameter(description = "ID of contact information to be deleted", required = true) @PathVariable int id) {
        contactInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
