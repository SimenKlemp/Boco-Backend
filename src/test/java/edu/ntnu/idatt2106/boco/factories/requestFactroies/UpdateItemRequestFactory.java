package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import org.springframework.beans.factory.FactoryBean;

import java.util.Random;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class UpdateItemRequestFactory implements FactoryBean<UpdateItemRequest> {
    @Override
    public UpdateItemRequest getObject() throws Exception {

        Random random=new Random();
        UserFactory userFactory=new UserFactory();
        ImageFactory imageFactory=new ImageFactory();

        return UpdateItemRequest.builder()
                .streetAddress(getStringRandomly(6))
                .postalCode(getStringRandomly(6))
                .postOffice(getStringRandomly(6))
                .price(random.nextFloat())
                .description(getStringRandomly(6))
                .category(getStringRandomly(10))
                .title(getStringRandomly(10))
                .isPickupable(random.nextBoolean())
                .isDeliverable(random.nextBoolean())
                .userId(userFactory.getObject().getUserId())
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
