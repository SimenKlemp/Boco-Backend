package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static edu.ntnu.idatt2106.boco.util.Randomization.getEmailRandomly;
import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class UpdateUserRequestFactory implements FactoryBean<UpdateUserRequest> {

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @Override
    public UpdateUserRequest getObject() throws Exception {

        ImageFactory imageFactory=new ImageFactory();

        return UpdateUserRequest.builder()
                .name(getStringRandomly(6))
                .isPerson(true)
                .streetAddress(getStringRandomly(6))
                .postalCode(getStringRandomly(6))
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
        return false;
    }
}
