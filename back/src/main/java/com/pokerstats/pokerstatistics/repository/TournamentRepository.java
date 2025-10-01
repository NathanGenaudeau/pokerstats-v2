package com.pokerstats.pokerstatistics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pokerstats.pokerstatistics.model.Tournament;

public interface TournamentRepository extends MongoRepository<Tournament, String> {
  
}
