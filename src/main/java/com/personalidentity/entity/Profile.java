package com.personalidentity.entity;

import com.personalidentity.dto.LinkDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @Id
    private String id;
    private String displayName;
    private String profileType;
    private String userName;
    private String headline;
    private String bio;
    private String theme;
    private String photoUrl;
    private boolean active;
    private List<String> interests;
    private List<LinkDTO> links;
    private Instant createdAt;
    private Instant updatedAt;
}
