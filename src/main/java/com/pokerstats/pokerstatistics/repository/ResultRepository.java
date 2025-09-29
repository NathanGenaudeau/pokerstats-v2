package com.pokerstats.pokerstatistics.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pokerstats.pokerstatistics.model.Result;

public interface ResultRepository extends MongoRepository<Result, String> {
    List<Result> findByIdTournament(String idTournament);
    List<Result> findByPseudo(String pseudo);
}