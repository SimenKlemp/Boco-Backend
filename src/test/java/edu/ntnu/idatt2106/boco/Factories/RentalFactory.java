package edu.ntnu.idatt2106.boco.Factories;

import edu.ntnu.idatt2106.boco.models.Rental;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.util.Random;

import static edu.ntnu.idatt2106.boco.utils.Randomization.getStringRandomly;

public class RentalFactory implements FactoryBean<Rental> {
    @Override
    public Rental getObject() throws Exception {
        int pick = new Random().nextInt(Rental.Status.values().length);
        int pick1 = new Random().nextInt(Rental.Status.values().length);
        UserFactory userFactory=new UserFactory();
        ItemFactory itemFactory=new ItemFactory();
        Random random =new Random();
        return Rental.builder()
                .message(getStringRandomly(20))
                .startDate(new Date())
                .endDate(new Date())
                .status(Rental.Status.values()[pick])
                .user(userFactory.getObject())
                .item(itemFactory.getObject())
                .deliveryInfo(Rental.DeliverInfo.values()[pick1])
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
