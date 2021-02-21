package com.example.guess.game;

import com.example.guess.dto.PlaceBetResponseDto;
import com.example.guess.events.RoundEndedEvent;
import com.example.guess.game.storage.GuessGameStorage;
import com.example.guess.game.utils.BetValidator;
import com.example.guess.game.utils.RandomNumberSupplier;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

class InMemoryGuessGameServiceTest {

  private final int number = 6;
  private final int lowerBound = 0;
  private final int upperBound = 10;

  private GuessGameService gameService;
  private ApplicationEventPublisher publisher;
  private GuessGameStorage storage;
  private BetValidator validator;
  private RandomNumberSupplier supplier;

  @BeforeEach
  public void setup() {
    this.publisher = mock(ApplicationEventPublisher.class);
    this.supplier = (lower, upper) -> number;
    this.storage = mock(GuessGameStorage.class);
    this.validator = mock(BetValidator.class);
    this.gameService = new InMemoryGuessGameService(publisher, supplier, storage, validator, lowerBound, upperBound);
  }

  @Test
  void placeBetWithErrorShouldFailed() {
    val bet = Seed.createBet();
    val someError = "SomeError";
    when(validator.getErrorMessage(bet)).thenReturn(someError);
    val result = gameService.placeBet(bet);
    assertEquals(PlaceBetResponseDto.Result.FAILED, result.getResult());
    assertEquals(someError, result.getErrorMessage());
  }

  @Test
  void placeBetWithoutError() {
    val bet = Seed.createBet();
    when(validator.getErrorMessage(bet)).thenReturn(nullable(String.class));
    val result = gameService.placeBet(bet);
    assertEquals(PlaceBetResponseDto.Result.CREATED, result.getResult());
  }

  @Test
  void nextRoundShouldPublishEvents() {
    val captor = ArgumentCaptor.forClass(RoundEndedEvent.class);
    val bet = Seed.createBet();
    val round = Collections.singletonMap(bet.getUserId(), bet);
    when(storage.nextRound()).thenReturn(round);
    gameService.startNewRound();
    verify(publisher, times(1)).publishEvent(captor.capture());
    assertEquals(number, captor.getValue().getNumber());
  }
}