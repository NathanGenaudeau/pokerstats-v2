package com.pokerstats.pokerstatistics.tournament.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tournament")
public class Tournament {
  @Id
  private String id;
  private Integer buyIn;
  private String date;
  private String name;
  private Integer nbPlayers;
  private Double prizePool;
  //private EnumSpeed speed;
  //private EnumType type;

  public Tournament(String id, Integer buyIn, String date, String name, Integer nbPlayers, Double prizePool/*, EnumSpeed speed, EnumType type*/) {
    this.id = id;
    this.buyIn = buyIn;
    this.date = date;
    this.name = name;
    this.nbPlayers = nbPlayers;
    this.prizePool = prizePool;
    //this.speed = speed;
    //this.type = type;
  }
}
