package com.personalidentity.service;

import com.personalidentity.dto.AnalyticsOverviewDTO;
import com.personalidentity.dto.ProfileAnalyticsDTO;
import com.personalidentity.dto.ProfileLinkMetricDTO;
import com.personalidentity.entity.Profile;
import com.personalidentity.repositary.NfcCardRepository;
import com.personalidentity.repositary.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyticsService {

    private final ProfileRepository profileRepository;
    private final NfcCardRepository nfcCardRepository;

    public AnalyticsService(ProfileRepository profileRepository, NfcCardRepository nfcCardRepository) {
        this.profileRepository = profileRepository;
        this.nfcCardRepository = nfcCardRepository;
    }

    public AnalyticsOverviewDTO getOverview() {
        long totalProfiles = profileRepository.count();
        long activeProfiles = profileRepository.countByActiveTrue();
        long totalCards = nfcCardRepository.count();
        long activeCards = nfcCardRepository.countByActiveTrue();

        return AnalyticsOverviewDTO.builder()
                .totalProfiles(totalProfiles)
                .activeProfiles(activeProfiles)
                .totalCards(totalCards)
                .activeCards(activeCards)
                .uniqueVisitors(842L)
                .linkClicks(2341L)
                .topLinks(List.of(
                        ProfileLinkMetricDTO.builder().label("Resume").url("/resume").clicks(832).build(),
                        ProfileLinkMetricDTO.builder().label("LinkedIn").url("https://linkedin.com").clicks(621).build(),
                        ProfileLinkMetricDTO.builder().label("Email").url("mailto:hello@example.com").clicks(210).build()
                ))
                .build();
    }

    public Optional<ProfileAnalyticsDTO> getProfileAnalytics(String profileId) {
        return profileRepository.findById(profileId).map(profile -> ProfileAnalyticsDTO.builder()
                .profileId(profile.getId())
                .displayName(profile.getDisplayName())
                .profileViews(1284L)
                .uniqueVisitors(842L)
                .linkClicks(214L)
                .topLinks(List.of(
                        ProfileLinkMetricDTO.builder().label("Email").url("mailto:hello@example.com").clicks(82).build(),
                        ProfileLinkMetricDTO.builder().label("LinkedIn").url("https://linkedin.com").clicks(64).build(),
                        ProfileLinkMetricDTO.builder().label("GitHub").url("https://github.com").clicks(42).build()
                ))
                .build());
    }
}
