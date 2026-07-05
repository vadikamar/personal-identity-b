package com.personalidentity.service;

import com.personalidentity.dto.NfcCardRequestDTO;
import com.personalidentity.entity.NfcCard;
import com.personalidentity.repositary.NfcCardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NfcCardService {
    private final NfcCardRepository nfcCardRepository;

    public NfcCardService(NfcCardRepository nfcCardRepository) {
        this.nfcCardRepository = nfcCardRepository;
    }

    public List<NfcCard> getAllCards() {
        return nfcCardRepository.findAll();
    }

    public Optional<NfcCard> getCardById(String id) {
        return nfcCardRepository.findById(id);
    }

    public Optional<NfcCard> getActiveCard() {
        return nfcCardRepository.findFirstByActiveTrue();
    }

    public Optional<NfcCard> getCardByCardId(String cardId) {
        return nfcCardRepository.findByCardId(cardId);
    }

    public NfcCard createCard(NfcCardRequestDTO request) {
        NfcCard card = NfcCard.builder()
                .cardLabel(request.getCardLabel())
                .cardId(UUID.randomUUID().toString())
                .status(request.getStatus())
                .active(request.isActive())
                .assignedProfileId(request.getAssignedProfile())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return nfcCardRepository.save(card);
    }

    public Optional<NfcCard> updateCard(String id, NfcCardRequestDTO request) {
        return nfcCardRepository.findById(id).map(existing -> {
            existing.setCardLabel(request.getCardLabel());
            existing.setCardId(existing.getCardId());
            existing.setStatus(request.getStatus());
            existing.setActive(request.isActive());
            existing.setAssignedProfileId(request.getAssignedProfile());
            existing.setUpdatedAt(Instant.now());
            return nfcCardRepository.save(existing);
        });
    }

    public void deleteCard(String id) {
        nfcCardRepository.deleteById(id);
    }

    public Optional<NfcCard> deactivateCard(String id) {
        return nfcCardRepository.findById(id).map(existing -> {
            existing.setActive(false);
            existing.setUpdatedAt(Instant.now());
            return nfcCardRepository.save(existing);
        });
    }
}
