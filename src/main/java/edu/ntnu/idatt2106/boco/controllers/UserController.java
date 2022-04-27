package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.UpdateUserAdminRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import edu.ntnu.idatt2106.boco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;

import java.util.List;


@RestController
@RequestMapping(value = "/user")
@EnableAutoConfiguration
@CrossOrigin
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private TokenComponent tokenComponent;

    /**
     * A method for login and returning token in login response
     * @param request Email and password
     * @return returns a LoginResponse containing token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request)
    {
        try
        {
            UserResponse user = userService.login(request);
            if (user == null)
            {
                return new ResponseEntity("Email or password is incorrect", HttpStatus.NO_CONTENT);
            }

            String token = tokenComponent.generateToken(user.getUserId(), user.getRole());
            LoginResponse loginResponse = new LoginResponse(token, user);

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Could not login", HttpStatus.NO_CONTENT);
        }
    }

    /**
     * A method for registering a new user and returning token in login response
     * @param request user info
     * @return returns a LoginResponse containing token and user info
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterUserRequest request)
    {
        try
        {
            UserResponse user = userService.register(request);
            if (user == null)
            {
                return new ResponseEntity("Email is already in use", HttpStatus.NO_CONTENT);
            }

            String token = tokenComponent.generateToken(user.getUserId(), user.getRole());
            LoginResponse loginResponse = new LoginResponse(token, user);

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Could not register", HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping(value="/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId){
        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            boolean success = userService.deleteUser(userId);
            if(!success)
            {
                return new ResponseEntity<>("Error: User not found", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("User has been deleted successfully", HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error: Can not delete ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping (value="/update/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("userId") long userId, @RequestBody UpdateUserRequest request) {
        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            UserResponse user = userService.updateUser(userId, request);
            if (user == null)
            {
                return new ResponseEntity("Error: User not found", HttpStatus.OK);
            }
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping (value="/updateUserAdmin/{userId}")
    public ResponseEntity<UserResponse> updateUserRoleAdmin(@PathVariable("userId") long userId) {
        try
        {
            if (!tokenComponent.isAdmin())
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            UserResponse user = userService.updateUserRoleAdmin(userId);
            if (user == null)
            {
                return new ResponseEntity("Error: User not found", HttpStatus.OK);
            }
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers()
    {
        try
        {
            List<UserResponse> users = userService.getAllUsers();
            if (users == null || users.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Could not fetch all users", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
