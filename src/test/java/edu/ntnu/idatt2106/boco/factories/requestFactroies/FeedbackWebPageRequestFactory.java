package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.payload.request.FeedbackWebPageRequest;
import org.springframework.beans.factory.FactoryBean;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class FeedbackWebPageRequestFactory implements FactoryBean<FeedbackWebPageRequest> {
    UserFactory userFactory=new UserFactory();
    @Override
    public FeedbackWebPageRequest getObject() throws Exception {
        return FeedbackWebPageRequest.builder()
                .message(getStringRandomly(6))
                .userId(userFactory.getObject().getUserId())
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
