package com.example.lms.init;

import com.example.lms.user.Role;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class StudentSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        final String email = "student@lms.local";
        final String rawPassword = "student12345";
        userRepository.findByEmail(email).ifPresentOrElse(
                u -> System.out.println("[INIT] Student already exists: " + email),
                () -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setFullName("Student");
                    u.setRole(Role.STUDENT);
                    u.setPasswordHash(passwordEncoder.encode(rawPassword));
                    userRepository.save(u);
                    System.out.println("[INIT] Created default student: " + email + " / " + rawPassword);
                }
        );
    }
}
