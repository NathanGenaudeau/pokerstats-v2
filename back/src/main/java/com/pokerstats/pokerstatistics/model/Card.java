package com.pokerstats.pokerstatistics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
  private EnumSuit suit;
  private EnumRank rank;

  public static Card fromString(String s) {
    String rankSymbol = s.substring(0, 1);
    char suitSymbol = s.charAt(1);

    EnumRank rank = switch (rankSymbol) {
      case "2" -> EnumRank.TWO;
      case "3" -> EnumRank.THREE;
      case "4" -> EnumRank.FOUR;
      case "5" -> EnumRank.FIVE;
      case "6" -> EnumRank.SIX;
      case "7" -> EnumRank.SEVEN;
      case "8" -> EnumRank.EIGHT;
      case "9" -> EnumRank.NINE;
      case "T" -> EnumRank.TEN;
      case "J" -> EnumRank.JACK;
      case "Q" -> EnumRank.QUEEN;
      case "K" -> EnumRank.KING;
      case "A" -> EnumRank.ACE;
      default -> throw new IllegalArgumentException("Invalid rank: " + rankSymbol);
    };

    EnumSuit suit = switch (suitSymbol) {
      case 'c' -> EnumSuit.CLUBS;
      case 'd' -> EnumSuit.DIAMONDS;
      case 'h' -> EnumSuit.HEARTS;
      case 's' -> EnumSuit.SPADES;
      default -> throw new IllegalArgumentException("Invalid suit: " + suitSymbol);
    };

    return new Card(suit, rank);
  }
}