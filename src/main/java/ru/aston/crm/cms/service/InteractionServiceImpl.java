package ru.aston.crm.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.Interaction;
import ru.aston.crm.cms.repository.InteractionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InteractionServiceImpl implements InteractionService {
    private final InteractionRepository interactionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public InteractionServiceImpl(InteractionRepository interactionRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.interactionRepository = interactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Interaction> findAll() {
        kafkaTemplate.send("cms", "FIND ALL INTERACTIONS");
        return interactionRepository.findAll();
    }

    @Override
    public Interaction findById(int id) {
        kafkaTemplate.send("cms", "FIND INTERACTION BY ID: " + id);
        return interactionRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Interaction not found for the id " + id));
    }

    @Override
    public List<Interaction> findByCustomerId(int customerId) {
        kafkaTemplate.send("cms", "FIND INTERACTIONS BY CUSTOMER ID: " + customerId);
        return interactionRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Interaction save(Interaction interaction) {
        Interaction savedInteraction = interactionRepository.save(interaction);
        kafkaTemplate.send("cms", "SAVE INTERACTION: " + interaction.getCustomerId() + " " +
                interaction.getContactId() + " " + interaction.getDate() + " " +
                interaction.getType() + " " + interaction.getNotes());
        return savedInteraction;
    }

    @Override
    @Transactional
    public Interaction update(int id, Interaction updatedInteraction) {
        interactionRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Interaction not found for the id " + id));
        kafkaTemplate.send("cms", "UPDATE INTERACTION: " + updatedInteraction.getCustomerId() + " " +
                updatedInteraction.getContactId() + " " + updatedInteraction.getDate() + " " +
                updatedInteraction.getType() + " " + updatedInteraction.getNotes());
        return interactionRepository.save(updatedInteraction);
    }

    @Override
    @Transactional
    public void delete(int id) {
        interactionRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Interaction not found for the id " + id));
        kafkaTemplate.send("cms", "DELETE INTERACTION BY ID: " + id);
        interactionRepository.deleteById(id);
    }
}
