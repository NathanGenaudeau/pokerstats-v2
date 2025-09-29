package com.pokerstats.pokerstatistics.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.pokerstats.pokerstatistics.model.Hand;
import com.pokerstats.pokerstatistics.repository.HandRepository;

@Service
public class HandService {
  private final HandRepository handRepository;

  public HandService(HandRepository handRepository) {
    this.handRepository = handRepository;
  }

  public List<Hand> getAll() {
    return handRepository.findAll();
  }

  public List<Hand> getByTournamentId(String tournamentId) {
    return handRepository.findByIdTournament(tournamentId);
  }

  public List<Hand> getByPseudo(String pseudo) {
    return handRepository.findByPseudo(pseudo);
  }

  public Hand save(Hand hand) {
    return handRepository.save(hand);
  }
}