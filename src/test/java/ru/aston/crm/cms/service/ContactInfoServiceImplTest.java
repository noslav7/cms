package ru.aston.crm.cms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.aston.crm.cms.model.ContactInfo;
import ru.aston.crm.cms.repository.ContactInfoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactInfoServiceImplTest {
    @Mock
    private ContactInfoRepository contactInfoRepository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;

    @Test
    void whenFindAll_thenAllContactInfosReturned() {
        List<ContactInfo> expectedContactInfos = Arrays.asList(new ContactInfo(), new ContactInfo());
        when(contactInfoRepository.findAll()).thenReturn(expectedContactInfos);

        List<ContactInfo> actualContactInfos = contactInfoService.findAll();

        assertEquals(expectedContactInfos, actualContactInfos);
    }

    @Test
    void whenFindByIdValid_thenContactInfoReturned() {
        int id = 1;
        ContactInfo expectedContactInfo = new ContactInfo();
        when(contactInfoRepository.findById(id)).thenReturn(Optional.of(expectedContactInfo));

        ContactInfo actualContactInfo = contactInfoService.findById(id);

        assertEquals(expectedContactInfo, actualContactInfo);
    }

    @Test
    void whenFindByIdInvalid_thenThrowException() {
        int invalidId = 999;
        when(contactInfoRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> contactInfoService.findById(invalidId));

        assertTrue(exception.getMessage().contains("ContactInfo not found for the id " + invalidId));
    }

    @Test
    void whenFindByCustomerId_thenContactInfosReturned() {
        int customerId = 1;
        List<ContactInfo> expectedContactInfos = Arrays.asList(new ContactInfo(), new ContactInfo());
        when(contactInfoRepository.findByCustomerId(customerId)).thenReturn(expectedContactInfos);

        List<ContactInfo> actualContactInfos = contactInfoService.findByCustomerId(customerId);

        assertEquals(expectedContactInfos, actualContactInfos);
    }

    @Test
    void whenSave_thenContactInfoSaved() {
        ContactInfo newContactInfo = new ContactInfo();
        when(contactInfoRepository.save(any(ContactInfo.class))).thenReturn(newContactInfo);

        ContactInfo savedContactInfo = contactInfoService.save(newContactInfo);

        assertEquals(newContactInfo, savedContactInfo);
    }

    @Test
    void whenUpdateValid_thenContactInfoUpdated() {
        int id = 1;
        ContactInfo existingContactInfo = new ContactInfo();
        ContactInfo updatedContactInfo = new ContactInfo();
        when(contactInfoRepository.findById(id)).thenReturn(Optional.of(existingContactInfo));
        when(contactInfoRepository.save(any(ContactInfo.class))).thenReturn(updatedContactInfo);

        ContactInfo result = contactInfoService.update(id, updatedContactInfo);

        assertEquals(updatedContactInfo, result);
    }

    @Test
    void whenUpdateInvalid_thenThrowException() {
        int invalidId = 999;
        ContactInfo updatedContactInfo = new ContactInfo();
        when(contactInfoRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> contactInfoService.update(invalidId, updatedContactInfo));

        assertTrue(exception.getMessage().contains("ContactInfo not found for the id " + invalidId));
    }

    @Test
    void whenDeleteValid_thenContactInfoDeleted() {
        int id = 1;
        when(contactInfoRepository.findById(id)).thenReturn(Optional.of(new ContactInfo()));

        contactInfoService.delete(id);

        verify(contactInfoRepository).deleteById(id);
    }

    @Test
    void whenDeleteInvalid_thenThrowException() {
        int invalidId = 999;
        when(contactInfoRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> contactInfoService.delete(invalidId));

        assertTrue(exception.getMessage().contains("ContactInfo not found for the id " + invalidId));
        verify(contactInfoRepository, never()).deleteById(invalidId);
    }
}
