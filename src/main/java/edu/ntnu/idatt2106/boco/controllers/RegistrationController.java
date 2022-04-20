package edu.ntnu.idatt2106.boco.controllers;


import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/registration")
@EnableAutoConfiguration
@CrossOrigin
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        logger.info("Prøver å opprette bruker i service");
        return registrationService.registrationUser(user);
    }
}
