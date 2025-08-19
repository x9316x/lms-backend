package com.example.lms.api;

import com.example.lms.auth.CurrentUser;
import com.example.lms.user.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MeController {

    private final CurrentUser current;

    public MeController(CurrentUser current) {
        this.current = current;
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        User u = current.require();
        return Map.of(
                "id", u.getId(),
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "role", u.getRole(),
                "createdAt", u.getCreatedAt()
        );
    }
}
