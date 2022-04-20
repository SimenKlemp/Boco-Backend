package edu.ntnu.idatt2106.boco.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", 
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "userId")
  private Long userId;

  @NotBlank
  @Size(max = 20)
  @Column(name = "name")
  private String name;

  @NotBlank
  @Size(max = 20)
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
  @Column(name = "imageId")
  private Long imageId;

  @NotBlank
  @Size(max = 120)
  @Column(name = "role")
  private String role;


  public User() { }

  public User(String name, boolean isPerson, String address,  String email, String password, String role ) {
    this.name = name;
    this.isPerson = isPerson;
    this.address = address;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isPerson() {
    return isPerson;
  }

  public void setPerson(boolean person) {
    isPerson = person;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
