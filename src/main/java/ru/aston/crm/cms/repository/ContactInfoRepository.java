package ru.aston.crm.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.aston.crm.cms.model.ContactInfo;

import java.util.List;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    @Query("SELECT c FROM ContactInfo c WHERE c.customer_id = ?")
    List<ContactInfo> findByCustomerId(Long customerId);
}
