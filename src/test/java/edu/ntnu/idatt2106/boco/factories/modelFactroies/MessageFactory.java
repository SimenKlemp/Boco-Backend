package edu.ntnu.idatt2106.boco.factories.modelFactroies;

import edu.ntnu.idatt2106.boco.models.Message;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class MessageFactory implements FactoryBean<Message> {

    private UserFactory user =new UserFactory();
    private RentalFactory rental = new RentalFactory();
    @Override
    public Message getObject() throws Exception {

        return Message.builder()
            .text(getStringRandomly(10))
                .isByUser(true)
                .date(new Date())
                .user(user.getObject())
                .rental (rental.getObject())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
