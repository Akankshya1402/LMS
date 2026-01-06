package com.lms.auth.security;

import com.lms.auth.config.SecurityConfig;
import com.lms.auth.controller.AdminController;
import com.lms.auth.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = true)
class RoleAccessTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 THIS FIXES YOUR ERROR
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerShouldNotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard"))
                .andExpect(status().isForbidden());
    }
}
