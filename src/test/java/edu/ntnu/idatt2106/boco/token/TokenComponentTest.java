package edu.ntnu.idatt2106.boco.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private TokenComponent tokenComponent;
    @Autowired
    private MockMvc mvc;

    @Mock
    private UserRepository userRepository;

    User user =new User();
    @BeforeEach
    public void setUp() throws Exception {
        RepositoryMock.mockUserRepository(userRepository);
        User user=new UserFactory().getObject();
        userRepository.save(user);
    }

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
        user.setRole("ADMIN");
        userRepository.save(user);
        String token =tokenComponent.generateToken(user.getUserId(), user.getRole());
        assertNotNull(token);
    }

    /**
     * Mehtod for testing if user are user or admin,
     * @throws Exception
     */

    @Test
    void isThisUserTest() throws Exception {
        user.setRole("USER");
        userRepository.save(user);
        tokenComponent.generateToken(user.getUserId(), user.getRole());
        Boolean isUser=tokenComponent.haveAccessTo(user.getUserId());
        assertThat(isUser).isEqualTo(true);
    }

    /**
     * method for testing if user just admin not just user
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "email", password = "password", roles = {"ADMIN"})
    void isAdminTest() throws Exception {
        user.setRole("ADMIN");
        userRepository.save(user);
        Boolean isAdmin=tokenComponent.haveAccessTo(user.getUserId());
        assertThat(isAdmin).isEqualTo(true);
    }
}