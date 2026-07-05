package com.personalidentity.repositary;

import com.personalidentity.entity.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findFirstByActiveTrue();
    long countByActiveTrue();
    Optional<Profile> findByUserNameAndActiveTrue(String userName);
    List<Profile> findByUserName(String userName);
}
