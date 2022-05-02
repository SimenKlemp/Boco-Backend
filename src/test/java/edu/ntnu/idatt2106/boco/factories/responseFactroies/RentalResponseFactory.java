package edu.ntnu.idatt2106.boco.factories.responseFactroies;

import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import org.springframework.beans.factory.FactoryBean;

public class RentalResponseFactory implements FactoryBean<RentalResponse> {
    @Override
    public RentalResponse getObject() throws Exception {
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
