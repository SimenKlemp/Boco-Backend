package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @NotBlank
    @Column(name = "name", length = 50)
    private String name;


    @Column(name = "isPerson")
    private Boolean isPerson;

    @NotBlank
    @Column(name = "streetAddress", length = 200)
    private String streetAddress;

    @NotBlank
    @Column(name = "postalCode", length = 50)
    private String postalCode;

    @NotBlank
    @Column(name = "postOffice", length = 50)
    private String postOffice;

    @NotBlank
    @Column(name = "email", length = 50)
    @Email
    private String email;

    @NotBlank
    @Column(name = "password", length = 256)
    private String password;

    @NotBlank
    @Column(name = "role", length = 30)
    private String role;

    @JoinColumn(name = "imageId")
    @OneToOne
    private Image image;

    public User(String name, Boolean isPerson, String streetAddress, String postalCode, String postOffice, String email, String password, String role, Image image)
    {
        this.name = name;
        this.isPerson = isPerson;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postOffice = postOffice;
        this.email = email;
        this.password = password;
        this.role = role;
        this.image = image;
    }
}
