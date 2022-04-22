package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
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
    @Column(name = "address")
    private String address;

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

    public User(String name, boolean isPerson, String address,  String email, String password, String role, Image image)
    {
        this.name = name;
        this.isPerson = isPerson;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = role;
        this.image = image;
    }
}
