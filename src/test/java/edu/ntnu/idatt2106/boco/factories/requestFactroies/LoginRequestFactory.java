package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static edu.ntnu.idatt2106.boco.util.Randomization.*;


public class LoginRequestFactory implements FactoryBean<LoginRequest> {

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    @Override
    public LoginRequest getObject() throws Exception {

        return LoginRequest.builder()
                .email(getEmailRandomly())
                .password(encoder.encode(getStringRandomly(10)))
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
