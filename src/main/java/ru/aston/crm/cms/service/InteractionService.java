package ru.aston.crm.cms.service;

import ru.aston.crm.cms.model.Interaction;

import java.util.List;

public interface InteractionService {
    List<Interaction> findAll();
    Interaction findById(int id);
    List<Interaction> findByCustomerId(int customerId);
    Interaction save(Interaction interaction);
    Interaction update(int id, Interaction updatedInteraction);
    void delete(int id);
}
