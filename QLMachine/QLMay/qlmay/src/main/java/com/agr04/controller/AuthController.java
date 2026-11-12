package com.agr04.controller;

import com.agr04.model.Users;
import com.agr04.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Set<String> REGISTERABLE_ROLES = Set.of("FARMER", "OWNER", "OPERATOR");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body,
                                     HttpServletRequest request) {
        String phone = body.get("phone");
        String password = body.get("password");
        Users user = phone == null ? null : userRepository.findByPhone(phone.trim());

        if (user == null || password == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Số điện thoại hoặc mật khẩu không chính xác.");
        }

        HttpSession session = request.getSession(true);
        request.changeSessionId();
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("role", user.getRole());
        session.setAttribute("fullName", user.getFullName());
        return userResponse(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String fullName = body.get("fullName");
        String phone = body.get("phone");
        String password = body.get("password");
        String role = body.getOrDefault("role", "FARMER").toUpperCase(Locale.ROOT);
        if (isBlank(fullName) || isBlank(phone) || isBlank(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng điền đầy đủ họ tên, số điện thoại và mật khẩu.");
        }
        if (!REGISTERABLE_ROLES.contains(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vai trò đăng ký không hợp lệ.");
        }
        if (userRepository.existsByPhone(phone.trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Số điện thoại này đã được đăng ký.");
        }

        Users user = new Users();
        user.setFullName(fullName.trim());
        user.setPhone(phone.trim());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse(userRepository.save(user)));
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Số điện thoại này đã được đăng ký.");
        }
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập.");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không còn tồn tại."));
        return userResponse(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> userResponse(Users user) {
        return Map.of(
                "userId", user.getUserId(),
                "fullName", user.getFullName(),
                "phone", user.getPhone(),
                "role", user.getRole()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
