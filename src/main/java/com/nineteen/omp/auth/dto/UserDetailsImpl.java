package com.nineteen.omp.auth.dto;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.user.domain.User;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

  private final User user;

  public UserDetailsImpl(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String role = user.getRole().name();

    SimpleGrantedAuthority simpleGrantedAuthority =
        new SimpleGrantedAuthority(role);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(simpleGrantedAuthority);

    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public Long getUserId() {
    return user.getId();
  }

  public Role getRole() {
    return user.getRole();
  }

}
