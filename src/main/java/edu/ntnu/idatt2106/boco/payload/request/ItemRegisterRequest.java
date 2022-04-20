package edu.ntnu.idatt2106.boco.payload.request;

import javax.validation.constraints.NotBlank;

public class ItemRegisterRequest {
    @NotBlank
    private String address;

    @NotBlank
    private String price;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NotBlank
    private String title;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long imageId;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
