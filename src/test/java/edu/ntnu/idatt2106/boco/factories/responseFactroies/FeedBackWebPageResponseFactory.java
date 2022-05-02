package edu.ntnu.idatt2106.boco.factories.responseFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.FeedBackWebPageFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.FeedbackWebPageRequestFactory;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import org.springframework.beans.factory.FactoryBean;

import java.util.Random;
import java.util.UUID;

public class FeedBackWebPageResponseFactory implements FactoryBean<FeedbackWebPageResponse> {
   Random random=new Random();
    public FeedbackWebPageResponse getObject(FeedbackWebPageRequestFactory feedbackWebPageRequest, UserResponseFactory user) throws Exception {

        return FeedbackWebPageResponse.builder()
                .feedbackId(UUID.randomUUID().getLeastSignificantBits())
                .message(feedbackWebPageRequest.getObject().getMessage())
                .user(user.getObject())
                .build();
    }

    @Override
    public FeedbackWebPageResponse getObject() throws Exception {
        return null;
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
