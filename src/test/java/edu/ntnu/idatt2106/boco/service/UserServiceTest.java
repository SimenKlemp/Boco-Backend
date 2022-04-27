package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
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

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.isPerson()).isEqualTo(request.isPerson());
        assertThat(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
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

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.isPerson()).isEqualTo(request.isPerson());
        assertThat(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getImageId()).isEqualTo(image.getImageId());
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

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.isPerson()).isEqualTo(request.isPerson());
        assertThat(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
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

        assertThat(response.getUserId()).isEqualTo(user.getUserId());
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.isPerson()).isEqualTo(user.isPerson());
        assertThat(response.getStreetAddress()).isEqualTo(user.getStreetAddress());
        assertThat(response.getPostalCode()).isEqualTo(user.getPostalCode());
        assertThat(response.getPostOffice()).isEqualTo(user.getPostOffice());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
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

    }

    @Test
    public void updateAll()
    {

    }

    @Test
    public void updateNothing()
    {

    }

    @Test
    public void updateWrongUserId()
    {

    }

    @Test
    public void toggleRoleToAdmin()
    {

    }

    @Test
    public void toggleRoleToUser()
    {

    }

    @Test
    public void toggleRoleWrongUserId()
    {

    }

    @Test
    public void getAllWithUsers()
    {

    }

    @Test
    public void getAllEmpty()
    {

    }
}
