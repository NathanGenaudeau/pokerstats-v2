package com.pokerstats.pokerstatistics.player.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokerstats.pokerstatistics.player.model.Player;
import com.pokerstats.pokerstatistics.player.service.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/players")
public class PlayerController {

  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping
  public List<Player> getPlayers() {
    return playerService.getAllPlayers();
  }

  @PostMapping
  public Player addPlayer(@RequestBody Player player) {
    return playerService.savePlayer(player);
  }
  
  @GetMapping("/{pseudo}")
  public Player getByPseudo(String pseudo) {
    return playerService.getByEmail(pseudo);
  }
}
