package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Indexed
@Entity
@Table(name = "item", uniqueConstraints = { @UniqueConstraint(columnNames = "itemId") })
public class Item
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long itemId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "streetAddress")
    private String streetAddress;

    @NotBlank
    @Size(max = 20)
    @Column(name = "postalCode")
    private String postalCode;

    @NotBlank
    @Size(max = 20)
    @Column(name = "postOffice")
    private String postOffice;

    @Column(name = "price")
    private float price;

    @NotBlank
    @Size(max = 20)
    @Field
    @Column(name = "description")
    private String description;

    @NotBlank
    @Size(max = 20)
    @Field
    @Column(name = "category")
    private String category;

    @NotBlank
    @Size(max = 20)
    @Field
    @Column(name = "title")
    private String title;

    @JoinColumn(name = "imageId")
    @OneToOne
    private Image image;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @Column(name = "publicityDate")
    private Date publicityDate;

    public Item(String streetAddress, String postalCode, String postOffice, float price, String description, String category, String title, Image image, User user, Date publicityDate){
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postOffice = postOffice;
        this.price = price;
        this.description = description;
        this.category = category;
        this.title = title;
        this.image = image;
        this.user = user;
        this.publicityDate = publicityDate;
    }
}
