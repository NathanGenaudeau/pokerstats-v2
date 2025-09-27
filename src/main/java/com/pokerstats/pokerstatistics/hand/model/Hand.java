package com.pokerstats.pokerstatistics.hand.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hand")
public class Hand {
  @Id
  private String id;
  private String pseudo;
  private Integer idTournament;
  //private List<Card> cards;
  //private EnumPosition position;
  private Integer bigBlind;
  private Integer ante;
  private Integer stack;
  //private List<Card> board;
  private Boolean win;

  public Hand(String id, String pseudo, Integer idTournament, /*List<Card> cards, EnumPosition position,*/ Integer bigBlind, Integer ante, Integer stack, /*List<Card> board,*/ Boolean win) {
    this.id = id;
    this.pseudo = pseudo;
    this.idTournament = idTournament;
    //this.cards = cards;
    //this.position = position;
    this.bigBlind = bigBlind;
    this.ante = ante;
    this.stack = stack;
    //this.board = board;
    this.win = win;
  }

  public String getId() {
    return id;
  }
  public String getPseudo() {
    return pseudo;
  }
  public Integer getIdTournament() {
    return idTournament;
  }
  /*public List<Card> getCards() {
    return cards;
  }
  public EnumPosition getPosition() {
    return position;
  }*/
  public Integer getBigBlind() {
    return bigBlind;
  }
  public Integer getAnte() {
    return ante;
  }
  public Integer getStack() {
    return stack;
  }
  /*public List<Card> getBoard() {
    return board;
  }*/
  public Boolean isWin() {
    return win;
  }
}