package ru.aston.crm.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.ContactInfo;
import ru.aston.crm.cms.repository.ContactInfoRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;

    @Autowired
    public ContactInfoServiceImpl(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }

    @Override
    public List<ContactInfo> findAll() {
        return contactInfoRepository.findAll();
    }

    @Override
    public ContactInfo findById(int id) {
        return contactInfoRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("ContactInfo not found for the id " + id));
    }

    @Override
    public List<ContactInfo> findByCustomerId(int customerId) {
        return contactInfoRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public void save(ContactInfo newContactInfo) {
        contactInfoRepository.save(newContactInfo);
    }

    @Override
    @Transactional
    public void update(int id, ContactInfo updatedContactInfo) {
        contactInfoRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("ContactInfo not found for the id " + id));
        updatedContactInfo.setContactId(id);
        contactInfoRepository.save(updatedContactInfo);
    }

    @Override
    @Transactional
    public void delete(int id) {
        contactInfoRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("ContactInfo not found for the id " + id));
        contactInfoRepository.deleteById(id);
    }
}
