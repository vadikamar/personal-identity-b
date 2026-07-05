package com.personalidentity.service;

import com.personalidentity.dto.ProfileRequestDTO;
import com.personalidentity.entity.Profile;
import com.personalidentity.repositary.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileByUsername(String username) {
        return profileRepository.findByUserNameAndActiveTrue(username);
    }

    public Optional<Profile> getActiveProfile() {
        return profileRepository.findFirstByActiveTrue();
    }

    public Profile createProfile(ProfileRequestDTO request) {
        Profile profile = Profile.builder()
                .displayName(request.getDisplayName())
                .userName(request.getUserName())
                .profileType(request.getProfileType())
                .headline(request.getHeadline())
                .bio(request.getBio())
                .theme(request.getTheme())
                .active(request.isActive())
                .interests(request.getInterests())
                .links(request.getLinks())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return profileRepository.save(profile);
    }

    public Optional<Profile> updateProfile(String id, ProfileRequestDTO request) {
        return profileRepository.findById(id).map(existing -> {
            existing.setDisplayName(request.getDisplayName());
            existing.setProfileType(request.getProfileType());
            existing.setHeadline(request.getHeadline());
            existing.setBio(request.getBio());
            existing.setTheme(request.getTheme());
            existing.setActive(request.isActive());
            existing.setInterests(request.getInterests());
            existing.setLinks(request.getLinks());
            existing.setUpdatedAt(Instant.now());
            return profileRepository.save(existing);
        });
    }

    public void deleteProfile(String id) {
        if(profileRepository.findById(id).isPresent() && !profileRepository.findById(id).get().isActive()) {
            profileRepository.deleteById(id);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Profile> activateProfile(String id) {
        return profileRepository.findById(id).map(profile -> {
            profile.setActive(true);
            profile.setUpdatedAt(Instant.now());
            List<Profile> profiles = profileRepository.findByUserName(profile.getUserName());
            for(Profile p : profiles) {
                if(!Objects.equals(p.getId(), id)){
                    p.setActive(false);
                    p.setUpdatedAt(Instant.now());
                    profileRepository.save(p);
                }
            }
            return profileRepository.save(profile);
        });
    }

    public Optional<Profile> deactivateProfile(String id) {
        return profileRepository.findById(id).map(profile -> {
            profile.setActive(false);
            profile.setUpdatedAt(Instant.now());
            return profileRepository.save(profile);
        });
    }
}
