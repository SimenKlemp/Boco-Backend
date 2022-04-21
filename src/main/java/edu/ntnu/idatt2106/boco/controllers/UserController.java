package edu.ntnu.idatt2106.boco.controllers;

import javax.validation.Valid;

import edu.ntnu.idatt2106.boco.token.TokenComponent;
import edu.ntnu.idatt2106.boco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;

import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController
{
  @Autowired
  private UserService userService;

  @Autowired
  private TokenComponent tokenComponent;

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) throws UnsupportedEncodingException
  {
    User user = userService.login(loginRequest);
    String token = tokenComponent.generateToken(user.getUserId(), user.getRole());
    return new LoginResponse(
            token,
            user.getUserId(),
            user.getEmail(),
            user.getRole()
    );
  }

  @PostMapping("/register")
  public LoginResponse register(@Valid @RequestBody RegisterUserRequest request) throws UnsupportedEncodingException
  {
    User user = userService.register(request);
    String token = tokenComponent.generateToken(user.getUserId(), user.getRole());
    return new LoginResponse(
            token,
            user.getUserId(),
            user.getEmail(),
            user.getRole()
    );
  }
}
