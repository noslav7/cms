package ru.aston.crm.cms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.ContactInfo;
import ru.aston.crm.cms.repository.ContactInfoRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"cms"})
@Transactional
public class ContactInfoServiceIntegrationTest {
    @Autowired
    private ContactInfoService contactInfoService;
    @MockBean
    private ContactInfoRepository contactInfoRepository;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testFindAll() {
        when(contactInfoRepository.findAll()).thenReturn(List.of(new ContactInfo()));
        var contactInfos = contactInfoService.findAll();
        assertFalse(contactInfos.isEmpty());
        verify(kafkaTemplate).send("cms", "FIND ALL CONTACT INFOS");
    }

    @Test
    public void testFindById() {
        int id = 1;
        when(contactInfoRepository.findById(id)).thenReturn(Optional.of(new ContactInfo()));
        var contactInfo = contactInfoService.findById(id);
        assertNotNull(contactInfo);
        verify(kafkaTemplate).send("cms", "FIND CONTACT INFO BY ID: " + id);
    }

    @Test
    public void testFindByCustomerId() {
        int customerId = 1;
        when(contactInfoRepository.findByCustomerId(customerId)).thenReturn(List.of(new ContactInfo()));
        var contactInfos = contactInfoService.findByCustomerId(customerId);
        assertFalse(contactInfos.isEmpty());
        verify(kafkaTemplate).send("cms", "FIND CONTACT INFO BY CUSTOMER ID: " + customerId);
    }

    @Test
    public void testSave() {
        ContactInfo contactInfo = new ContactInfo();
        when(contactInfoRepository.save(any(ContactInfo.class))).thenReturn(contactInfo);
        var savedContactInfo = contactInfoService.save(contactInfo);
        assertNotNull(savedContactInfo);
        verify(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    public void testUpdate() {
        int id = 1;
        ContactInfo updatedContactInfo = new ContactInfo();
        when(contactInfoRepository.findById(id)).thenReturn(Optional.of(new ContactInfo()));
        when(contactInfoRepository.save(any(ContactInfo.class))).thenReturn(updatedContactInfo);
        var result = contactInfoService.update(id, updatedContactInfo);
        assertNotNull(result);
        verify(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    public void testDelete() {
        int id = 1;
        when(contactInfoRepository.findById(id)).thenReturn(Optional.of(new ContactInfo()));
        contactInfoService.delete(id);
        verify(contactInfoRepository).deleteById(id);
        verify(kafkaTemplate).send("cms", "DELETE CONTACT INFO BY ID: " + id);
    }
}
