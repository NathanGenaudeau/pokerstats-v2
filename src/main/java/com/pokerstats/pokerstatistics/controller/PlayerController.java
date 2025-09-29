package com.pokerstats.pokerstatistics.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pokerstats.pokerstatistics.model.Player;
import com.pokerstats.pokerstatistics.service.PlayerService;
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
    return playerService.getAll();
  }
  
  @GetMapping("/{pseudo}")
  public Player getByPseudo(@PathVariable String pseudo) {
    return playerService.getByPseudo(pseudo);
  }
  
  @PostMapping
  public Player addPlayer(@RequestBody Player player) {
    return playerService.save(player);
  }
}
