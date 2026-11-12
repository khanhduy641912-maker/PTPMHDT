package com.agr04.service;

import jakarta.servlet.http.HttpSession;
import com.agr04.model.Users;
import com.agr04.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Locale;

@Component
public class SessionIdentity {
    private final UserRepository userRepository;

    public SessionIdentity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Integer requireUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Integer id) {
            return id;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để tiếp tục.");
    }

    public String requireRole(HttpSession session) {
        Integer userId = requireUserId(session);
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() == null) {
            session.invalidate();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Phiên đăng nhập không hợp lệ.");
        }
        String role = user.getRole().toUpperCase(Locale.ROOT);
        session.setAttribute("role", role);
        session.setAttribute("fullName", user.getFullName());
        return role;
    }

    public Integer requireRole(HttpSession session, String... allowedRoles) {
        String role = requireRole(session);
        if (Arrays.stream(allowedRoles).noneMatch(role::equals)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này.");
        }
        return requireUserId(session);
    }

    public boolean hasRole(HttpSession session, String role) {
        return role.equals(requireRole(session));
    }
}
