package com.pokerstats.pokerstatistics.service;

import org.springframework.stereotype.Service;

import com.pokerstats.pokerstatistics.model.Player;
import com.pokerstats.pokerstatistics.repository.PlayerRepository;

import java.util.List;

@Service
public class PlayerService {
  private final PlayerRepository playerRepository;

  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  public List<Player> getAll() {
    return playerRepository.findAll();
  }
  
  public Player getByPseudo(String pseudo) {
    return playerRepository.findByPseudo(pseudo);
  }

  public Player save(Player player) {
    return playerRepository.save(player);
  }
}
