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
@Document(collection = "profile_access_requests")
public class ProfileAccessRequest {
    @Id
    private String id;
    private String ownerProfileId;
    private String ownerUsername;
    private String requesterUsername;
    private String requestedProfileId;
    private String message;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
