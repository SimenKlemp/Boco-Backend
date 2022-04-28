package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "name")
    private String name;

    @Column(name = "isPerson")
    private boolean isPerson;

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

    @NotBlank
    @Size(max = 50)
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Size(max = 120)
    @Column(name = "role")
    private String role;

    @JoinColumn(name = "imageId")
    @OneToOne
    private Image image;


    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    public User(String name, boolean isPerson, String address,  String email, String password, String role, Image image){};

    public User(String name, boolean isPerson, String streetAddress, String postalCode, String postOffice, String email, String password, String role, Image image)

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
