package com.pokerstats.pokerstatistics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
  private EnumSuit suit;
  private EnumRank rank;
}

enum EnumSuit {
  CLUBS, DIAMONDS, HEARTS, SPADES
}

enum EnumRank {
  TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}