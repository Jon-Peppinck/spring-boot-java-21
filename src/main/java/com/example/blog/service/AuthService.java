package com.example.blog.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

import com.example.blog.dto.SignInDTO;
import com.example.blog.dto.SignUpDTO;
import com.example.blog.security.RoleEnum;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  private final String COOKIE_NAME = "SESSIONID";

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public User signUp(SignUpDTO request) {
    User user = new User();
    user.setEmail(request.getEmail());
    user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setRoleId(RoleEnum.SIGNED_OUT.getRoleId());
    user.setRoleName(RoleEnum.SIGNED_OUT.getRoleName());
    return userRepository.save(user);
  }

  public User signIn(SignInDTO request, HttpServletResponse response) {
    Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(request.getPassword(), user.getEncodedPassword())) {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        user.setSessionId(sessionId);
        user.setRoleId(RoleEnum.USER.getRoleId());
        user.setRoleName(RoleEnum.USER.getRoleName());
        return userRepository.save(user);
      }
    }
    return null;
  }

  public void signOut(HttpServletRequest request, HttpServletResponse response) {
    Cookie cookie = new Cookie(COOKIE_NAME, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    Optional<User> userOptional = getUserFromSession(request);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setSessionId(null);
      user.setRoleId(RoleEnum.SIGNED_OUT.getRoleId());
      user.setRoleName(RoleEnum.SIGNED_OUT.getRoleName());
      userRepository.save(user);
    }
  }

  public Optional<User> getUserFromSession(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (COOKIE_NAME.equals(cookie.getName())) {
          String sessionId = cookie.getValue();
          return userRepository.findBySessionId(sessionId);
        }
      }
    }
    return Optional.empty();
  }

  public void makeUserAdmin(HttpServletRequest request) {
    Optional<User> userOptional = getUserFromSession(request);

    if (!userOptional.isPresent()) {
      throw new AccessDeniedException("unauthorized");
    }

    User user = userOptional.get();
    user.setRoleId(RoleEnum.ADMIN.getRoleId());
    user.setRoleName(RoleEnum.ADMIN.getRoleName());
    userRepository.save(user);
  }
}
