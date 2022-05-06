package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Indexed
@Entity
@Table(name = "item", uniqueConstraints = {@UniqueConstraint(columnNames = "itemId")})
public class Item
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long itemId;

    @NotBlank
    @Column(name = "streetAddress", length = 200)
    private String streetAddress;

    @NotBlank
    @Column(name = "postalCode", length = 50)
    private String postalCode;

    @NotBlank
    @Column(name = "postOffice", length = 50)
    private String postOffice;

    @Column(name = "lat")
    @Latitude
    private float lat;

    @Column(name = "lng")
    @Longitude
    private float lng;


    @Field
    @SortableField
    @Column(name = "price")
    private float price;

    @NotBlank
    @Field
    @Column(name = "description", length = 200)
    private String description;

    @NotBlank
    @Field(analyze = Analyze.NO)
    @Column(name = "category", length = 50)
    private String category;

    @NotBlank
    @Field
    @Column(name = "title", length = 50)
    private String title;

    @Field(analyze = Analyze.NO)
    @SortableField
    @Column(name = "publicityDate")
    private Date publicityDate;

    @Field(analyze = Analyze.NO)
    @Column(name = "isPickupable")
    private Boolean isPickupable;

    @Field(analyze = Analyze.NO)
    @Column(name = "isDeliverable")
    private Boolean isDeliverable;

    @JoinColumn(name = "imageId")
    @OneToOne
    private Image image;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Item(String streetAddress, String postalCode, String postOffice, float lat, float lng, float price, String description, String category, String title, Date publicityDate, Boolean isPickupable, Boolean isDeliverable, Image image, User user)
    {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postOffice = postOffice;
        this.price = price;
        this.description = description;
        this.category = category;
        this.title = title;
        this.publicityDate = publicityDate;
        this.isPickupable = isPickupable;
        this.isDeliverable = isDeliverable;
        this.image = image;
        this.user = user;
        this.lat = lat;
        this.lng = lng;
    }
}
