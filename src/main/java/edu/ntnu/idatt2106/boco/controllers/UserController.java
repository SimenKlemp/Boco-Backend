package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import edu.ntnu.idatt2106.boco.service.UserService;
import edu.ntnu.idatt2106.boco.utility.Utility;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/user")
@EnableAutoConfiguration
@SuperBuilder
@CrossOrigin
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private TokenComponent tokenComponent;

    @Autowired
    private JavaMailSender mailSender;
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

            UserResponse user = userService.toggleRole(userId);
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

    @PostMapping("/forgot_password")
    public ResponseEntity<String> processForgotPassword(HttpServletRequest request) {
        String email = request.getParameter("email");
        //
        String token = UUID.randomUUID().toString();

        try {
            if( userService.updateResetPasswordToken(token, email)) {
                String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
                sendEmail(email, resetPasswordLink);
                return new ResponseEntity("We have sent a reset password link to your email. Please check.", HttpStatus.OK);
            }
            return new ResponseEntity ("There is no user with email "+email,HttpStatus.OK);
        } catch (UnsupportedEncodingException | MessagingException e) {
            return new ResponseEntity("Error while sending email",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
    A method for changing a password and sending email for user to
    Params:String recipientEmail, String link
    */
    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("", "");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";


        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
    /*
    A method to reset  a password
    Params:HttpServletRequest request
    returns an assuring message to user
    */

    @PostMapping("/reset_password")
    public ResponseEntity<String> processResetPassword(HttpServletRequest request) {
        //
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        try{
            if (user == null) {
                return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
            }
            userService.updatePassword(user, password);

            return new ResponseEntity( "You have successfully changed your password.",HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity("Error while sending email",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
