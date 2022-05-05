package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService
{
    private final BCryptPasswordEncoder encoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    RentalService rentalService;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    RatingService ratingService;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    FeedbackWebPageService feedbackWebPageService;
    @Autowired
    FeedbackWebPageRepository feedbackWebPageRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationRepository notificationRepository;

    public UserService()
    {
        encoder = new BCryptPasswordEncoder();
    }

    /**
     * A method for checking if login credentials are correct
     *
     * @param request user info
     * @return returns the user if the credentials match
     */
    public UserResponse login(LoginRequest request)
    {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        if (encoder.matches(request.getPassword(), user.getPassword()))
        {
            return Mapper.ToUserResponse(user);
        }

        return null;
    }

    /**
     * A method for registering a new user
     *
     * @param request user info
     * @return returns the newly created user
     */
    public UserResponse register(RegisterUserRequest request)
    {
        if (userRepository.existsByEmail(request.getEmail())) return null;

        Image image = null;
        if (request.getImageId() != null)
        {
            Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
            if (optionalImage.isPresent())
            {
                image = optionalImage.get();
            }
        }

        User user = new User(
                request.getName(),
                request.getIsPerson(),
                request.getStreetAddress(),
                request.getPostalCode(),
                request.getPostOffice(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                "USER",
                image
        );

        user = userRepository.save(user);
        return Mapper.ToUserResponse(user);
    }

    public UserResponse update(Long userId, UpdateUserRequest request)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        if (request.getName() != null) user.setName(request.getName());
        if (request.getIsPerson() != null) user.setIsPerson(request.getIsPerson());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getStreetAddress() != null) user.setStreetAddress(request.getStreetAddress());
        if (request.getPostalCode() != null) user.setPostalCode(request.getPostalCode());
        if (request.getPostOffice() != null) user.setPostOffice(request.getPostOffice());


        if (request.getPassword() != null)
        {
            String encodedPassword = encoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }

        if (request.getImageId() != null)
        {
            Image prevImage = user.getImage();

            Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
            if (optionalImage.isPresent()) user.setImage(optionalImage.get());
            user = userRepository.save(user);

            if (prevImage != null && !Objects.equals(request.getImageId(), prevImage.getImageId()))
            {
                imageService.delete(prevImage.getImageId());
            }
        }

        user = userRepository.save(user);
        return Mapper.ToUserResponse(user);
    }

    public UserResponse toggleRole(Long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        if (user.getRole().equals("USER"))
        {
            user.setRole("ADMIN");
        }
        else
        {
            user.setRole("USER");
        }

        user = userRepository.save(user);
        return Mapper.ToUserResponse(user);
    }

    public List<UserResponse> getAll()
    {
        List<User> users = userRepository.findAll();
        return Mapper.ToUserResponses(users);
    }

    public boolean delete(Long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return false;
        User user = optionalUser.get();

        for (Rating rating : ratingRepository.findAllByUser(user))
        {
            ratingService.delete(rating.getRatingId());
        }

        for (Notification notification : notificationRepository.findAllByUser(user))
        {
            notificationService.delete(notification.getNotificationId());
        }

        for (Item item : itemRepository.findAllByUser(user))
        {
            itemService.delete(item.getItemId());
        }

        for (Rental rental : rentalRepository.findAllByUser(user))
        {
            rentalService.delete(rental.getRentalId());
        }

        for (FeedbackWebPage feedbackWebPage : feedbackWebPageRepository.findAllByUser(user))
        {
            feedbackWebPageService.delete(feedbackWebPage.getFeedbackId());
        }

        Image image = user.getImage();

        userRepository.delete(user);

        if (image != null)
        {
            imageService.delete(user.getImage().getImageId());
        }

        return true;
    }

}

