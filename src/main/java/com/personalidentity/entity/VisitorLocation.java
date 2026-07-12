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
public class VisitorLocation {
    private String id;
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;
    private String address;
    private Instant visitedAt;
}
