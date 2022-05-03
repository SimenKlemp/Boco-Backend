package edu.ntnu.idatt2106.boco.factories.modelFactroies;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import org.springframework.beans.factory.FactoryBean;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class FeedBackWebPageFactory implements FactoryBean<FeedbackWebPage> {
    UserFactory userFactory = new UserFactory();
    @Override
    public FeedbackWebPage getObject() throws Exception {

        return FeedbackWebPage.builder()
                .message(getStringRandomly(10))
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
