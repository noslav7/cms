package ru.aston.crm.cms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.aston.crm.cms.model.Interaction;
import ru.aston.crm.cms.repository.InteractionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractionServiceImplTest {
    @Mock
    private InteractionRepository interactionRepository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private InteractionServiceImpl interactionService;

    @Test
    void whenFindAll_thenInteractionsReturned() {
        List<Interaction> expectedInteractions = Arrays.asList(new Interaction(), new Interaction());
        when(interactionRepository.findAll()).thenReturn(expectedInteractions);

        List<Interaction> actualInteractions = interactionService.findAll();

        assertEquals(expectedInteractions, actualInteractions);
    }

    @Test
    void whenFindByIdWithValidId_thenInteractionReturned() {
        int interactionId = 1;
        Interaction expectedInteraction = new Interaction();
        when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(expectedInteraction));

        Interaction actualInteraction = interactionService.findById(interactionId);

        assertEquals(expectedInteraction, actualInteraction);
    }

    @Test
    void whenFindByIdWithInvalidId_thenThrowException() {
        int interactionId = 999;
        when(interactionRepository.findById(interactionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> interactionService.findById(interactionId));

        assertEquals("Interaction not found for the id " + interactionId, exception.getMessage());
    }

    @Test
    void whenFindByCustomerId_thenInteractionsReturned() {
        int customerId = 1;
        List<Interaction> expectedInteractions = Arrays.asList(new Interaction(), new Interaction());
        when(interactionRepository.findByCustomerId(customerId)).thenReturn(expectedInteractions);

        List<Interaction> actualInteractions = interactionService.findByCustomerId(customerId);

        assertEquals(expectedInteractions, actualInteractions);
    }

    @Test
    void whenSave_thenInteractionSaved() {
        Interaction interaction = new Interaction();
        when(interactionRepository.save(any(Interaction.class))).thenReturn(interaction);

        Interaction savedInteraction = interactionService.save(interaction);

        assertEquals(interaction, savedInteraction);
    }

    @Test
    void whenUpdateValidInteraction_thenInteractionUpdated() {
        int interactionId = 1;
        Interaction existingInteraction = new Interaction();
        Interaction updatedInteraction = new Interaction();
        updatedInteraction.setContactId(interactionId);

        when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(existingInteraction));
        when(interactionRepository.save(any(Interaction.class))).thenReturn(updatedInteraction);

        Interaction result = interactionService.update(interactionId, updatedInteraction);

        assertEquals(updatedInteraction.getContactId(), result.getContactId());
    }

    @Test
    void whenUpdateNonExistentInteraction_thenThrowException() {
        int nonExistentId = 999;
        Interaction interactionToUpdate = new Interaction();
        when(interactionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> interactionService.update(nonExistentId, interactionToUpdate));

        assertTrue(exception.getMessage().contains("Interaction not found for the id " + nonExistentId));
    }

    @Test
    void whenDeleteValidInteraction_thenInteractionDeleted() {
        int interactionId = 1;

        when(interactionRepository.findById(interactionId)).thenReturn(Optional.of(new Interaction()));

        interactionService.delete(interactionId);

        verify(interactionRepository).deleteById(interactionId);
    }

    @Test
    void whenDeleteNonExistentInteraction_thenThrowException() {
        int nonExistentId = 999;
        when(interactionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> interactionService.delete(nonExistentId));

        assertTrue(exception.getMessage().contains("Interaction not found for the id " + nonExistentId));
    }
}