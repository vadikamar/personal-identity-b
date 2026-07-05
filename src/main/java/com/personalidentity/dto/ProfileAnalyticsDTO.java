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
public class ProfileAnalyticsDTO {
    private String profileId;
    private String displayName;
    private Long profileViews;
    private Long uniqueVisitors;
    private Long linkClicks;
    private List<ProfileLinkMetricDTO> topLinks;
}
