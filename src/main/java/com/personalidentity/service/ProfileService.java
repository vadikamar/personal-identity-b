package com.personalidentity.service;

import com.personalidentity.dto.ProfileAccessRequestDTO;
import com.personalidentity.dto.ProfilePostRequestDTO;
import com.personalidentity.dto.ProfileRequestDTO;
import com.personalidentity.dto.VisitorLocationRequestDTO;
import com.personalidentity.entity.Profile;
import com.personalidentity.entity.ProfileAccessRequest;
import com.personalidentity.entity.ProfilePost;
import com.personalidentity.entity.VisitorLocation;
import com.personalidentity.repositary.ProfileAccessRequestRepository;
import com.personalidentity.repositary.ProfileRepository;
import com.personalidentity.repositary.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final CloudinaryService cloudinaryService;
    private final ProfileAccessRequestRepository profileAccessRequestRepository;
    private final UserAccountRepository userAccountRepository;

    public ProfileService(ProfileRepository profileRepository,
                          CloudinaryService cloudinaryService,
                          ProfileAccessRequestRepository profileAccessRequestRepository,
                          UserAccountRepository userAccountRepository) {
        this.profileRepository = profileRepository;
        this.cloudinaryService = cloudinaryService;
        this.profileAccessRequestRepository = profileAccessRequestRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileByUsername(String username) {
        return profileRepository.findByUserNameAndActiveTrue(username);
    }
    public Optional<Profile> getProfileById(String id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> getProfileByUsername(String username, String loggedUser) {
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
                .photoUrl(request.getPhotoUrl())
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
            existing.setPhotoUrl(request.getPhotoUrl());
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

    public Optional<Profile> uploadProfilePhoto(String id, MultipartFile file) {
        return profileRepository.findById(id).map(profile -> {
            String photoUrl = cloudinaryService.uploadImage(file);
            profile.setPhotoUrl(photoUrl);
            profile.setUpdatedAt(Instant.now());
            return profileRepository.save(profile);
        });
    }

    public Optional<Profile> addPost(String id, ProfilePostRequestDTO request) {
        return profileRepository.findById(id).map(profile -> {
            if (profile.getPosts() == null) {
                profile.setPosts(new java.util.ArrayList<>());
            }
            if (profile.getPosts().size() >= 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A profile can have at most 5 posts");
            }

            ProfilePost post = ProfilePost.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .description(request.getDescription())
                    .photoUrl(request.getPhotoUrl())
                    .createdAt(Instant.now())
                    .build();

            profile.getPosts().add(post);
            profile.setUpdatedAt(Instant.now());
            return profileRepository.save(profile);
        });
    }

    public Optional<List<ProfilePost>> getPosts(String id) {
        return profileRepository.findById(id).map(Profile::getPosts);
    }

    public Optional<ProfilePost> updatePost(String profileId, String postId, ProfilePostRequestDTO request) {
        return profileRepository.findById(profileId).flatMap(profile -> {
            if (profile.getPosts() == null) {
                return Optional.empty();
            }

            return profile.getPosts().stream()
                    .filter(post -> postId.equals(post.getId()))
                    .findFirst()
                    .map(post -> {
                        post.setDescription(request.getDescription());
                        post.setPhotoUrl(request.getPhotoUrl());
                        profile.setUpdatedAt(Instant.now());
                        profileRepository.save(profile);
                        return post;
                    });
        });
    }

    public boolean deletePost(String profileId, String postId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (profileOpt.isEmpty()) {
            return false;
        }

        Profile profile = profileOpt.get();
        if (profile.getPosts() == null) {
            return false;
        }

        boolean removed = profile.getPosts().removeIf(post -> postId.equals(post.getId()));
        if (removed) {
            profile.setUpdatedAt(Instant.now());
            profileRepository.save(profile);
        }
        return removed;
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

    public Optional<Profile> recordVisitorLocation(String username, VisitorLocationRequestDTO request) {
        return profileRepository.findByUserNameAndActiveTrue(username)
                .filter(profile -> "SOS".equalsIgnoreCase(profile.getProfileType()))
                .map(profile -> {
                    if (profile.getVisitorLocations() == null) {
                        profile.setVisitorLocations(new java.util.ArrayList<>());
                    }
                    VisitorLocation visitorLocation = VisitorLocation.builder()
                            .id(UUID.randomUUID().toString())
                            .latitude(request.getLatitude())
                            .longitude(request.getLongitude())
                            .city(request.getCity())
                            .country(request.getCountry())
                            .address(request.getAddress())
                            .visitedAt(Instant.now())
                            .build();
                    profile.getVisitorLocations().add(visitorLocation);
                    profile.setUpdatedAt(Instant.now());
                    return profileRepository.save(profile);
                });
    }

    public Optional<List<VisitorLocation>> getVisitorLocations(String username) {
        return profileRepository.findByUserNameAndActiveTrue(username)
                .filter(profile -> "SOS".equalsIgnoreCase(profile.getProfileType()))
                .map(profile -> Optional.ofNullable(profile.getVisitorLocations()).orElseGet(java.util.ArrayList::new));
    }

    public ProfileAccessRequest requestProfileAccess(String ownerProfileId, String requesterUsername, ProfileAccessRequestDTO request) {
        Profile ownerProfile = profileRepository.findById(ownerProfileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        if (ownerProfile.getAuthorizedViewerUsernames() != null && ownerProfile.getAuthorizedViewerUsernames().contains(requesterUsername)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has access");
        }

        Optional<ProfileAccessRequest> existingRequest = profileAccessRequestRepository.findByOwnerUsernameAndRequesterUsernameAndRequestedProfileIdAndStatus(
                ownerProfile.getUserName(), requesterUsername, request.getRequestedProfileId(), "PENDING");
        if (existingRequest.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A pending request already exists");
        }

        ProfileAccessRequest accessRequest = ProfileAccessRequest.builder()
                .ownerProfileId(ownerProfileId)
                .ownerUsername(ownerProfile.getUserName())
                .requesterUsername(requesterUsername)
                .requestedProfileId(request.getRequestedProfileId())
                .message(request.getMessage())
                .status("PENDING")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return profileAccessRequestRepository.save(accessRequest);
    }

    public List<ProfileAccessRequest> getPendingRequestsForOwner(String ownerUsername) {
        return profileAccessRequestRepository.findByOwnerUsernameAndStatus(ownerUsername, "PENDING");
    }

    public Optional<ProfileAccessRequest> approveAccessRequest(String requestId, String currentOwnerUsername) {
        return profileAccessRequestRepository.findById(requestId).map(request -> {
            if (!Objects.equals(request.getOwnerUsername(), currentOwnerUsername)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to approve this request");
            }
            Optional<ProfileAccessRequest> accessRequest = profileAccessRequestRepository.findByOwnerUsernameAndRequesterUsernameAndStatus(
                    request.getOwnerUsername(), request.getRequesterUsername(), "APPROVED");
            if (accessRequest.isPresent()) {
                accessRequest.get().setStatus("REJECTED");
                profileAccessRequestRepository.save(accessRequest.get());
                Optional<Profile> profile = profileRepository.findById(accessRequest.get().getRequestedProfileId());
                if(profile.isPresent()) {
                    profile.get().setAuthorizedViewerUsernames(
                            profile.get().getAuthorizedViewerUsernames().stream().filter(username -> !username.equals(accessRequest.get().getRequesterUsername()))
                                    .toList());
                    profileRepository.save(profile.get());
                }
            }
            request.setStatus("APPROVED");
            request.setUpdatedAt(Instant.now());
            profileAccessRequestRepository.save(request);

            profileRepository.findById(request.getOwnerProfileId()).ifPresent(profile -> {
                if (profile.getAuthorizedViewerUsernames() == null) {
                    profile.setAuthorizedViewerUsernames(new ArrayList<>());
                }
                if (!profile.getAuthorizedViewerUsernames().contains(request.getRequesterUsername())) {
                    profile.getAuthorizedViewerUsernames().add(request.getRequesterUsername());
                    profile.setUpdatedAt(Instant.now());
                    profileRepository.save(profile);
                }
            });
            return request;
        });
    }

    public Optional<ProfileAccessRequest> rejectAccessRequest(String requestId, String currentOwnerUsername) {
        return profileAccessRequestRepository.findById(requestId).map(request -> {
            if (!Objects.equals(request.getOwnerUsername(), currentOwnerUsername)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to reject this request");
            }
            request.setStatus("REJECTED");
            request.setUpdatedAt(Instant.now());
            return profileAccessRequestRepository.save(request);
        });
    }

    public String[] canUserAccessProfile(String username, String requesterUsername) {
        Optional<ProfileAccessRequest> profileOpt = profileAccessRequestRepository.findByOwnerUsernameAndRequesterUsernameAndStatus(username, requesterUsername, "APPROVED");
        if (profileOpt.isEmpty()) {
            return new String[]{"0", null};
        }
        ProfileAccessRequest profileAccessRequest = profileOpt.get();
        Optional<Profile> profile = profileRepository.findById(profileAccessRequest.getRequestedProfileId());
        if (profile.isEmpty()) {
            return new String[]{"0", null};
        }
        if (Objects.equals(profile.get().getUserName(), requesterUsername)) {
            return new String[]{"1", profile.get().getId()};
        }
        if (profile.get().getAuthorizedViewerUsernames() == null) {
            return new String[]{"0", profile.get().getId()};
        }
        return new String[]{profile.get().getAuthorizedViewerUsernames().contains(requesterUsername)?"1":"0", profile.get().getId()};
    }
}

