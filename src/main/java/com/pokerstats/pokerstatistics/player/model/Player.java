package com.pokerstats.pokerstatistics.player.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "player")
public class Player {
  @Id
  private String id;
  private String pseudo;

  public Player(String id, String pseudo) {
    this.id = id;
    this.pseudo = pseudo;
  }

  public String getId() {
    return id;
  }

  public String getPseudo() {
    return pseudo;
  }
}
