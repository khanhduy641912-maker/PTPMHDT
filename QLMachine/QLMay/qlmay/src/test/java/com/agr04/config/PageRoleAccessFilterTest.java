package com.agr04.config;

import com.agr04.model.Users;
import com.agr04.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PageRoleAccessFilterTest {
    private UserRepository userRepository;
    private PageRoleAccessFilter filter;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        filter = new PageRoleAccessFilter(userRepository);
    }

    @Test
    void farmerGets403AndRedirectMessageWhenOpeningAdminPage() throws Exception {
        MockHttpServletRequest request = requestFor("/admin.html", "FARMER");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(403, response.getStatus());
        assertTrue(response.getContentAsString().contains("403 Forbidden"));
        assertTrue(response.getContentAsString().contains("window.location.replace('/index.html')"));
        assertNull(chain.getRequest());
    }

    @Test
    void ownerAndOperatorAreDeniedFromAdminPage() throws Exception {
        for (String role : new String[]{"OWNER", "OPERATOR"}) {
            MockHttpServletRequest request = requestFor("/admin.html", role);
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilter(request, response, new MockFilterChain());

            assertEquals(403, response.getStatus());
            assertTrue(response.getContentAsString().contains(
                    role.equals("OWNER") ? "window.location.replace('/owner.html')"
                            : "window.location.replace('/operator.html')"));
        }
    }

    @Test
    void adminCanOpenAdminPage() throws Exception {
        MockHttpServletRequest request = requestFor("/admin.html", "ADMIN");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertEquals(request, chain.getRequest());
    }

    @Test
    void anonymousUserGets401AndLoginRedirect() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/admin.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("window.location.replace('/login.html')"));
    }

    private MockHttpServletRequest requestFor(String path, String role) {
        Users user = new Users();
        user.setUserId(7);
        user.setRole(role);
        when(userRepository.findById(7)).thenReturn(Optional.of(user));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 7);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(path);
        request.setSession(session);
        return request;
    }
}
