package com.pokerstats.pokerstatistics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "result")
public class Result {

  @Id
  private String id;
  private Double bounty;
  private Double earning;
  @Field("id_tournament")
  private String idTournament;
  private Integer place;
  private String pseudo;
  @Field("time_played")
  private Integer timePlayed;
}
