package it.interlogica.crm.repository;

import it.interlogica.crm.model.entity.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity, Long> {

    Optional<PhoneNumberEntity> findByOriginalNumber(String originalNumber);
}
