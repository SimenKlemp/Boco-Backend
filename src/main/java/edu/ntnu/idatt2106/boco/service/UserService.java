package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService()
    {
        encoder = new BCryptPasswordEncoder();
    }

    /**
     * A method for checking if login credentials are correct
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
     * @param request user info
     * @return returns the newly created user
     */
    public UserResponse register(RegisterUserRequest request)
    {
        if (userRepository.existsByEmail(request.getEmail())) return null;

        Image image = null;
        Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
        if (optionalImage.isPresent())
        {
            image = optionalImage.get();
        }

        User user = new User(
                request.getName(),
                request.isPerson(),
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

    public boolean deleteUser(long userId)
    {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) return false;

        userRepository.delete(user.get());
        return true;
    }

    public UserResponse updateUser(long userId, UpdateUserRequest request)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        if (request.getName() != null) user.setName(request.getName());
        if (request.getIsPerson() != null) user.setPerson(request.getIsPerson());
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
            if (prevImage != null && !Objects.equals(request.getImageId(), prevImage.getImageId()))
            {
                imageRepository.delete(prevImage);
            }

            Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
            if (optionalImage.isPresent()) user.setImage(optionalImage.get());
        }

        user = userRepository.save(user);
        return Mapper.ToUserResponse(user);
    }
}

