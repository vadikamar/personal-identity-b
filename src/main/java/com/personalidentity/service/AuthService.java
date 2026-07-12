package com.personalidentity.service;

import com.personalidentity.dto.AuthRequestDTO;
import com.personalidentity.entity.UserAccount;
import com.personalidentity.repositary.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(AuthRequestDTO request) {
        if (request.getUsername() == null || request.getUsername().isBlank() || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }
        if (userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        UserAccount user = UserAccount.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        userAccountRepository.save(user);
    }

    public boolean signIn(AuthRequestDTO request) {
        if (request.getUsername() == null || request.getUsername().isBlank() || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }

        Optional<UserAccount> userOpt = userAccountRepository.findByUsername(request.getUsername());
        return userOpt.filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash())).isPresent();
    }

    public boolean updatePassword(AuthRequestDTO request) {
        if (request.getUsername() == null || request.getUsername().isBlank() || request.getPassword() == null || request.getPassword().isBlank() || request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username, current password and new password are required");
        }

        Optional<UserAccount> userOpt = userAccountRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return false;
        }

        UserAccount user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(Instant.now());
        userAccountRepository.save(user);
        return true;
    }
}
