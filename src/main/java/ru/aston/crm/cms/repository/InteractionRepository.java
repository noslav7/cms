package ru.aston.crm.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.aston.crm.cms.model.Interaction;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

    @Query("SELECT i FROM Interaction i WHERE i.customerId = ?1")
    List<Interaction> findByCustomerId(int customerId);
}
