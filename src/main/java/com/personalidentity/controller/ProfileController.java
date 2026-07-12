package com.personalidentity.controller;

import com.personalidentity.dto.ApiResponseDTO;
import com.personalidentity.dto.ProfileAccessRequestDTO;
import com.personalidentity.dto.ProfilePostRequestDTO;
import com.personalidentity.dto.ProfileRequestDTO;
import com.personalidentity.dto.VisitorLocationRequestDTO;
import com.personalidentity.entity.Profile;
import com.personalidentity.entity.ProfileAccessRequest;
import com.personalidentity.entity.ProfilePost;
import com.personalidentity.entity.VisitorLocation;
import com.personalidentity.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:8080, https://personal-identity-nine.vercel.app/")
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final Logger log = Logger.getLogger(ProfileController.class.getName());
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<Profile>>> getProfiles() {
        log.info("ProfileController getProfiles");
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Profiles fetched", UUID.randomUUID().toString(), profileService.getAllProfiles()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<Profile>> getActiveProfile() {
        log.info("ProfileController getActiveProfile");
        return profileService.getActiveProfile()
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Active profile fetched", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponseDTO<Profile>> getProfile(@PathVariable String username) {
        log.info("ProfileController getProfile");
        return profileService.getProfileByUsername(username)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile fetched", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Profile>> createProfile(@RequestBody ProfileRequestDTO request) {
        log.info("ProfileController createProfile");
        Profile created = profileService.createProfile(request);
        log.info("ProfileController createProfile created");
        return ResponseEntity.ok(new ApiResponseDTO<>(201, "Profile created", UUID.randomUUID().toString(), created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Profile>> updateProfile(@PathVariable String id, @RequestBody ProfileRequestDTO request) {
        log.info("ProfileController updateProfile");
        return profileService.updateProfile(id, request)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile updated", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProfile(@PathVariable String id) {
        log.info("ProfileController deleteProfile");
        profileService.deleteProfile(id);
        log.info("ProfileController deleteProfile deleted");
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile deleted", UUID.randomUUID().toString(), null));
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<Profile>> uploadProfilePhoto(@PathVariable String id,
                                                                      @RequestParam("file") MultipartFile file) {
        log.info("ProfileController uploadProfilePhoto");
        return profileService.uploadProfilePhoto(id, file)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile photo uploaded", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/posts")
    public ResponseEntity<ApiResponseDTO<Profile>> addPost(@PathVariable String id, @RequestBody ProfilePostRequestDTO request) {
        log.info("ProfileController addPost");
        return profileService.addPost(id, request)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Post created", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<ApiResponseDTO<List<ProfilePost>>> getPosts(@PathVariable String id) {
        log.info("ProfileController getPosts");
        return profileService.getPosts(id)
                .map(posts -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Posts fetched", UUID.randomUUID().toString(), posts)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/posts/{postId}")
    public ResponseEntity<ApiResponseDTO<ProfilePost>> updatePost(@PathVariable String id,
                                                                   @PathVariable String postId,
                                                                   @RequestBody ProfilePostRequestDTO request) {
        log.info("ProfileController updatePost");
        return profileService.updatePost(id, postId, request)
                .map(post -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Post updated", UUID.randomUUID().toString(), post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/posts/{postId}")
    public ResponseEntity<ApiResponseDTO<Void>> deletePost(@PathVariable String id, @PathVariable String postId) {
        log.info("ProfileController deletePost");
        boolean deleted = profileService.deletePost(id, postId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Post deleted", UUID.randomUUID().toString(), null));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO<Profile>> activateProfile(@PathVariable String id) {
        log.info("ProfileController activateProfile");
        return profileService.activateProfile(id)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile activated", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<Profile>> deactivateProfile(@PathVariable String id) {
        log.info("ProfileController deactivateProfile");
        return profileService.deactivateProfile(id)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile deactivated", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{username}/visitor-location")
    public ResponseEntity<ApiResponseDTO<Profile>> recordVisitorLocation(@PathVariable String username,
                                                                        @RequestBody VisitorLocationRequestDTO request) {
        log.info("ProfileController recordVisitorLocation");
        return profileService.recordVisitorLocation(username, request)
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Visitor location recorded", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{username}/visitor-locations")
    public ResponseEntity<ApiResponseDTO<List<VisitorLocation>>> getVisitorLocations(@PathVariable String username) {
        log.info("ProfileController getVisitorLocations");
        return profileService.getVisitorLocations(username)
                .map(locations -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Visitor locations fetched", UUID.randomUUID().toString(), locations)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{ownerProfileId}/access-requests")
    public ResponseEntity<ApiResponseDTO<ProfileAccessRequest>> requestProfileAccess(@PathVariable String ownerProfileId,
                                                                                   @RequestBody ProfileAccessRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        String requesterUsername = isAuthenticated ? authentication.getName() : null;
        if (!isAuthenticated || requesterUsername == null || requesterUsername.isBlank()) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(401, "Authentication required", UUID.randomUUID().toString(), null));
        }
        ProfileAccessRequest created = profileService.requestProfileAccess(ownerProfileId, requesterUsername, request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Access request created", UUID.randomUUID().toString(), created));
    }

    @GetMapping("/dashboard/access-requests")
    public ResponseEntity<ApiResponseDTO<List<ProfileAccessRequest>>> getPendingAccessRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        String currentUsername = isAuthenticated ? authentication.getName() : null;
        if (!isAuthenticated || currentUsername == null || currentUsername.isBlank()) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(401, "Authentication required", UUID.randomUUID().toString(), null));
        }
        List<ProfileAccessRequest> requests = profileService.getPendingRequestsForOwner(currentUsername);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Access requests fetched", UUID.randomUUID().toString(), requests));
    }

    @PostMapping("/access-requests/{requestId}/approve")
    public ResponseEntity<ApiResponseDTO<ProfileAccessRequest>> approveAccessRequest(@PathVariable String requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        String currentUsername = isAuthenticated ? authentication.getName() : null;
        if (!isAuthenticated || currentUsername == null || currentUsername.isBlank()) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(401, "Authentication required", UUID.randomUUID().toString(), null));
        }
        return profileService.approveAccessRequest(requestId, currentUsername)
                .map(request -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Access request approved", UUID.randomUUID().toString(), request)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/access-requests/{requestId}/reject")
    public ResponseEntity<ApiResponseDTO<ProfileAccessRequest>> rejectAccessRequest(@PathVariable String requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        String currentUsername = isAuthenticated ? authentication.getName() : null;
        if (!isAuthenticated || currentUsername == null || currentUsername.isBlank()) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(401, "Authentication required", UUID.randomUUID().toString(), null));
        }
        return profileService.rejectAccessRequest(requestId, currentUsername)
                .map(request -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Access request rejected", UUID.randomUUID().toString(), request)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{username}/view")
    public ResponseEntity<ApiResponseDTO<Profile>> viewProfileIfAuthorized(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        String currentUsername = isAuthenticated ? authentication.getName() : null;
        if (!isAuthenticated || currentUsername == null || currentUsername.isBlank()) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(401, "Authentication required", UUID.randomUUID().toString(), null));
        }
        String[] canAccess = profileService.canUserAccessProfile(username, currentUsername);
        if (canAccess[0].equals("0")) {
            return ResponseEntity.status(403).body(new ApiResponseDTO<>(403, "Access denied", UUID.randomUUID().toString(), null));
        }
        return profileService.getProfileById(canAccess[1])
                .map(profile -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile fetched", UUID.randomUUID().toString(), profile)))
                .orElse(ResponseEntity.notFound().build());
    }
}
