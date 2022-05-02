package edu.ntnu.idatt2106.boco.factories.responseFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import org.springframework.beans.factory.FactoryBean;

public class UserResponseFactory implements FactoryBean<UserResponse> {
    @Override
    public UserResponse getObject() throws Exception {

        UserFactory userFactory=new UserFactory();
        return UserResponse.builder()
                .name(userFactory.getObject().getName())
                .isPerson(userFactory.getObject().getIsPerson())
                .streetAddress(userFactory.getObject().getStreetAddress())
                .postalCode(userFactory.getObject().getPostalCode())
                .postOffice(userFactory.getObject().getPostOffice())
                .email(userFactory.getObject().getEmail())
                .role(userFactory.getObject().getRole())
                .imageId(userFactory.getObject().getImage().getImageId())
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
