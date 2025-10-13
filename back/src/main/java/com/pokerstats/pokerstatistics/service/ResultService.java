package com.pokerstats.pokerstatistics.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.pokerstats.pokerstatistics.model.Result;
import com.pokerstats.pokerstatistics.repository.ResultRepository;

@Service
public class ResultService {
  private final ResultRepository resultRepository;

  public ResultService(ResultRepository resultRepository) {
    this.resultRepository = resultRepository;
  }

  public List<Result> getAll() {
    return resultRepository.findAll();
  }

  public List<Result> getByTournamentId(String tournamentId) {
    return resultRepository.findByIdTournament(tournamentId);
  }

  public List<Result> getByPseudo(String pseudo) {
    return resultRepository.findByPseudo(pseudo);
  }

  public Result save(Result result) {
    try {
      return resultRepository.save(result);
    } catch (Exception e) {
      System.err.println("Error saving result: " + result.toString());
      return null;
    }
  }
}
