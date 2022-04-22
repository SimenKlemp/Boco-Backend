package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    public UserService()
    {
        encoder = new BCryptPasswordEncoder();
    }

    /**
     * A method for checking if login credentials are correct
     * @param request user info
     * @return returns the user if the credentials match
     */
    public User login(LoginRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail()).get();
        if (encoder.matches(request.getPassword(), user.getPassword()))
        {
            return user;
        }
        throw new IllegalArgumentException("Email and/or password is wrong");
    }

    /**
     * A method for registering a new user
     * @param request user info
     * @return returns the newly created user
     */
    public User register(RegisterUserRequest request)
    {
        if (userRepository.existsByEmail(request.getEmail()))
        {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = new User(
                request.getName(),
                request.getIsPerson(),
                request.getAddress(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                "USER"
        );

        user = userRepository.save(user);
        return user;
    }



    public boolean deleteUserByEmail(User user){
        Optional<User> userEmail=userRepository.findByEmail(user.getEmail());
        if(userEmail.isEmpty()){
            return false;
        }
            userRepository.deleteById(user.getUserId());
            return true;

    }

    public boolean updateUserByEmail(String email,User user){
        Optional<User> updatedUser = userRepository.findByEmail(email);
        if(updatedUser.isEmpty()){
            return false;
        }
        updatedUser.get().setName(user.getName());
        updatedUser.get().setEmail(user.getEmail());
        updatedUser.get().setAddress(user.getAddress());
        updatedUser.get().setPassword(user.getPassword());
        userRepository.save(updatedUser.get());
        return true;

    }
}

