package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.util.Random;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class RegisterRentalRequestFactory implements FactoryBean<RegisterRentalRequest> {
    @Override
    public RegisterRentalRequest getObject() throws Exception {
        UserFactory userFactory=new UserFactory();
        ImageFactory imageFactory = new ImageFactory();
        int pick = new Random().nextInt(Rental.DeliverInfo.values().length);

        return RegisterRentalRequest.builder()
                .message(getStringRandomly(10))
                .startDate(new Date())
                .endDate(new Date())
                .userId(userFactory.getObject().getUserId())
                .itemId(imageFactory.getObject().getImageId())
                .deliveryInfo(Rental.DeliverInfo.values()[pick])
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
