package com.personalidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfcCardRequestDTO {
    private String cardLabel;
    private String status;
    private boolean active;
    private String assignedProfile;
}
