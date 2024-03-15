package ru.aston.crm.cms.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "contacts_info")
@Schema(description = "Information about a contact")
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    @Schema(description = "The unique ID of the contact")
    private int contactId;
    @Column(name = "customer_id")
    @Schema(description = "The ID of the contact's organisation")
    private int customerId;
    @Schema(description = "Name of the contact (preferably full name)")
    private String name;
    @Schema(description = "Type of the contact information (tel.№, mobile№, email et. al.")
    private String type;
    @Schema(description = "Contact information")
    private String details;
    @Schema(description = "Is this the preferred mean of communication with the customer?")
    private boolean preferred;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}
