package edu.ntnu.idatt2106.boco.factories.modelFactroies;
import edu.ntnu.idatt2106.boco.models.User;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.UUID;

import static edu.ntnu.idatt2106.boco.util.Randomization.getEmailRandomly;
import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;



public class UserFactory implements FactoryBean<User> {

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @Override
    public User getObject() throws Exception {

        ImageFactory imageFactory = new ImageFactory();
        return  User.builder()
                .userId(UUID.randomUUID().getLeastSignificantBits())
                .name(getStringRandomly(6))
                .isPerson(true)
                .streetAddress(getStringRandomly(6))
                .postalCode (getStringRandomly(6))
                .postOffice (getStringRandomly(6))
                .email(getEmailRandomly())
                .password (encoder.encode(getStringRandomly(15)))
                .role ("ADMIN")
                .image(imageFactory.getObject())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
