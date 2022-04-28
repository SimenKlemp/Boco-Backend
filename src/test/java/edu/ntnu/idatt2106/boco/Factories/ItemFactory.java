package edu.ntnu.idatt2106.boco.Factories;

import edu.ntnu.idatt2106.boco.models.Item;
import org.springframework.beans.factory.FactoryBean;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;

import static edu.ntnu.idatt2106.boco.utils.Randomization.getStringRandomly;

public class ItemFactory implements FactoryBean<Item> {
    @Override
    public Item getObject() throws Exception {

        UserFactory userFactory=new UserFactory();
        ImageFactory imageFactory=new ImageFactory();
        Random random=new Random();
        return Item.builder()
                .streetAddress(getStringRandomly(6))
                .postalCode(getStringRandomly(6))
                .postOffice(getStringRandomly(6))
                .price(0+random.nextFloat()*500)
                .category(getStringRandomly(6))
                .title(getStringRandomly(6))
                .publicityDate(new Date())
                .isPickupable(random.nextBoolean())
                .isDeliverable(random.nextBoolean())
                .image(imageFactory.getObject())
                .user(userFactory.getObject())
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
