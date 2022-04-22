package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
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
    @Column(name = "address")
    private String address;

    @Column(name = "price")
    private float price;

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

    @JoinColumn(name = "imageId")
    @OneToOne
    private Image image;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    public Item(String address, float price, String description, String category, String title, Image image, User user)
    {
        this.address = address;
        this.price = price;
        this.description = description;
        this.category = category;
        this.title = title;
        this.image = image;
        this.user = user;
    }
}
