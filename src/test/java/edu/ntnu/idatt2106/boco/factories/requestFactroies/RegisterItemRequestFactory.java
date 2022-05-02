package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import org.springframework.beans.factory.FactoryBean;

import java.util.Random;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class RegisterItemRequestFactory implements FactoryBean<RegisterItemRequest> {
    @Override
    public RegisterItemRequest getObject() throws Exception {
        Random random =new Random();
        UserFactory userFactory=new UserFactory();
        ImageFactory imageFactory=new ImageFactory();

        return RegisterItemRequest.builder()
                .streetAddress(getStringRandomly(6))
                .postalCode(getStringRandomly(6))
                .postOffice(getStringRandomly(6))
                .price(0+random.nextFloat()*500)
                .description(getStringRandomly(20))
                .category(getStringRandomly(10))
                .title(getStringRandomly(10))
                .isPickupable(random.nextBoolean())
                .isDeliverable(random.nextBoolean())
                .userId(userFactory.getObject().getUserId())
                .imageId(userFactory.getObject().getImage().getImageId())
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
