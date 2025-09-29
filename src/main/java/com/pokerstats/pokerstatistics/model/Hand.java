package com.pokerstats.pokerstatistics.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "hand")
public class Hand {
  @Id
  private String id;
  private String pseudo;
  @Field("id_tournament")
  private String idTournament;
  private List<Card> cards;
  private EnumPosition position;
  @Field("big_blind")
  private Integer bigBlind;
  private Integer ante;
  private Integer stack;
  private List<Card> board;
  private Boolean win;
}