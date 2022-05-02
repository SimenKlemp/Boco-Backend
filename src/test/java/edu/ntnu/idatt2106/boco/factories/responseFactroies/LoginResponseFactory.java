package edu.ntnu.idatt2106.boco.factories.responseFactroies;

import edu.ntnu.idatt2106.boco.factories.requestFactroies.LoginRequestFactory;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.springframework.beans.factory.FactoryBean;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class LoginResponseFactory implements FactoryBean<LoginResponse> {
    private UserResponseFactory userResponseFactory=new UserResponseFactory();

    public LoginResponse getObject(LoginRequestFactory loginRequest, TokenComponent token) throws Exception {

        return LoginResponse.builder()
                .token(getStringRandomly(10))
                .userInfo(userResponseFactory.getObject())
                .build();
    }

    @Override
    public LoginResponse getObject() throws Exception {
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
