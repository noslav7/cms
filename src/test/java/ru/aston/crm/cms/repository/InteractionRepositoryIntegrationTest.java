package ru.aston.crm.cms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.aston.crm.cms.model.Interaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class InteractionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InteractionRepository interactionRepository;

    private Interaction savedInteraction;

    @BeforeEach
    void setUp() {
        Interaction interaction = new Interaction();
        interaction.setCustomerId(1);
        interaction.setContactId(2);
        interaction.setDate(new Date());
        interaction.setType("Meeting");
        interaction.setNotes("Discussion about project scope.");

        savedInteraction = entityManager.persistAndFlush(interaction);
    }

    @Test
    void whenFindByCustomerId_thenReturnInteractions() {
        List<Interaction> foundInteractions = interactionRepository.findByCustomerId(
                savedInteraction.getCustomerId());

        assertThat(foundInteractions).isNotEmpty();
        assertThat(foundInteractions.get(0).getCustomerId()).isEqualTo(savedInteraction.getCustomerId());
    }

    @Test
    void whenFindAll_thenReturnAllInteractions() {
        List<Interaction> interactions = interactionRepository.findAll();

        assertThat(interactions).hasSize(1);
        assertThat(interactions.get(0).getInteractionId()).isEqualTo(savedInteraction.getInteractionId());
    }

    @Test
    void whenSave_thenPersistInteraction() {
        Interaction newInteraction = new Interaction();
        newInteraction.setCustomerId(3);
        newInteraction.setContactId(4);
        newInteraction.setDate(new Date());
        newInteraction.setType("Email");
        newInteraction.setNotes("Follow-up email regarding meeting.");

        Interaction saved = interactionRepository.save(newInteraction);

        assertThat(saved.getInteractionId()).isNotNull();
        assertThat(saved.getType()).isEqualTo("Email");
    }

    @Test
    void whenUpdate_thenChangesArePersisted() {
        Interaction interactionToUpdate = entityManager.find(Interaction.class, savedInteraction.getInteractionId());
        interactionToUpdate.setNotes("Updated notes after second meeting.");

        interactionRepository.saveAndFlush(interactionToUpdate);

        Interaction updatedInteraction = entityManager.find(Interaction.class, savedInteraction.getInteractionId());
        assertThat(updatedInteraction.getNotes()).isEqualTo("Updated notes after second meeting.");
    }

    @Test
    void whenDeleteById_thenRemoveInteraction() {
        interactionRepository.deleteById(savedInteraction.getInteractionId());
        Interaction deletedInteraction = entityManager.find(Interaction.class, savedInteraction.getInteractionId());
        assertThat(deletedInteraction).isNull();
    }

    @Test
    void whenFindById_thenReturnInteraction() {
        Interaction interaction = new Interaction();
        interaction.setCustomerId(1);
        interaction.setContactId(2);
        interaction.setDate(new Date());
        interaction.setType("Meeting");
        interaction.setNotes("Initial discussion about project requirements.");

        Interaction savedInteraction = entityManager.persistFlushFind(interaction);

        Optional<Interaction> foundInteractionOpt = interactionRepository
                .findById(savedInteraction.getInteractionId());

        assertThat(foundInteractionOpt).isPresent();
        foundInteractionOpt.ifPresent(foundInteraction -> {
            assertThat(foundInteraction.getInteractionId()).isEqualTo(savedInteraction.getInteractionId());
            assertThat(foundInteraction.getCustomerId()).isEqualTo(interaction.getCustomerId());
            assertThat(foundInteraction.getContactId()).isEqualTo(interaction.getContactId());
            assertThat(foundInteraction.getType()).isEqualTo(interaction.getType());
            assertThat(foundInteraction.getNotes()).isEqualTo(interaction.getNotes());
        });
    }
}