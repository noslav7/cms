package ru.aston.crm.cms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.ContactInfo;
import ru.aston.crm.cms.repository.ContactInfoRepository;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ContactInfoServiceIntegrationTest {
    @Autowired
    private ContactInfoService contactInfoService;
    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;
    private ContactInfo savedContactInfo;

    @BeforeEach
    void setUp() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setCustomerId(1);
        contactInfo.setName("John Doe");
        contactInfo.setType("Email");
        contactInfo.setDetails("john.doe@example.com");
        contactInfo.setPreferred(true);

        savedContactInfo = contactInfoRepository.save(contactInfo);
    }

    @Test
    void whenFindAll_thenAllContactInfosReturned() {
        List<ContactInfo> contactInfos = contactInfoService.findAll();

        assertThat(contactInfos).isNotEmpty();
        assertThat(contactInfos.size()).isGreaterThanOrEqualTo(1);
        verify(kafkaTemplate).send("cms", "FIND ALL CONTACT INFOS");
    }

    @Test
    void whenFindById_thenCorrectContactInfoReturned() {
        ContactInfo contactInfo = contactInfoService.findById(savedContactInfo.getContactId());

        assertThat(contactInfo).isNotNull();
        assertThat(contactInfo.getContactId()).isEqualTo(savedContactInfo.getContactId());
        verify(kafkaTemplate).send("cms", "FIND CONTACT INFO BY ID: " + savedContactInfo.getContactId());
    }

    @Test
    void whenFindByCustomerId_thenContactInfosByCustomerIdReturned() {
        List<ContactInfo> contactInfos = contactInfoService.findByCustomerId(savedContactInfo.getCustomerId());

        assertThat(contactInfos).isNotEmpty();
        assertThat(contactInfos.get(0).getCustomerId()).isEqualTo(savedContactInfo.getCustomerId());
        verify(kafkaTemplate).send("cms", "FIND CONTACT INFOS BY CUSTOMER ID: " + savedContactInfo.getCustomerId());
    }

    @Test
    void whenSave_thenContactInfoSaved() {
        ContactInfo newContactInfo = new ContactInfo();
        newContactInfo.setCustomerId(2);
        newContactInfo.setName("Jane Doe");
        newContactInfo.setType("Phone");
        newContactInfo.setDetails("555-1234");
        newContactInfo.setPreferred(false);

        ContactInfo saved = contactInfoService.save(newContactInfo);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Jane Doe");
        verify(kafkaTemplate).send(eq("cms"), argThat(argument ->
                argument.matches("SAVE CONTACT INFO: " + saved.getCustomerId() + " " +
                        Pattern.quote(saved.getName()) + " " + Pattern.quote(saved.getType()) + " " +
                        Pattern.quote(saved.getDetails()) + " " + saved.isPreferred())));
    }

    @Test
    void whenUpdate_thenContactInfoUpdated() {
        savedContactInfo.setName("Updated Name");

        ContactInfo updated = contactInfoService.update(savedContactInfo.getContactId(), savedContactInfo);

        assertThat(updated.getName()).isEqualTo("Updated Name");
        verify(kafkaTemplate).send("cms", "UPDATE CONTACT INFO: " +
                updated.getCustomerId() + " " + updated.getName() + " " + updated.getType() + " " +
                updated.getDetails() + " " + updated.isPreferred());
    }

    @Test
    void whenDelete_thenContactInfoDeleted() {
        contactInfoService.delete(savedContactInfo.getContactId());

        Exception exception = assertThrows(Exception.class, () -> contactInfoService
                .findById(savedContactInfo.getContactId()));

        assertThat(exception).isNotNull();
        verify(kafkaTemplate).send("cms", "DELETE CONTACT INFO BY ID: " + savedContactInfo.getContactId());
    }
}