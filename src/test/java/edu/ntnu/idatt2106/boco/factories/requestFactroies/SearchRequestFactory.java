package edu.ntnu.idatt2106.boco.factories.requestFactroies;

import edu.ntnu.idatt2106.boco.payload.request.SearchRequest;
import org.springframework.beans.factory.FactoryBean;

import java.util.Random;

import static edu.ntnu.idatt2106.boco.util.Randomization.getStringRandomly;

public class SearchRequestFactory implements FactoryBean<SearchRequest> {
    @Override
    public SearchRequest getObject() throws Exception {

        Random random =new Random();
        int pick = random.nextInt(SearchRequest.SortField.values().length);
        return SearchRequest.builder()
                .text(getStringRandomly(10))
                .page(1+random.nextInt()*10)
                .pageSize(1+random.nextInt()*100)
                .sortField(SearchRequest.SortField.values()[pick])
                .minPrice(0+random.nextFloat())
                .maxPrice(1+random.nextFloat())
                .mustBePickupable(random.nextBoolean())
                .mustBeDeliverable(random.nextBoolean())
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
