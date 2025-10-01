package com.pokerstats.pokerstatistics.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.pokerstats.pokerstatistics.model.Hand;

public interface HandRepository extends MongoRepository<Hand, String> {
  List<Hand> findByIdTournament(String idTournament);
  List<Hand> findByPseudo(String pseudo);
}
