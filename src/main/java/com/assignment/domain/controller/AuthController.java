package com.assignment.domain.controller;

import com.assignment.domain.security.JwtHelper;
import com.assignment.domain.security.LoginResult;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
public class AuthController {

  private final JwtHelper jwtHelper;
  private final PasswordEncoder passwordEncoder;
  private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

  @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public LoginResult login(@RequestParam String username, @RequestParam String password) {

    UserDetails userDetails;
    try {
      userDetails = inMemoryUserDetailsManager.loadUserByUsername(username);
    } catch (UsernameNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
    }

    if (passwordEncoder.matches(password, userDetails.getPassword())) {
      Map<String, String> claims = new HashMap<>();
      claims.put("username", username);

      String authorities = userDetails.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.joining(","));
      claims.put("authorities", authorities);
      claims.put("userId", String.valueOf(1));

      String jwt = jwtHelper.createJwtForClaims(username, claims);
      return new LoginResult(jwt);
    }

    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
  }
}