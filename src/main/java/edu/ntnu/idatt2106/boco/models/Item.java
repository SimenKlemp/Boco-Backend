package edu.ntnu.idatt2106.boco.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "itemId")
        })

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long itemId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "address")
    private String address;

    @NotBlank
    @Size(max = 20)
    @Column(name = "price")
    private String price;

    @NotBlank
    @Size(max = 20)
    @Column(name = "description")
    private String description;

    @NotBlank
    @Size(max = 20)
    @Column(name = "category")
    private String category;

    @NotBlank
    @Size(max = 20)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Size(max = 20)
    @ManyToOne
    private User user;

    @NotBlank
    @Size(max = 20)
    @Column(name = "imageId")
    private Long imageid;

    public Item(){

    }

    public Item(String address, String price, String description, String category, String title, User user, Long imageid){
        this.address = address;
        this.price = price;
        this.description = description;
        this.category = category;
        this.title = title;
        this.user = user;
        this.imageid = imageid;

    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

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

    public User getUser() {
        return user;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public Long getImageid() {
        return imageid;
    }

    public void setImageid(Long imageid) {
        this.imageid = imageid;
    }
}
