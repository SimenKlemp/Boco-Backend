package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest
{
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private FeedbackWebPageRepository feedbackWebPageRepository;

    @BeforeEach
    public void beforeEach()
    {
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockImageRepository(imageRepository);
        RepositoryMock.mockItemRepository(itemRepository);
        RepositoryMock.mockRentalRepository(rentalRepository);
        RepositoryMock.mockFeedbackWebPageRepository(feedbackWebPageRepository);
    }

    @Test
    public void registerWithoutImage()
    {
        RegisterUserRequest request = new RegisterUserRequest(
                "name",
                true,
                "streetAddress",
                "postalCode",
                "postOffice",
                "email",
                "password",
                null
        );

        UserResponse response = userService.register(request);

        User user = userRepository.findById(response.getUserId()).orElseThrow();
        assertThat(user.getName()).isEqualTo(response.getName()).isEqualTo(request.getName());
        assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson()).isEqualTo(request.getIsPerson());
        assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(user.getEmail()).isEqualTo(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getImage()).isNull();
        assertThat(response.getImageId()).isEqualTo(request.getImageId());
    }

    @Test
    public void registerWithImage()
    {
        Image image = new Image("name", null);
        image = imageRepository.save(image);

        RegisterUserRequest request = new RegisterUserRequest(
                "name",
                true,
                "streetAddress",
                "postalCode",
                "postOffice",
                "email",
                "password",
                image.getImageId()
        );

        UserResponse response = userService.register(request);

        User user = userRepository.findById(response.getUserId()).orElseThrow();
        assertThat(user.getName()).isEqualTo(response.getName()).isEqualTo(request.getName());
        assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson()).isEqualTo(request.getIsPerson());
        assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(user.getEmail()).isEqualTo(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getImage().getImageId()).isEqualTo(response.getImageId()).isEqualTo(image.getImageId());
    }

    @Test
    public void registerWrongImage()
    {
        RegisterUserRequest request = new RegisterUserRequest(
                "name",
                true,
                "streetAddress",
                "postalCode",
                "postOffice",
                "email",
                "password",
                1L
        );

        UserResponse response = userService.register(request);

        User user = userRepository.findById(response.getUserId()).orElseThrow();
        assertThat(user.getName()).isEqualTo(response.getName()).isEqualTo(request.getName());
        assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson()).isEqualTo(request.getIsPerson());
        assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(user.getEmail()).isEqualTo(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getImageId()).isNull();
    }

    @Test
    public void registerExistingEmail()
    {
        User existingUser = ModelFactory.getUser(null);
        existingUser = userRepository.save(existingUser);

        RegisterUserRequest request = new RegisterUserRequest(
                "name",
                true,
                "streetAddress",
                "postalCode",
                "postOffice",
                existingUser.getEmail(),
                "password",
                1L
        );

        userService.register(request);
        UserResponse response = userService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void loginCorrect()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        LoginRequest request = new LoginRequest(
                user.getEmail(),
                "password"
        );

        UserResponse response = userService.login(request);

        assertThat(user.getUserId()).isEqualTo(response.getUserId());
        assertThat(user.getName()).isEqualTo(response.getName());
        assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson());
        assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress());
        assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode());
        assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice());
        assertThat(user.getEmail()).isEqualTo(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getImageId()).isNull();
    }

    @Test
    public void loginWrongEmail()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        LoginRequest request = new LoginRequest(
                "wrong email",
                "password"
        );

        UserResponse response = userService.login(request);

        assertThat(response).isNull();
    }

    @Test
    public void loginWrongPassword()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        LoginRequest request = new LoginRequest(
                user.getEmail(),
                "wrong password"
        );

        UserResponse response = userService.login(request);

        assertThat(response).isNull();
    }

    @Test
    public void deleteWithoutAnything()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        boolean success = userService.deleteUser(user.getUserId());

        assertThat(success).isTrue();
        assertThat(userRepository.findById(user.getUserId()).isEmpty()).isTrue();
    }

    @Test
    public void deleteWithImage()
    {
        Image image = new Image("name", null);
        image = imageRepository.save(image);

        User user = ModelFactory.getUser(image);
        user = userRepository.save(user);

        boolean success = userService.deleteUser(user.getUserId());

        assertThat(success).isTrue();
        assertThat(userRepository.findById(user.getUserId()).isEmpty()).isTrue();
        assertThat(imageRepository.findById(image.getImageId()).isEmpty()).isTrue();
    }

    @Test
    public void deleteWithItem()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        boolean success = userService.deleteUser(user.getUserId());

        assertThat(success).isTrue();
        assertThat(userRepository.findById(user.getUserId()).isEmpty()).isTrue();
        assertThat(itemRepository.findById(item.getItemId()).isEmpty()).isTrue();
    }

    @Test
    public void deleteWithRental()
    {
        User otherUser = ModelFactory.getUser(null);
        otherUser = userRepository.save(otherUser);

        Item item = ModelFactory.getItem(null, otherUser);
        item = itemRepository.save(item);

        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        boolean success = userService.deleteUser(user.getUserId());

        assertThat(success).isTrue();
        assertThat(userRepository.findById(user.getUserId()).isEmpty()).isTrue();
        assertThat(rentalRepository.findById(rental.getRentalId()).isEmpty()).isTrue();
    }

    @Test
    public void deleteWithFeedback()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        FeedbackWebPage feedback = ModelFactory.getFeedbackWebPage(user);
        feedback = feedbackWebPageRepository.save(feedback);

        boolean success = userService.deleteUser(user.getUserId());

        assertThat(success).isTrue();
        assertThat(userRepository.findById(user.getUserId()).isEmpty()).isTrue();
        assertThat(feedbackWebPageRepository.findById(feedback.getFeedbackId()).isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongUserId()
    {
        boolean success = userService.deleteUser(1);
        assertThat(success).isFalse();
    }

    @Test
    public void updateAll()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Image image = ModelFactory.getImage();
        image = imageRepository.save(image);

        UpdateUserRequest request = new UpdateUserRequest(
                "new name",
                false,
                "new streetAddress",
                "new postalCode",
                "new postOffice",
                "new email",
                "new password",
                image.getImageId()
        );

        UserResponse response = userService.updateUser(user.getUserId(), request);

        assertThat(user.getName()).isEqualTo(response.getName()).isEqualTo(request.getName());
        assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson()).isEqualTo(request.getIsPerson());
        assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(user.getEmail()).isEqualTo(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getImage().getImageId()).isEqualTo(response.getImageId()).isEqualTo(image.getImageId());
    }

    @Test
    public void updateNothing()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        User oldUser = ModelFactory.getUser(null);

        UpdateUserRequest request = new UpdateUserRequest();

        UserResponse response = userService.updateUser(user.getUserId(), request);

        assertThat(user.getUserId()).isEqualTo(response.getUserId()).isEqualTo(oldUser.getUserId());
        assertThat(user.getName()).isEqualTo(response.getName()).isEqualTo(oldUser.getName());
        assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson()).isEqualTo(oldUser.getIsPerson());
        assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(oldUser.getStreetAddress());
        assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(oldUser.getPostalCode());
        assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(oldUser.getPostOffice());
        assertThat(user.getEmail()).isEqualTo(response.getEmail()).isEqualTo(oldUser.getEmail());
    }

    @Test
    public void updateWrongUserId()
    {
        UpdateUserRequest request = new UpdateUserRequest();

        UserResponse response = userService.updateUser(1, request);

        assertThat(response).isNull();
    }

    @Test
    public void toggleRoleToAdmin()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        UserResponse response = userService.toggleRole(user.getUserId());

        assertThat(user.getRole()).isEqualTo(response.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void toggleRoleToUser()
    {
        User user = ModelFactory.getUser(null);
        user.setRole("ADMIN");
        user = userRepository.save(user);

        UserResponse response = userService.toggleRole(user.getUserId());

        assertThat(user.getRole()).isEqualTo(response.getRole()).isEqualTo("USER");
    }

    @Test
    public void toggleRoleWrongUserId()
    {
        UserResponse response = userService.toggleRole(1);

        assertThat(response).isNull();
    }

    @Test
    public void getAllWithUsers()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);
        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<UserResponse> responses = userService.getAllUsers();

        assertThat(users.size()).isEqualTo(2);
        for (int i = 0; i < 2; i++)
        {
            User user = users.get(i);
            UserResponse response = responses.get(i);

            assertThat(user.getUserId()).isEqualTo(response.getUserId());
            assertThat(user.getName()).isEqualTo(response.getName());
            assertThat(user.getIsPerson()).isEqualTo(response.getIsPerson());
            assertThat(user.getStreetAddress()).isEqualTo(response.getStreetAddress());
            assertThat(user.getPostalCode()).isEqualTo(response.getPostalCode());
            assertThat(user.getPostOffice()).isEqualTo(response.getPostOffice());
            assertThat(user.getEmail()).isEqualTo(response.getEmail());
        }
    }

    @Test
    public void getAllEmpty()
    {
        List<UserResponse> responses = userService.getAllUsers();

        assertThat(responses.size()).isZero();
    }


    @Test()
    public void when_user_register_without_email_it_should_throw_exception(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("testemail");
    }
    @Test
    public void when_save_user_it_should_return_user() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName("test user");

        UserResponse register = userService.register(registerUserRequest);

        assertThat(register.getName()).isSameAs(registerUserRequest.getName());


    }
}
