package edu.ntnu.idatt2106.boco.controllers;

import javax.validation.Valid;

import edu.ntnu.idatt2106.boco.token.TokenComponent;
import edu.ntnu.idatt2106.boco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @DeleteMapping(value="/user/delete")
  public ResponseEntity<String> deleteUserByEmail(@RequestBody User user ){
    try{

      if( userService.deleteUserByEmail(user)){
        return new ResponseEntity("Error: User not found", HttpStatus.OK);
      }
      return new ResponseEntity("User has been deleted successfully", HttpStatus.OK);
    }catch (Exception ex){
      return new ResponseEntity("Error: Can not delete ",HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping (value="/user/update")
  public ResponseEntity<String> updateUserInfo(@PathVariable("email")String email, @RequestBody User user) {
    try {

      if (userService.updateUserByEmail(email,user)) {
        return new ResponseEntity("Error: User not found", HttpStatus.OK);
      }

      return new ResponseEntity("User has been updated",HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity("Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
