package com.example.lms.auth;

import com.example.lms.user.Role;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@Valid @RequestBody RegisterRequest req) {
        if (users.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        User u = new User();
        u.setEmail(req.getEmail());
        u.setFullName(req.getFullName());
        u.setRole(Role.STUDENT); // по умолчанию студент
        u.setPasswordHash(encoder.encode(req.getPassword()));
        users.save(u);

        String access = jwt.generateAccessToken(u);
        String refresh = jwt.generateRefreshToken(u);
        return new TokenResponse(access, refresh);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest req) {
        User u = users.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(req.getPassword(), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        String access = jwt.generateAccessToken(u);
        String refresh = jwt.generateRefreshToken(u);
        return new TokenResponse(access, refresh);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest req) {
        var jws = jwt.parse(req.getRefreshToken());
        if (!jwt.isRefreshToken(jws)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a refresh token");
        }
        String email = jwt.email(jws);
        User u = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        String access = jwt.generateAccessToken(u);
        // по желанию можно выпускать новый refresh, но для простоты оставим старый
        return new TokenResponse(access, req.getRefreshToken());
    }
}
