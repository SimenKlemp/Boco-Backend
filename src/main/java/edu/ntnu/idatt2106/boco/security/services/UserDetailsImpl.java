package edu.ntnu.idatt2106.boco.security.services;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.ntnu.idatt2106.boco.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails
{
  private static final long serialVersionUID = 1L;

  private Long id;

  private String email;

  @JsonIgnore
  private String password;

  private GrantedAuthority authority;

  public UserDetailsImpl(Long id, String email, String password, GrantedAuthority authority)
  {
    this.id = id;
    this.email = email;
    this.password = password;
    this.authority = authority;
  }

  public static UserDetailsImpl build(User user)
  {
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

    return new UserDetailsImpl(
        user.getUserId(),
        user.getEmail(),
        user.getPassword(), 
        authority);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(authority);
    return authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername()
  {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
