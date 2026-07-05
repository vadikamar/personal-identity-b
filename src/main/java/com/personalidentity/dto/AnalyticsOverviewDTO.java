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
public class AnalyticsOverviewDTO {
    private Long totalProfiles;
    private Long activeProfiles;
    private Long totalCards;
    private Long activeCards;
    private Long uniqueVisitors;
    private Long linkClicks;
    private List<ProfileLinkMetricDTO> topLinks;
}
