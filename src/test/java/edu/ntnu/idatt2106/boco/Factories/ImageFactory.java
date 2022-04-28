package edu.ntnu.idatt2106.boco.Factories;

import edu.ntnu.idatt2106.boco.models.Image;
import org.springframework.beans.factory.FactoryBean;
import org.testcontainers.shaded.org.apache.commons.lang.math.RandomUtils;

import java.security.SecureRandom;
import java.util.Random;

import static edu.ntnu.idatt2106.boco.utils.Randomization.getStringRandomly;

public class ImageFactory implements FactoryBean<Image> {
    Random random =new Random();
    byte[] bytes=new byte[4];
    @Override
    public Image getObject() throws Exception {
        /**
         * public Image(String name, byte[] content)
         *     {
         *         this.name = name;
         *         this.content = content;
         *     }
         */


        return Image.builder()
                .name(getStringRandomly(6))
                .content(random.nextBytes(bytes));
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
