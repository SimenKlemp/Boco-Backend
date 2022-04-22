package edu.ntnu.idatt2106.boco.payload.request;

import javax.validation.constraints.NotBlank;

public class ItemRegisterRequest {
    @NotBlank
    private String streetAddress;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String postOffice;

    @NotBlank
    private float price;

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

    public ItemRegisterRequest(String streetAddress, String postalCode, String postOffice, float price, String description, String category, String title, Long userId, Long imageId){
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postOffice = postOffice;
        this.price = price;
        this.description = description;
        this.category = category;
        this.title = title;
        this.userId = userId;
        this.imageId = imageId;
    }


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
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
