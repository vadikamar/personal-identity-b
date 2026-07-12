package com.personalidentity.controller;

import com.personalidentity.dto.ApiResponseDTO;
import com.personalidentity.dto.AuthRequestDTO;
import com.personalidentity.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:8080, https://personal-identity-nine.vercel.app/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final Logger log = Logger.getLogger(AuthController.class.getName());
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponseDTO<String>> signUp(@RequestBody AuthRequestDTO request) {
        log.info("AuthController signUp");
        authService.signUp(request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Sign up successful", UUID.randomUUID().toString(), "Signed up successfully"));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponseDTO<String>> signIn(@RequestBody AuthRequestDTO request, HttpServletRequest servletRequest) {
        log.info("AuthController signIn");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            servletRequest.getSession(true);
            return ResponseEntity.ok(new ApiResponseDTO<>(200, "Sign in successful", UUID.randomUUID().toString(), "Signed in successfully"));
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO<>(401, "Invalid username or password", UUID.randomUUID().toString(), null));
        }
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponseDTO<String>> signOut(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        log.info("AuthController signOut");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(servletRequest, servletResponse, authentication);
        } else {
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Sign out successful", UUID.randomUUID().toString(), "Signed out successfully"));
    }

    @PostMapping("/password-update")
    public ResponseEntity<ApiResponseDTO<String>> updatePassword(@RequestBody AuthRequestDTO request) {
        log.info("AuthController updatePassword");
        boolean updated = authService.updatePassword(request);
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid current password");
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Password updated", UUID.randomUUID().toString(), "Password updated successfully"));
    }
}
