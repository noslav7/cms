package ru.aston.crm.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "interactions")
@Schema(description = "Information about particular interaction with the customer")
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interaction_id")
    @Schema(description = "The unique ID of the interaction")
    private int interactionId;
    @Column(name = "customer_id")
    @Schema(description = "The ID of the organisation (customer) with which the interaction took place")
    @JsonProperty("customer_id")
    private int customerId;
    @Column(name = "contact_id")
    @Schema(description = "The ID of the contact of the organisation (customer)")
    @JsonProperty("contact_id")
    private int contactId;
    @Schema(description = "Date of the interaction")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private Date date;
    @Schema(description = "E.g. phone or email communication, purchase application etc.")
    @JsonProperty("type")
    private String type;
    @Schema(description = "Details of the interaction")
    @JsonProperty("notes")
    private String notes;

    public int getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(int interactionId) {
        this.interactionId = interactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "interactionId=" + interactionId +
                ", customerId=" + customerId +
                ", contactId=" + contactId +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
