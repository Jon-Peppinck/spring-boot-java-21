package com.example.blog.controller;

import com.example.blog.dto.SignInDTO;
import com.example.blog.dto.SignUpDTO;
import com.example.blog.entity.User;
import com.example.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<User> signUp(@RequestBody SignUpDTO signUpDTO) {
    User user = authService.signUp(signUpDTO);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO, HttpServletResponse response) {
    User user = authService.signIn(signInDTO, response);
    if (user == null) {
      return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/sign-out")
  public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
    authService.signOut(request, response);
    return new ResponseEntity<>("Successfully signed out", HttpStatus.OK);
  }

  @PutMapping("/make-user-admin")
  public ResponseEntity<Void> makeUserAdmin(HttpServletRequest request) {
    authService.makeUserAdmin(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
