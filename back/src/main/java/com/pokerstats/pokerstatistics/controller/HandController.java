package com.pokerstats.pokerstatistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pokerstats.pokerstatistics.model.Hand;
import com.pokerstats.pokerstatistics.service.HandService;

@RestController
@RequestMapping("/hands")
public class HandController {
  private final HandService handService;

  public HandController(HandService handService) {
    this.handService = handService;
  }

  @CrossOrigin
  @GetMapping
  public List<Hand> getHands() {
    return handService.getAll();
  }

  @GetMapping("/{tournamentId}")
  public List<Hand> getHandsByTournamentId(@PathVariable String tournamentId) {
    return handService.getByTournamentId(tournamentId);
  }

  @GetMapping("/player/{pseudo}")
  public List<Hand> getHandsByPseudo(@PathVariable String pseudo) {
    return handService.getByPseudo(pseudo);
  }

  @PostMapping
  public Hand addHand(@RequestBody Hand hand) {
    return handService.save(hand);
  }
}
