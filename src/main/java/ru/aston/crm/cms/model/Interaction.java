package ru.aston.crm.cms.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "interactions")
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interaction_id;
    private Long customer_id;
    private Long contact_id;
    private Date date;
    private String type;
    private String notes;

    public Long getInteraction_id() {
        return interaction_id;
    }

    public void setInteraction_id(Long interaction_id) {
        this.interaction_id = interaction_id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Long getContact_id() {
        return contact_id;
    }

    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
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
}
