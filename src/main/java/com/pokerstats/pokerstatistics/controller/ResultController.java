package com.pokerstats.pokerstatistics.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pokerstats.pokerstatistics.model.Result;
import com.pokerstats.pokerstatistics.service.ResultService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/results")
public class ResultController {
  private final ResultService resultService;

  public ResultController(ResultService resultService) {
    this.resultService = resultService;
  }

  @GetMapping
  public List<Result> getResults() {
    return resultService.getAll();
  }
  
  @GetMapping("/{tournamentId}")
  public List<Result> getResultsByTournamentId(@PathVariable String tournamentId) {
    return resultService.getByTournamentId(tournamentId);
  }

  @GetMapping("/player/{pseudo}")
  public List<Result> getResultsByPseudo(@PathVariable String pseudo) {
    return resultService.getByPseudo(pseudo);
  }

  @PostMapping
  public Result addResult(@RequestBody Result result) {
    return resultService.save(result);
  }
}
