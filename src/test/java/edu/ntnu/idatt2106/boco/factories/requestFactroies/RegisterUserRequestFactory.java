package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static edu.ntnu.idatt2106.boco.util.Randomization.getEmailRandomly;
import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;


public class RegisterUserRequestFactory implements FactoryBean<RegisterUserRequest> {

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    private ImageFactory imageFactory=new ImageFactory();
    @Override
    public RegisterUserRequest getObject() throws Exception {

        return RegisterUserRequest.builder()
                .name(getStringRandomly(10))
                .isPerson(true)
                .streetAddress(getStringRandomly(10))
                .postalCode(getStringRandomly(10))
                .postOffice(getStringRandomly(10))
                .email(getEmailRandomly())
                .password(encoder.encode(getStringRandomly(10)))
                .imageId(imageFactory.getObject().getImageId())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
