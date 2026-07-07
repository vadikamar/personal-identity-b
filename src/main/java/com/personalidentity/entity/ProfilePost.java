package com.personalidentity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePost {
    private String id;
    private String description;
    private String photoUrl;
    private Instant createdAt;
}
