package com.example.lms.auth;

import com.example.lms.user.User;
import com.example.lms.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CurrentUser {

    private final UserService users;

    public CurrentUser(UserService users) {
        this.users = users;
    }

    /** Вернёт текущего пользователя по email из JWT или бросит 401 */
    public User require() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated() || a.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return users.getByEmailOrThrow(a.getName());
    }
}
