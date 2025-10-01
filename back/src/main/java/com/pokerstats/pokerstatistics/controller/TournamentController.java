package com.pokerstats.pokerstatistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pokerstats.pokerstatistics.model.Tournament;
import com.pokerstats.pokerstatistics.service.TournamentService;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {
  private final TournamentService tournamentService;

  public TournamentController(TournamentService tournamentService) {
    this.tournamentService = tournamentService;
  }

  @CrossOrigin
  @GetMapping
  public List<Tournament> getTournaments() {
    return tournamentService.getAll();
  }

  @PostMapping
  public Tournament addTournament(@RequestBody Tournament tournament) {
    return tournamentService.save(tournament);
  }
}
