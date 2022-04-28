package edu.ntnu.idatt2106.boco.Factories;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.User;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static edu.ntnu.idatt2106.boco.utils.Randomization.getEmailRandomly;
import static edu.ntnu.idatt2106.boco.utils.Randomization.getStringRandomly;



public class UserFactory implements FactoryBean<User> {

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @Override
    public User getObject() throws Exception {

        ImageFactory imageFactory = new ImageFactory();
        return  User.builder()
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
