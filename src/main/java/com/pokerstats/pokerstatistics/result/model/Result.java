package com.pokerstats.pokerstatistics.result.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "result")
public class Result {

  @Id
  private String id;
  private Double bounty;
  private Double earning;
  private Integer idTournament;
  private Integer place;
  private String pseudo;
  private Integer timePlayed;
}
