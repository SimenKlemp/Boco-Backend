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
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @NotBlank
    @Column(name = "name")
    private String name;


    @Column(name = "isPerson")
    private Boolean isPerson;

    @NotBlank
    @Column(name = "streetAddress")
    private String streetAddress;

    @NotBlank
    @Column(name = "postalCode")
    private String postalCode;

    @NotBlank
    @Column(name = "postOffice")
    private String postOffice;

    @NotBlank
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "role")
    private String role;

    @JoinColumn(name = "imageId")
    @OneToOne
    private Image image;


    @Column(name = "reset_password_token")
    private String resetPasswordToken;


    public User(String name, Boolean isPerson, String streetAddress, String postalCode, String postOffice, String email, String password, String role, Image image) {
        this.name = name;
        this.isPerson = isPerson;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postOffice = postOffice;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
