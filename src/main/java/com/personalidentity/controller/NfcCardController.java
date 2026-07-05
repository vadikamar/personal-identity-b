package com.personalidentity.controller;

import com.personalidentity.dto.ApiResponseDTO;
import com.personalidentity.dto.NfcCardRequestDTO;
import com.personalidentity.entity.NfcCard;
import com.personalidentity.service.NfcCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/cards")
public class NfcCardController {
    private final NfcCardService nfcCardService;
    private final Logger log = Logger.getLogger(ProfileController.class.getName());

    public NfcCardController(NfcCardService nfcCardService) {
        this.nfcCardService = nfcCardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<NfcCard>>> getCards() {
        log.info("NfcCardController getCards");
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Cards fetched", UUID.randomUUID().toString(), nfcCardService.getAllCards()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<NfcCard>> getActiveCard() {
        log.info("NfcCardController getActiveCard");
        return nfcCardService.getActiveCard()
                .map(card -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Active card fetched", UUID.randomUUID().toString(), card)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<NfcCard>> getCard(@PathVariable String id) {
        log.info("NfcCardController getCard");
        return nfcCardService.getCardById(id)
                .map(card -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Card fetched", UUID.randomUUID().toString(), card)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resolve/{cardId}")
    public ResponseEntity<ApiResponseDTO<NfcCard>> resolveCard(@PathVariable String cardId) {
        log.info("NfcCardController resolveCard");
        return nfcCardService.getCardByCardId(cardId)
                .map(card -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Card resolved", UUID.randomUUID().toString(), card)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<NfcCard>> createCard(@RequestBody NfcCardRequestDTO request) {
        log.info("NfcCardController createCard");
        NfcCard created = nfcCardService.createCard(request);
        log.info("NfcCardController createCard created");
        return ResponseEntity.ok(new ApiResponseDTO<>(201, "Card created", UUID.randomUUID().toString(), created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<NfcCard>> updateCard(@PathVariable String id, @RequestBody NfcCardRequestDTO request) {
        log.info("NfcCardController updateCard");
        return nfcCardService.updateCard(id, request)
                .map(card -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Card updated", UUID.randomUUID().toString(), card)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCard(@PathVariable String id) {
        log.info("NfcCardController deleteCard");
        nfcCardService.deleteCard(id);
        log.info("NfcCardController deleteCard deleted");
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Card deleted", UUID.randomUUID().toString(), null));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<NfcCard>> deactivateCard(@PathVariable String id) {
        log.info("NfcCardController deactivateCard");
        return nfcCardService.deactivateCard(id)
                .map(card -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Card deactivated", UUID.randomUUID().toString(), card)))
                .orElse(ResponseEntity.notFound().build());
    }
}
