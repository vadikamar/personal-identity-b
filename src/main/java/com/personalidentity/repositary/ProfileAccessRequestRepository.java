package com.personalidentity.repositary;

import com.personalidentity.entity.ProfileAccessRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileAccessRequestRepository extends MongoRepository<ProfileAccessRequest, String> {
    List<ProfileAccessRequest> findByOwnerUsernameAndStatus(String ownerUsername, String status);
    List<ProfileAccessRequest> findByOwnerUsername(String ownerUsername);
    Optional<ProfileAccessRequest> findByOwnerUsernameAndRequesterUsernameAndRequestedProfileIdAndStatus(
            String ownerUsername,
            String requesterUsername,
            String requestedProfileId,
            String status);
    Optional<ProfileAccessRequest> findByOwnerUsernameAndRequesterUsername(String ownerUsername, String requesterUsername);
}
