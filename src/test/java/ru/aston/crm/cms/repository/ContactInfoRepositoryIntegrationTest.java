package ru.aston.crm.cms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.aston.crm.cms.model.ContactInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ContactInfoRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ContactInfoRepository contactInfoRepository;
    private ContactInfo savedContactInfo;

    @BeforeEach
    void setUp() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setCustomerId(1);
        contactInfo.setName("John Doe");
        contactInfo.setType("Email");
        contactInfo.setDetails("john.doe@example.com");
        contactInfo.setPreferred(true);

        savedContactInfo = entityManager.persistFlushFind(contactInfo);
    }

    @Test
    void whenFindByCustomerId_thenReturnContactInfos() {
        List<ContactInfo> foundContactInfos = contactInfoRepository.findByCustomerId(
                savedContactInfo.getCustomerId());

        assertThat(foundContactInfos).isNotEmpty();
        assertThat(foundContactInfos.get(0).getCustomerId()).isEqualTo(savedContactInfo.getCustomerId());
    }

    @Test
    void whenFindAll_thenReturnAllContactInfos() {
        List<ContactInfo> contactInfos = contactInfoRepository.findAll();

        assertThat(contactInfos).hasSize(1);
        assertThat(contactInfos.get(0).getContactId()).isEqualTo(savedContactInfo.getContactId());
    }

    @Test
    void whenFindById_thenReturnContactInfo() {
        ContactInfo foundContactInfo = contactInfoRepository.findById(
                savedContactInfo.getContactId()).orElse(null);

        assertThat(foundContactInfo).isNotNull();
        assertThat(foundContactInfo.getContactId()).isEqualTo(savedContactInfo.getContactId());
    }

    @Test
    void whenSave_thenPersistContactInfo() {
        ContactInfo newContactInfo = new ContactInfo();
        newContactInfo.setCustomerId(2); // Different customer
        newContactInfo.setName("Jane Doe");
        newContactInfo.setType("Mobile");
        newContactInfo.setDetails("+123456789");
        newContactInfo.setPreferred(false);

        ContactInfo saved = contactInfoRepository.save(newContactInfo);

        assertThat(saved.getContactId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Jane Doe");
    }

    @Test
    void whenUpdate_thenChangesArePersisted() {
        ContactInfo contactInfoToUpdate = entityManager.find(
                ContactInfo.class, savedContactInfo.getContactId());
        contactInfoToUpdate.setDetails("updated.email@example.com");

        contactInfoRepository.saveAndFlush(contactInfoToUpdate);

        ContactInfo updatedContactInfo = entityManager.find(
                ContactInfo.class, savedContactInfo.getContactId());

        assertThat(updatedContactInfo.getDetails()).isEqualTo("updated.email@example.com");
    }

    @Test
    void whenDeleteById_thenRemoveContactInfo() {
        contactInfoRepository.deleteById(savedContactInfo.getContactId());
        ContactInfo deletedContactInfo = entityManager.find(
                ContactInfo.class, savedContactInfo.getContactId());

        assertThat(deletedContactInfo).isNull();
    }
}