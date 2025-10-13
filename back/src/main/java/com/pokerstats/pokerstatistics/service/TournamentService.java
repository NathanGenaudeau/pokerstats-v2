package com.pokerstats.pokerstatistics.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.pokerstats.pokerstatistics.model.Tournament;
import com.pokerstats.pokerstatistics.repository.TournamentRepository;

@Service
public class TournamentService {
  private final TournamentRepository tournamentRepository;

  public TournamentService(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
  }

  public List<Tournament> getAll() {
    return tournamentRepository.findAll();
  }

  public Tournament save(Tournament tournament) {
    try {
      return tournamentRepository.save(tournament);
    } catch (Exception e) {
      System.err.println("Error saving tournament: " + tournament.toString());
      return null;
    }
  }
}
