package com.example.lms.init;

import com.example.lms.user.Role;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        final String email = "admin@lms.local";
        final String rawPassword = "admin12345"; // поменяешь позже
        userRepository.findByEmail(email).ifPresentOrElse(
                u -> System.out.println("[INIT] Admin already exists: " + email),
                () -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setFullName("Admin");
                    u.setRole(Role.ADMIN);
                    u.setPasswordHash(passwordEncoder.encode(rawPassword));
                    userRepository.save(u);
                    System.out.println("[INIT] Created default admin: " + email + " / " + rawPassword);
                }
        );
    }
}
