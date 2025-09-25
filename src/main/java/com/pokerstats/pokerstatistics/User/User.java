package com.pokerstats.pokerstatistics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
public class User {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;
  private String pseudo;

  public User(UUID id, String pseudo) {
    this.id = id;
    this.pseudo = pseudo;
  }

  public UUID getId() {
    return id;
  }

  public String getPseudo() {
    return pseudo;
  }
}
