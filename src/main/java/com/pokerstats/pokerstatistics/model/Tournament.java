package com.pokerstats.pokerstatistics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "tournament")
public class Tournament {
  @Id
  private String id;
  @Field("buy_in")
  private Integer buyIn;
  private String date;
  private String name;
  @Field("nb_players")
  private Integer nbPlayers;
  @Field("prize_pool")
  private Double prizePool;
  private EnumSpeed speed;
  private EnumType type;
}
