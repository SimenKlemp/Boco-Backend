package edu.ntnu.idatt2106.boco.factories.modelFactroies;

import edu.ntnu.idatt2106.boco.factories.requestFactroies.RegisterItemRequestFactory;
import edu.ntnu.idatt2106.boco.models.Item;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class ItemFactory implements FactoryBean<Item> {

    private UserFactory userFactory=new UserFactory();
    private ImageFactory imageFactory=new ImageFactory();
    private RegisterItemRequestFactory itemRequest=new RegisterItemRequestFactory();
    private Random random=new Random();
    @Override
    public Item getObject() throws Exception {


        return Item.builder()
                .itemId(UUID.randomUUID().getLeastSignificantBits())
                .streetAddress(getStringRandomly(10))
                .postalCode(getStringRandomly(10))
                .postOffice(getStringRandomly(10))
                .price(0+ random.nextFloat()*1000)
                .category(getStringRandomly(10))
                .title(getStringRandomly(10))
                .publicityDate(new Date())
                .isPickupable(random.nextBoolean())
                .isDeliverable(random.nextBoolean())
                .image(null)
                .user(userFactory.getObject())
                .build();

    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

}
