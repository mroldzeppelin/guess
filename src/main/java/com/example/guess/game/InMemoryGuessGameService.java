package com.example.guess.game;

import com.example.guess.dto.BetDto;
import com.example.guess.dto.PlaceBetResponseDto;
import com.example.guess.events.RoundEndedEvent;
import com.example.guess.game.storage.GuessGameStorage;
import com.example.guess.game.utils.BetValidator;
import com.example.guess.game.utils.RandomNumberSupplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InMemoryGuessGameService implements GuessGameService {

  private final int lowerBound;
  private final int upperBound;
  private final GuessGameStorage storage;
  private final BetValidator betValidator;
  private final RandomNumberSupplier randomNumberSupplier;
  private final ApplicationEventPublisher publisher;

  public InMemoryGuessGameService(
      ApplicationEventPublisher publisher,
      RandomNumberSupplier randomNumberSupplier,
      GuessGameStorage storage,
      BetValidator betValidator,
      @Value("${guess.number.lower_bound}") int lowerBound,
      @Value("${guess.number.upper_bound}") int upperBound) {
    this.publisher = publisher;
    this.randomNumberSupplier = randomNumberSupplier;
    this.storage = storage;
    this.betValidator = betValidator;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  @Override
  public PlaceBetResponseDto placeBet(@NonNull BetDto dto) {
    val errMsg = betValidator.getErrorMessage(dto);
    if (errMsg != null) {
      return PlaceBetResponseDto.failed(dto, errMsg);
    }
    storage.placeBet(dto);
    return PlaceBetResponseDto.created(dto);
  }

  @Override
  public void startNewRound() {
    val roundInfo = storage.nextRound();
    if (roundInfo == null || roundInfo.isEmpty()) {
      log.info("The round has finished, no players found");
      return;
    }

    val number = generateNumber();
    val event = new RoundEndedEvent(number, roundInfo);
    publisher.publishEvent(event);
  }

  private Integer generateNumber() {
    val random = randomNumberSupplier.nextInt(lowerBound, upperBound);
    log.info("The round has finished, the number was {}", random);
    return random;
  }

}
