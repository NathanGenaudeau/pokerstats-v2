package com.pokerstats.pokerstatistics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "player")
public class Player {
  @Id
  private String id;
  private String pseudo;
}
