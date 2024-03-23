package ru.aston.crm.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.ContactInfo;
import ru.aston.crm.cms.repository.ContactInfoRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ContactInfoServiceImpl(ContactInfoRepository contactInfoRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.contactInfoRepository = contactInfoRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<ContactInfo> findAll() {
        kafkaTemplate.send("cms", "FIND ALL CONTACT INFOS");
        return contactInfoRepository.findAll();
    }

    @Override
    public ContactInfo findById(int id) {
        kafkaTemplate.send("cms", "FIND CONTACT INFO BY ID: " + id);
        return contactInfoRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("ContactInfo not found for the id " + id));
    }

    @Override
    public List<ContactInfo> findByCustomerId(int customerId) {
        kafkaTemplate.send("cms", "FIND CONTACT INFOS BY CUSTOMER ID: " + customerId);
        return contactInfoRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public ContactInfo save(ContactInfo newContactInfo) {
        kafkaTemplate.send("cms", "SAVE CONTACT INFO: " +
                newContactInfo.getContactId() + " " + newContactInfo.getCustomerId() + " " +
                newContactInfo.getName() + " " + newContactInfo.getType() + " " +
                newContactInfo.getDetails() + " " + newContactInfo.isPreferred());
        contactInfoRepository.save(newContactInfo);
        return newContactInfo;
    }

    @Override
    @Transactional
    public ContactInfo update(int id, ContactInfo updatedContactInfo) {
        contactInfoRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("ContactInfo not found for the id " + id));
        kafkaTemplate.send("cms", "UPDATE CONTACT INFO: " +
                updatedContactInfo.getContactId() + " " + updatedContactInfo.getCustomerId() + " " +
                updatedContactInfo.getName() + " " + updatedContactInfo.getType() + " " +
                updatedContactInfo.getDetails() + " " + updatedContactInfo.isPreferred());
        updatedContactInfo.setContactId(id);
        contactInfoRepository.save(updatedContactInfo);
        return updatedContactInfo;
    }

    @Override
    @Transactional
    public void delete(int id) {
        contactInfoRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("ContactInfo not found for the id " + id));
        kafkaTemplate.send("cms", "DELETE CONTACT INFO BY ID: " + id);
        contactInfoRepository.deleteById(id);
    }
}
