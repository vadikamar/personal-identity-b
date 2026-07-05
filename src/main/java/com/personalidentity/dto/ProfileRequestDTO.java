package com.personalidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDTO {
    private String displayName;
    private String profileType;
    private String headline;
    private String userName;//can't be updated once created
    private String bio;
    private String theme;
    private boolean active;
    private List<String> interests;
    private List<LinkDTO> links;
}
