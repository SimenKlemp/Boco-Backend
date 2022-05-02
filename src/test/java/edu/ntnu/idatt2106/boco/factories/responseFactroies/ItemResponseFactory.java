package edu.ntnu.idatt2106.boco.factories.responseFactroies;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ItemFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.RegisterItemRequestFactory;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.util.UUID;

public class ItemResponseFactory implements FactoryBean<ItemResponse> {
    private ItemFactory itemFactory=new ItemFactory();
    private UserResponseFactory userResponseFactory=new UserResponseFactory();

    public ItemResponse getObject(RegisterItemRequest registerItem, UserResponse user) throws Exception {

        return ItemResponse.builder()
                .itemId(UUID.randomUUID().getLeastSignificantBits())
                .streetAddress(registerItem.getStreetAddress())
                .postalCode(registerItem.getPostalCode())
                .postOffice(registerItem.getPostOffice())
                .price(registerItem.getPrice())
                .description(registerItem.getDescription())
                .category(registerItem.getCategory())
                .title(registerItem.getTitle())
                .imageId(null)
                .publicityDate(new Date())
                .isPickupable(registerItem.getIsPickupable())
                .isDeliverable(registerItem.getIsDeliverable())
                .user(user)
                .build();

    }

    @Override
    public ItemResponse getObject() throws Exception {
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
