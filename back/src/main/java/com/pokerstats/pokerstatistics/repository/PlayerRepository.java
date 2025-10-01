package com.pokerstats.pokerstatistics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pokerstats.pokerstatistics.model.Player;

public interface PlayerRepository extends MongoRepository<Player, String> {
  Player findByPseudo(String pseudo);
}