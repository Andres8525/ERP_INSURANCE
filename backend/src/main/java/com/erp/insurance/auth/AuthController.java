package com.erp.insurance.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository users;
    private final JwtEncoder jwtEncoder;
    private final long ttlMinutes;

    public AuthController(AuthenticationManager authenticationManager, UserRepository users, JwtEncoder jwtEncoder,
                          @Value("${app.security.jwt-ttl-minutes:15}") long ttlMinutes) {
        this.authenticationManager = authenticationManager;
        this.users = users;
        this.jwtEncoder = jwtEncoder;
        this.ttlMinutes = ttlMinutes;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
            UserEntity user = users.findByEmailIgnoreCaseAndActiveTrue(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
            Instant now = Instant.now();
            Instant expiry = now.plusSeconds(ttlMinutes * 60);
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("erp-insurance-api")
                    .issuedAt(now)
                    .expiresAt(expiry)
                    .subject(user.getId().toString())
                    .claim("email", user.getEmail())
                    .claim("role", user.getRole().name())
                    .build();
            return new TokenResponse(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), "Bearer", expiry);
        } catch (org.springframework.security.core.AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    public record LoginRequest(@Email @NotBlank @Size(max = 320) String email,
                               @NotBlank @Size(min = 12, max = 128) String password) {}
    public record TokenResponse(String accessToken, String tokenType, Instant expiresAt) {}
}