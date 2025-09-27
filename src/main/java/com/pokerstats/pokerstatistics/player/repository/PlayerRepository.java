package com.pokerstats.pokerstatistics.player.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pokerstats.pokerstatistics.player.model.Player;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByPseudo(String pseudo);
}