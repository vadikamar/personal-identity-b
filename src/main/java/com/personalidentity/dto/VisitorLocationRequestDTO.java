package com.personalidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorLocationRequestDTO {
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;
    private String address;
}
