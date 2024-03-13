package ru.aston.crm.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.crm.cms.model.Interaction;
import ru.aston.crm.cms.repository.InteractionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InteractionServiceImpl implements InteractionService {
    private final InteractionRepository interactionRepository;

    @Autowired
    public InteractionServiceImpl(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Override
    public List<Interaction> findAll() {
        return interactionRepository.findAll();
    }

    @Override
    public Interaction findById(int id) {
        return interactionRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Interaction not found for the id " + id));
    }

    @Override
    public List<Interaction> findByCustomerId(int customerId) {
        return interactionRepository.findByCustomerId(customerId);
    }

    @Override
    public void save(Interaction interaction) {
        interactionRepository.save(interaction);
    }

    @Override
    public void update(int id, Interaction updatedInteraction) {
        interactionRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Interaction not found for the id " + id));
        updatedInteraction.setContactId(id);
        interactionRepository.save(updatedInteraction);
    }

    @Override
    public void delete(int id) {
        interactionRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("Interaction not found for the id " + id));
        interactionRepository.deleteById(id);
    }
}
