package com.personalidentity.repositary;

import com.personalidentity.entity.NfcCard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NfcCardRepository extends MongoRepository<NfcCard, String> {
    Optional<NfcCard> findByCardId(String cardId);
    Optional<NfcCard> findFirstByActiveTrue();
    long countByActiveTrue();
}
