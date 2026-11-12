package com.agr04.config;

import com.agr04.model.Users;
import com.agr04.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Component
public class PageRoleAccessFilter extends OncePerRequestFilter {
    private static final Map<String, String> PAGE_ROLES = Map.of(
            "/admin.html", "ADMIN",
            "/owner.html", "OWNER",
            "/operator.html", "OPERATOR",
            "/index.html", "FARMER"
    );

    private final UserRepository userRepository;

    public PageRoleAccessFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        String requiredRole = PAGE_ROLES.get(path);
        if (requiredRole == null) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Object sessionUserId = session == null ? null : session.getAttribute("userId");
        if (!(sessionUserId instanceof Integer userId)) {
            writeAccessDenied(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Bạn cần đăng nhập để truy cập trang này.", "/login.html");
            return;
        }

        Users user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() == null) {
            session.invalidate();
            writeAccessDenied(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.", "/login.html");
            return;
        }

        String role = user.getRole().toUpperCase(Locale.ROOT);
        if (!requiredRole.equals(role)) {
            session.setAttribute("role", role);
            String destination = switch (role) {
                case "ADMIN" -> "/admin.html";
                case "OWNER" -> "/owner.html";
                case "OPERATOR" -> "/operator.html";
                case "FARMER" -> "/index.html";
                default -> "/login.html";
            };
            writeAccessDenied(response, HttpServletResponse.SC_FORBIDDEN,
                    "403 Forbidden: tài khoản của bạn không có quyền truy cập trang này.", destination);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeAccessDenied(HttpServletResponse response, int status,
                                   String message, String destination) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.getWriter().write("""
                <!doctype html>
                <html lang="vi">
                <head><meta charset="utf-8"><title>Truy cập bị từ chối</title></head>
                <body>
                <h1>%d - Truy cập bị từ chối</h1>
                <p>%s</p>
                <script>
                    alert('%s');
                    window.location.replace('%s');
                </script>
                </body>
                </html>
                """.formatted(status, message, message, destination));
    }
}
