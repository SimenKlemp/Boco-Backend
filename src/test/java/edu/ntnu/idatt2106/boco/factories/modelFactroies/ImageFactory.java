package edu.ntnu.idatt2106.boco.factories.modelFactroies;

import edu.ntnu.idatt2106.boco.models.Image;
import org.springframework.beans.factory.FactoryBean;

import java.util.UUID;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class ImageFactory implements FactoryBean<Image> {
    @Override
    public Image getObject() throws Exception {

        return Image.builder()
                .name(getStringRandomly(6))
                .content(null)
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
