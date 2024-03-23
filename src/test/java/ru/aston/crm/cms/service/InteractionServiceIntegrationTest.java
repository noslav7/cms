package ru.aston.crm.cms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.Interaction;
import ru.aston.crm.cms.repository.InteractionRepository;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"cms"})
@Transactional
public class InteractionServiceIntegrationTest {

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private InteractionRepository interactionRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private Interaction savedInteraction;

    @BeforeEach
    void setUp() {
        Interaction interaction = new Interaction();
        interaction.setCustomerId(1);
        interaction.setContactId(1);
        interaction.setDate(new Date());
        interaction.setType("Email");
        interaction.setNotes("Test note");

        savedInteraction = interactionRepository.save(interaction);
    }

    @Test
    void whenFindAll_thenAllInteractionsReturned() {
        List<Interaction> interactions = interactionService.findAll();

        assertThat(interactions).isNotEmpty();
        verify(kafkaTemplate).send("cms", "FIND ALL INTERACTIONS");
    }

    @Test
    void whenFindById_thenCorrectInteractionReturned() {
        Interaction interaction = interactionService.findById(savedInteraction.getInteractionId());

        assertThat(interaction).isNotNull();
        assertThat(interaction.getInteractionId()).isEqualTo(savedInteraction.getInteractionId());
        verify(kafkaTemplate).send("cms", "FIND INTERACTION BY ID: " + savedInteraction.getInteractionId());
    }

    @Test
    void whenFindByCustomerId_thenInteractionsByCustomerIdReturned() {
        List<Interaction> interactions = interactionService.findByCustomerId(savedInteraction.getCustomerId());

        assertThat(interactions).isNotEmpty();
        assertThat(interactions.get(0).getCustomerId()).isEqualTo(savedInteraction.getCustomerId());
        verify(kafkaTemplate).send("cms", "FIND INTERACTIONS BY CUSTOMER ID: " + savedInteraction.getCustomerId());
    }

    @Test
    void whenSave_thenInteractionSaved() {
        Interaction newInteraction = new Interaction();
        newInteraction.setCustomerId(2);
        newInteraction.setContactId(2);
        newInteraction.setDate(new Date());
        newInteraction.setType("Call");
        newInteraction.setNotes("Follow-up call");

        Interaction saved = interactionService.save(newInteraction);

        assertThat(saved.getCustomerId()).isEqualTo(2);
        verify(kafkaTemplate).send("cms", "SAVE INTERACTION: " + saved.getInteractionId() + " 2 2 " + saved.getDate() + " Call " + saved.getNotes());
    }

    @Test
    void whenUpdate_thenInteractionUpdated() {
        savedInteraction.setNotes("Updated note");

        Interaction updated = interactionService.update(savedInteraction.getInteractionId(), savedInteraction);

        assertThat(updated.getNotes()).isEqualTo("Updated note");
        verify(kafkaTemplate).send("cms", "UPDATE INTERACTION: " + updated.getInteractionId() + " " + updated.getCustomerId() + " " + updated.getContactId() + " " + updated.getDate() + " " + updated.getType() + " Updated note");
    }

    @Test
    void whenDelete_thenInteractionDeleted() {
        interactionService.delete(savedInteraction.getInteractionId());

        assertThat(interactionRepository.findById(savedInteraction.getInteractionId())).isEmpty();
        verify(kafkaTemplate).send("cms", "DELETE INTERACTION BY ID: " + savedInteraction.getInteractionId());
    }
}
