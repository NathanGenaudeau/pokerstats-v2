package com.pokerstats.pokerstatistics.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.pokerstats.pokerstatistics.model.Card;
import com.pokerstats.pokerstatistics.model.EnumPosition;
import com.pokerstats.pokerstatistics.model.EnumSpeed;
import com.pokerstats.pokerstatistics.model.EnumType;
import com.pokerstats.pokerstatistics.model.Hand;
import com.pokerstats.pokerstatistics.model.Player;
import com.pokerstats.pokerstatistics.model.Result;
import com.pokerstats.pokerstatistics.model.Tournament;

@Service
public class UploadService {
  private final PlayerService PlayerService;
  private final TournamentService TournamentService;
  private final ResultService ResultService;
  private final HandService HandService;

  public UploadService(PlayerService playerService, TournamentService tournamentService, ResultService resultService, HandService handService) {
    this.PlayerService = playerService;
    this.TournamentService = tournamentService;
    this.ResultService = resultService;
    this.HandService = handService;
  }

  public void uploadFile(MultipartFile file) {
    try {
      List<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines().toList();

      if (file.getOriginalFilename() == null || file.getOriginalFilename().contains("Expresso")) return; // Ignore Expresso files
      if (!lines.isEmpty() && lines.get(0).contains("€/")) return; // Ignore non-Euro files

      if (file.getOriginalFilename().contains("summary")) {
        Player player = getPlayerInfo(lines);
        Tournament tournament = getTournamentInfo(lines);
        Result result = getResultInfo(lines, player, tournament);

        PlayerService.save(player);
        TournamentService.save(tournament);
        ResultService.save(result);
      } else {
        List<List<String>> hands = new ArrayList<>();
        List<String> currentHand = new ArrayList<>();
        
        lines.stream()
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .forEach(line -> {
            if (line.startsWith("Winamax Poker - Tournament") && !currentHand.isEmpty()) {
              hands.add(new ArrayList<>(currentHand));
              currentHand.clear();
            }
            currentHand.add(line);
          });

        System.out.println("Found " + hands.size() + " hands in file");

        hands.forEach(handLines -> {
          List<String> normalized = normalizeSeatsFromLines(handLines);
          Hand hand = getHandInfo(normalized);
          HandService.save(hand);
        });
        System.out.println("Imported " + hands.size() + " hands from file");
      }

    } catch (Exception e) {
      throw new RuntimeException("File processing failed", e);
    }
  }

  private Player getPlayerInfo(List<String> lines) {
    Player player = new Player();
    String pseudo = lines.stream()
      .filter(line -> line.contains("Player"))
      .findFirst()
      .map(line -> line.split(": ")[1].trim())
      .orElse("Unknown");

    player.setPseudo(pseudo);
    return player;
  } 

  private Tournament getTournamentInfo(List<String> lines) {
    Tournament tournament = new Tournament();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    lines.stream()
      .map(String::trim)
      .forEach(line -> {
        if (line.contains("Tournament summary")) {
          String part = line.split(": ")[1];
          String[] parts = part.split("\\(");
          tournament.setName(parts[0].trim());
          tournament.setId(parts[1].replace(")", "").split(" -")[0].trim());
        } else if (line.contains("Buy-In")) {
          String part = line.split(": ")[1];
          double totalBuyIn = List.of(part.split("\\+")).stream()
            .map(String::trim)
            .mapToDouble(p -> Double.parseDouble(p.split("€")[0].trim().replace(",", ".")))
            .sum();
          tournament.setBuyIn(Math.round(totalBuyIn * 100.0) / 100.0);
        } else if (line.contains("Registered players")) {
            tournament.setNbPlayers(Integer.parseInt(line.split(": ")[1].trim()));
        } else if (line.startsWith("Type")) {
          String rawType = line.split(": ")[1].trim().toUpperCase();
          try {
            tournament.setType(EnumType.valueOf(rawType.replace(" ", "")));
          } catch (IllegalArgumentException e) {
            tournament.setType(EnumType.NORMAL);
          }
        } else if (line.startsWith("Speed")) {
          String rawSpeed = line.split(": ")[1].trim().toUpperCase();
          try {
            tournament.setSpeed(EnumSpeed.valueOf(rawSpeed.replace(" ", "")));
          } catch (IllegalArgumentException e) {
            tournament.setSpeed(EnumSpeed.NORMAL);
          }
        } else if (line.contains("Prizepool")) {
          String part = line.split(": ")[1].replace("€", "").trim().replace(",", ".");
          tournament.setPrizePool(Double.parseDouble(part));
        } else if (line.contains("Tournament started")) {
          String part = line.split("Tournament started")[1].replace("UTC", "").trim();
          try {
            tournament.setDate(LocalDateTime.parse(part, dateFormatter));
          } catch (Exception e) {
            System.err.println("Error parsing date: " + part);
          }
        }
    });

    return tournament;
  }

  private Result getResultInfo(List<String> lines, Player player, Tournament tournament) {
    Result result = Result.builder()
      .idTournament(tournament.getId())
      .pseudo(player.getPseudo())
      .bounty(0.0)
      .earning(0.0)
      .build();

    Pattern bountyPattern = Pattern.compile("Bounty\\s+(\\d+[\\.,]?\\d*)€");
    Pattern earningPattern = Pattern.compile("You won\\s+(?!Bounty)(\\d+[\\.,]?\\d*)€");
    lines.stream()
      .map(String::trim)
      .forEach(line -> {
        if (line.contains("You finished in")) {
          String part = line.split("You finished in")[1];
          String placeStr = part.split("place")[0]
            .replaceAll("(th|st|nd|rd)", "")
            .trim();
          try {
            result.setPlace(Integer.parseInt(placeStr));
          } catch (NumberFormatException e) {
            System.err.println("Erreur parsing place: " + placeStr);
          }
        }
        if (line.contains("You won")) {
          Matcher bountyMatcher = bountyPattern.matcher(line);
          if (bountyMatcher.find()) {
            result.setBounty(Double.parseDouble(bountyMatcher.group(1).replace(",", ".")));
          }
          Matcher earningMatcher = earningPattern.matcher(line);
          if (earningMatcher.find()) {
            result.setEarning(Double.parseDouble(earningMatcher.group(1).replace(",", ".")));
          }
        }
        if (line.contains("You played")) {
          String[] parts = line.split("You played")[1].trim().split("\\s+");
          int totalSeconds = 0;
          for (String part : parts) {
            if (part.endsWith("h")) {
              totalSeconds += Integer.parseInt(part.replace("h", "")) * 3600;
            } else if (part.endsWith("min")) {
              totalSeconds += Integer.parseInt(part.replace("min", "")) * 60;
            } else if (part.endsWith("s")) {
              totalSeconds += Integer.parseInt(part.replace("s", ""));
            }
          }
          result.setTimePlayed(totalSeconds);
        }
      });

    return result;
  }
  
  private Hand getHandInfo(List<String> lines) {
    Hand hand = new Hand();
    
    hand.setId(lines.stream().filter(l -> l.contains("HandId")).findFirst().map(l -> l.split("HandId: ")[1].split(" -")[0].replace("#", "")).orElse(null));
    hand.setPseudo(lines.stream().filter(l -> l.contains("Dealt to")).findFirst().map(l -> l.split("Dealt to ")[1].split(" ")[0]).orElse(null));
    hand.setIdTournament(lines.stream().filter(l -> l.contains("Table:")).findFirst().map(l -> l.split("\\)")[0].split("\\(")[1]).orElse(null));
    String cardsStr = lines.stream().filter(l -> l.contains("Dealt to")).findFirst().map(l -> l.split("\\[")[1].split("]")[0]).orElse(null);
    hand.setCards(cardsStr != null ? orderCards(cardsStr) : null);
    int index = IntStream.range(0, lines.size()).filter(i -> lines.get(i).contains("*** ANTE/BLINDS ***")).findFirst().orElse(0);
    int nbPlayers = (int) lines.stream().limit(index).filter(l -> l.contains("Seat")).count() - 1;
    int button = Integer.parseInt(lines.stream().filter(l -> l.contains("is the button")).findFirst().map(l -> l.split("Seat ")[1].split("is")[0].replace("#", "").trim()).orElse(""));
    int seat = Integer.parseInt(lines.stream().filter(l -> l.contains(hand.getPseudo())).findFirst().map(l -> l.split("Seat ")[1].split(":")[0].trim()).orElse(""));
    hand.setPosition(calculatePosition(seat, nbPlayers, button));
    hand.setBigBlind(lines.stream().filter(l -> l.contains("HandId")).findFirst().map(l -> l.split("HandId: ")[1].split("\\(")[1].split("\\)")[0].split("\\/")[2]).map(Integer::parseInt).orElse(null));
    hand.setAnte(lines.stream().filter(l -> l.contains("HandId")).findFirst().map(l -> l.split("HandId: ")[1].split("\\(")[1].split("\\)")[0].split("\\/")[0]).map(Integer::parseInt).orElse(null));
    hand.setStack(lines.stream().filter(l -> l.contains(hand.getPseudo())).findFirst().map(l -> l.split(hand.getPseudo())[1].split("\\(")[1].split(",")[0]).map(s -> s.replaceAll("[^\\d]", "")).map(Integer::parseInt).orElse(null));
    hand.setBoard(getBoardCards(lines, hand.getId()));
    hand.setWin(lines.stream().filter(l -> l.contains("won")).findFirst().map(l -> l.contains(hand.getPseudo())).orElse(false));

    return hand;
  }

  private List<Card> getBoardCards(List<String> lines, String handId) {
    String boardStr = lines.stream().filter(l -> l.contains("Board:")).findFirst().map(l -> l.split("Board: ")[1].replace("[", "").replace("]", "").trim()).orElse("");
    if (boardStr.isEmpty()) return Collections.emptyList();
    String[] cardStrs = boardStr.split(" ");
    List<Card> cards = new ArrayList<>();
    for (String cardStr : cardStrs) {
      cards.add(Card.fromString(cardStr));
    }
    return cards;
  }
  
  private List<Card> orderCards(String cardsStr) {
    Card card1 = Card.fromString(cardsStr.split(" ")[0]);
    Card card2 = Card.fromString(cardsStr.split(" ")[1]);
    return card1.getRank().ordinal() > card2.getRank().ordinal() ? List.of(card1, card2) : List.of(card2, card1);
  }

  private EnumPosition calculatePosition(int seat, int nbPlayers, int button) {
    if (nbPlayers < 2 || nbPlayers > 9) return null;
    if (nbPlayers == 2) return seat == button ? EnumPosition.BIG_BLIND : EnumPosition.SMALL_BLIND;
    return EnumPosition.values()[( ( (button - seat + nbPlayers) % nbPlayers) + 2) % nbPlayers];
  }

  private List<String> normalizeSeatsFromLines(List<String> lines) {
    Pattern seatPattern = Pattern.compile("^Seat (\\d+):");
    Pattern buttonPattern = Pattern.compile("Seat #(\\d+) is the button");

    // Collect unique sorted seat numbers
    var seats = lines.stream()
      .map(l -> {
        var m = seatPattern.matcher(l);
        return m.find() ? Integer.parseInt(m.group(1)) : null;
      })
      .filter(Objects::nonNull)
      .distinct()
      .sorted()
      .toList();

    // Map old seat → new seat
    var seatMap = IntStream.range(0, seats.size())
      .boxed()
      .collect(Collectors.toMap(seats::get, i -> i + 1));

    // Find button or fallback to first seat
    var button = lines.stream()
      .map(l -> {
        var m = buttonPattern.matcher(l);
        return m.find() ? Integer.parseInt(m.group(1)) : null;
      })
      .filter(Objects::nonNull)
      .findFirst()
      .orElseGet(() -> seats.isEmpty() ? 1 : seats.getFirst());

    // Adjust button if needed
    var seatSet = new HashSet<>(seats);
    while (!seatSet.contains(button))
      button = (button - 1 < Collections.min(seatSet)) ? Collections.max(seatSet) : button - 1;

    var fixedButton = button;

    // Rewrite lines
    return lines.stream().map(l -> {
      var sm = seatPattern.matcher(l);
      var bm = buttonPattern.matcher(l);
      return sm.find()
        ? sm.replaceAll("Seat " + seatMap.getOrDefault(Integer.parseInt(sm.group(1)), 1) + ":")
        : bm.find()
          ? bm.replaceAll("Seat #" + seatMap.getOrDefault(fixedButton, fixedButton) + " is the button")
          : l;
    }).toList();
  }
}
