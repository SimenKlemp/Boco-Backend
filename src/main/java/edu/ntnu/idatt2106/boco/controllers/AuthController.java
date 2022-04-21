package edu.ntnu.idatt2106.boco.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import edu.ntnu.idatt2106.boco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.security.jwt.JwtUtils;
import edu.ntnu.idatt2106.boco.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    String role = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList()).get(0);

    return ResponseEntity.ok(new LoginResponse(
            jwt,
            userDetails.getId(),
            userDetails.getEmail(),
            role));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest request)
  {
    if (userService.userExists(request.getEmail()))
    {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = userService.register(request);

    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    return ResponseEntity.ok(new LoginResponse(
            jwt,
            user.getUserId(),
            user.getEmail(),
            user.getAddress()));
  }

  @DeleteMapping(value="api/auth/user/delete")
  public ResponseEntity<String> deleteUserById(@RequestBody User user ){
    try{
    Optional<User> email=userRepository.findByEmail(user.getEmail());
    if(email.isEmpty()){
      return new ResponseEntity("Error: User not found", HttpStatus.OK);
    }
    userRepository.deleteById(user.getUserId());
    return new ResponseEntity("User has been deleted successfully", HttpStatus.OK);
    }catch (Exception ex){
      return new ResponseEntity("Error: Can not delete ",HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @PutMapping (value="api/auth/user/update")
  public ResponseEntity<String> updateUserInfo(@PathVariable("email")String email, @RequestBody User user) {
    try {
      Optional<User> updatedUser = userRepository.findByEmail(email);
      if (updatedUser.isEmpty()) {
        return new ResponseEntity("Error: User not found", HttpStatus.OK);
      }
      updatedUser.get().setName(user.getName());
      updatedUser.get().setEmail(user.getEmail());
      updatedUser.get().setAddress(user.getAddress());
      updatedUser.get().setPassword(user.getPassword());
      userRepository.save(updatedUser.get());
      return new ResponseEntity("User has been updated",HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity("Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
