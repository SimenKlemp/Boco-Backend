package edu.ntnu.idatt2106.boco.payload.request;

import javax.validation.constraints.*;

public class RegisterUserRequest
{
  @NotBlank
  @Size(max = 20)
  private String name;

  private boolean isPerson;

  @Size(max = 20)
  private String address;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  public RegisterUserRequest(String name, boolean isPerson, String address, String email, String password)
  {
    this.name = name;
    this.isPerson = isPerson;
    this.address = address;
    this.email = email;
    this.password = password;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean getIsPerson()
  {
    return isPerson;
  }

  public void setIsPerson(boolean isPerson)
  {
    this.isPerson = isPerson;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }
}
