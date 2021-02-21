package com.example.guess.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.example.guess.config.SchedulerConfig.ROUND_TIME_MILLIS;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalGameScheduler implements GuessGameScheduler {

  private final GuessGameService gameService;

  @Override
  @Scheduled(
      initialDelayString = ROUND_TIME_MILLIS,
      fixedRateString = ROUND_TIME_MILLIS
  )
  public void scheduleRound() {
    gameService.startNewRound();
  }

}
