package com.personalidentity.controller;

import com.personalidentity.dto.ApiResponseDTO;
import com.personalidentity.dto.ProfileRequestDTO;
import com.personalidentity.entity.Profile;
import com.personalidentity.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
