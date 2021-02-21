package com.example.guess.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LocalGameSchedulerTest {

  private GuessGameScheduler scheduler;
  private GuessGameService service;

  @BeforeEach
  public void setup() {
    this.service = mock(GuessGameService.class);
    this.scheduler = new LocalGameScheduler(service);
  }

  @Test
  void scheduleRound() {
    scheduler.scheduleRound();
    verify(service, times(1)).startNewRound();
  }
}