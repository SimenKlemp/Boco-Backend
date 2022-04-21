package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    public boolean userExists(String email)
    {
        return userRepository.existsByEmail(email);
    }

    public User register(RegisterUserRequest request)
    {
        User user = new User(
                request.getName(),
                request.isPerson(),
                request.getAddress(),
                request.getEmail(),
                encoder.encode(request.getPassword())

        );

        userRepository.save(user);

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

