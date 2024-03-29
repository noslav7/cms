package ru.aston.crm.cms.service;

import ru.aston.crm.cms.model.ContactInfo;

import java.util.List;

public interface ContactInfoService {

    List<ContactInfo> findAll();
    ContactInfo findById(int id);
    List<ContactInfo> findByCustomerId(int customerId);
    ContactInfo save(ContactInfo newContactInfo);
    ContactInfo update(int id, ContactInfo updatedContactInfo);
    void delete(int id);
}
