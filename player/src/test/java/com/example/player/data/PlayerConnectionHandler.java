package com.example.player.data;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
class PlayerConnectionHandler extends StompSessionHandlerAdapter {

  private final String playerId;
  private final boolean needLog;

  public PlayerConnectionHandler(String playerId, boolean needLog) {
    this.playerId = playerId;
    this.needLog = needLog;
  }

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    val destination = String.format("/user/%s/messages", this.playerId);
    session.subscribe(destination, this);
    val scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(() -> session.send("/guess/bet", createBet()), 8, 8, TimeUnit.SECONDS);
  }

  @Override
  public void handleException(
      StompSession session,
      StompCommand command,
      StompHeaders headers,
      byte[] payload,
      Throwable exception) {
    log.error("Got an exception", exception);
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return JsonNode.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    if (needLog) {
      log.info("{} received : {}", playerId, payload.toString());
    }
  }

  private BetDto createBet() {
    val random = ThreadLocalRandom.current();
    return BetDto.builder()
        .userId(this.playerId)
        .amount(random.nextDouble(0, 1500))
        .number(random.nextInt(0, 11))
        .build();
  }

}
