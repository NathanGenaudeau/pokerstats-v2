package com.pokerstats.pokerstatistics.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pokerstats.pokerstatistics.model.EnumSpeed;
import com.pokerstats.pokerstatistics.model.EnumType;
import com.pokerstats.pokerstatistics.model.Player;
import com.pokerstats.pokerstatistics.model.Result;
import com.pokerstats.pokerstatistics.model.Tournament;

@Service
public class UploadService {
  private final PlayerService PlayerService;
  private final TournamentService TournamentService;
  private final ResultService ResultService;

  public UploadService(PlayerService playerService, TournamentService tournamentService, ResultService resultService) {
    this.PlayerService = playerService;
    this.TournamentService = tournamentService;
    this.ResultService = resultService;
  }

  public void uploadFile(MultipartFile file) {
    try {
      if (file.getName().contains("Expresso")) return;

      List<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines().toList();

      Player player = getPlayerInfo(lines);
      Tournament tournament = getTournamentInfo(lines);
      Result result = getResultInfo(lines, player, tournament);

      PlayerService.save(player);
      TournamentService.save(tournament);
      ResultService.save(result);

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
}
