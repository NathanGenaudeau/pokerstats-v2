package com.pokerstats.pokerstatistics.player.service;

import org.springframework.stereotype.Service;

import com.pokerstats.pokerstatistics.player.model.Player;
import com.pokerstats.pokerstatistics.player.repository.PlayerRepository;
import java.util.List;

@Service
public class PlayerService {
     private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player getByEmail(String pseudo) {
        return playerRepository.findByPseudo(pseudo);
    }
}
