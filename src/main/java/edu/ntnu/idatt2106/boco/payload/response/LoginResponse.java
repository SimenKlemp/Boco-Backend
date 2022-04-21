package edu.ntnu.idatt2106.boco.payload.response;

public class LoginResponse
{
  private String token;
  private String type = "Bearer";
  private Long id;
  private String email;
  private String role;

  public LoginResponse(String accessToken, Long id, String email, String role) {
    this.token = accessToken;
    this.id = id;
    this.email = email;
    this.role = role;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken(String token)
  {
    this.token = token;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public void setRole(String role)
  {
    this.role = role;
  }
}
