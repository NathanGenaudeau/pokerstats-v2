package com.pokerstats.pokerstatistics;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/users")
  public List<User> getUsers() {
    return List.of(
      new User(UUID.randomUUID(), "Alice"),
      new User(UUID.randomUUID(), "Bob")
    );
  }
}
