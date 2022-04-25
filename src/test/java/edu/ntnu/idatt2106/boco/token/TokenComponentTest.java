package edu.ntnu.idatt2106.boco.token;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TokenComponent.class
)
@AutoConfigureMockMvc
class TokenComponentTest {

    @Autowired
    TokenComponent tokenComponent;
    protected MockMvc mvc;

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(get("/test")).andExpect(status().isForbidden());
    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
        String token = tokenComponent.generateToken(1L,"ADMIN");

        assertNotNull(token);
        mvc.perform(get("/test").header("Authorization", token))
                .andExpect(status().isOk());
    }

    /**
     * Mehtod for testing if user are user or admin,
     * @throws Exception
     */

    @Test
    @WithMockUser(username = "email", password = "password", roles = {"USER","ADMIN"})
    void isThisUserTest() throws Exception {
        this.mvc.perform(get("\"/user\"")).andExpect(status().isOk());
    }

    /**
     * method for testing if user just admin not just user
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "email", password = "password", roles = {"ADMIN"})
    void isAdminTest() throws Exception {
        this.mvc.perform(get("\"/user\"")).andExpect(status().isOk());
    }
}