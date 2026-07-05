package com.personalidentity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "nfc_cards")
public class NfcCard {
    @Id
    private String id;
    private String cardLabel;
    private String cardId;
    private String status;
    private boolean active;
    private String assignedProfileId;
    private Instant createdAt;
    private Instant updatedAt;
}
