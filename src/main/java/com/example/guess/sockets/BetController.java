package com.example.guess.sockets;

import com.example.guess.dto.BetDto;
import com.example.guess.dto.RoundEndDto;
import com.example.guess.events.RoundEndedEvent;
import com.example.guess.game.GuessGameService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class BetController {

  private final Double winCoefficient;
  private final GuessGameService gameService;
  private final SimpMessagingTemplate template;

  public BetController(
      @Value("${guess.number.win_coefficient}") Double winCoefficient,
      GuessGameService gameService,
      SimpMessagingTemplate template) {
    this.winCoefficient = winCoefficient;
    this.gameService = gameService;
    this.template = template;
  }


  @MessageMapping("/bet")
  public void placeBet(@Payload final BetDto bet) {
    val result = gameService.placeBet(bet);
    sendToUser(result.getUserId(), result);
  }

  @Async
  @EventListener(RoundEndedEvent.class)
  public void roundEndListener(RoundEndedEvent event) {
    val roundInfo = event.getRoundInfo();
    val number = event.getNumber();
    val winners = roundInfo.values()
        .stream()
        .filter(bet -> bet.getNumber().equals(number))
        .map(BetDto::getUserId)
        .collect(Collectors.toList());
    logRoundStatistic(winners, roundInfo);
    roundInfo.values()
        .stream()
        .map(bet -> RoundEndDto.create(bet, winners, number, winCoefficient))
        .forEach(dto -> sendToUser(dto.getUserId(), dto));
  }

  private void sendToUser(String userId, Object dto) {
    try {
      template.convertAndSendToUser(userId, "/messages", dto);
    } catch (MessagingException e) {
      log.debug("Cannot send info to user", e);
    }
  }

  private void logRoundStatistic(List<String> winners, Map<String, BetDto> roundInfo) {
    val str = winners.stream()
        .limit(5)
        .collect(Collectors.joining(", "));
    log.info("The first 5 winners (winners: {}, total: {}): {}", winners.size(), roundInfo.size(), str);
  }

}
