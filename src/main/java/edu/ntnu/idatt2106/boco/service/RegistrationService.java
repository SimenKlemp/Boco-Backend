package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    UserRepository registrationRepository;

    public User registrationUser(User user){
        registrationRepository.save(user);
        return null;


    }
}

