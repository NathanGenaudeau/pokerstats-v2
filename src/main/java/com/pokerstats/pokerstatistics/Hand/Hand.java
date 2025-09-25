package com.pokerstats.pokerstatistics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
public class Hand {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;
  private String pseudo;
  private int idTournament;
  private List<Card> cards;
  private EnumPosition position;
  private int bigBlind;
  private int ante;
  private int stack;
  private List<Card> board;
  private boolean win;

  public Hand(UUID id, String pseudo, int idTournament, List<Card> cards, EnumPosition position, int bigBlind, int ante, int stack, List<Card> board, boolean win) {
    this.id = id;
    this.pseudo = pseudo;
    this.idTournament = idTournament;
    this.cards = cards;
    this.position = position;
    this.bigBlind = bigBlind;
    this.ante = ante;
    this.stack = stack;
    this.board = board;
    this.win = win;
  }

  public UUID getId() {
    return id;
  }
  public String getPseudo() {
    return pseudo;
  }
  public int getIdTournament() {
    return idTournament;
  }
  public List<Card> getCards() {
    return cards;
  }
  public EnumPosition getPosition() {
    return position;
  }
  public int getBigBlind() {
    return bigBlind;
  }
  public int getAnte() {
    return ante;
  }
  public int getStack() {
    return stack;
  }
  public List<Card> getBoard() {
    return board;
  }
  public boolean isWin() {
    return win;
  }
}