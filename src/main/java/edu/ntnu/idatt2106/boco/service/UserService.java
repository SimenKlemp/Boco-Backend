package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User login(LoginRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail()).get();
        String requestPassword = encoder.encode(request.getPassword());
        if (requestPassword.equals(user.getPassword()))
        {
            return user;
        }
        throw new IllegalArgumentException("Email and/or password is wrong");
    }

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
}

