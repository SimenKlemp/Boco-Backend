package edu.ntnu.idatt2106.boco.service;


import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        User user = new User("Name", true, "Address", "Email", "Password", "Admin");

        Mockito.lenient().when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.lenient().when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.lenient().when(userRepository.findByEmail(Mockito.anyString())).thenReturn(java.util.Optional.of(user));
    }
    @Test
    void registerTest() {
        RegisterUserRequest request = new RegisterUserRequest("Name",true, "Address", "Email", "Password");

        User user = userService.register(request);
    }

    @Test
    void deleteUserByEmailTest() {
        User user = new User("Name", true, "Address", "Email", "Password", "Admin");

        boolean response = userService.deleteUserByEmail(user);

        assertThat(response).isTrue();
    }

    @Test
    void updateUserByEmailTest() {
        User user = new User("Name", true, "Address", "Email", "Password", "Admin");

        boolean response = userService.updateUserByEmail("NewEmail.com", user);

        assertThat(response).isTrue();
    }

}
