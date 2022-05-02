package edu.ntnu.idatt2106.boco.factories.modelFactroies;

import edu.ntnu.idatt2106.boco.models.Rental;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RentalFactory implements FactoryBean<Rental> {
    int pick = new Random().nextInt(Rental.Status.values().length);
    int pick1 = new Random().nextInt(Rental.DeliverInfo.values().length);
    UserFactory userFactory=new UserFactory();
    ItemFactory itemFactory=new ItemFactory();
    Random random =new Random();
    @Override
    public Rental getObject() throws Exception {

        return Rental.builder()
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
