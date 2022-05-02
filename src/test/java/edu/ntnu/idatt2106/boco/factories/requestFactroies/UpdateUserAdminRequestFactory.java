package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserAdminRequest;
import org.springframework.beans.factory.FactoryBean;

public class UpdateUserAdminRequestFactory implements FactoryBean<UpdateUserAdminRequest> {
    @Override
    public UpdateUserAdminRequest getObject() throws Exception {

        UserFactory userFactory=new UserFactory();
        return UpdateUserAdminRequest.builder()
                .userId(userFactory.getObject().getUserId())
                .role("ADMIN")
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
