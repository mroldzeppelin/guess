package com.example.player.data;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class Player extends StompSessionHandlerAdapter {

  private static String URL = "ws://localhost:8080/guess";

  private final Integer roundTime;
  private final String playerId;
  private final boolean needLog;
  private final Consumer<Throwable> onError;
  private final Consumer<JsonNode> onResponse;

  public void init() {
    val client = new StandardWebSocketClient();
    val stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    stompClient.connect(URL, this);
  }

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    val destination = String.format("/user/%s/messages", this.playerId);
    session.subscribe(destination, this);
    val scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(() -> session.send("/guess/bet", createBet()), 1, roundTime, TimeUnit.SECONDS);
  }

  @Override
  public void handleException(
      StompSession session,
      StompCommand command,
      StompHeaders headers,
      byte[] payload,
      Throwable exception) {
    onError.accept(exception);
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return JsonNode.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    val json = (JsonNode) payload;
    onResponse.accept(json);
    if (needLog) {
      log.info("{} received : {}", playerId, json.toString());
    }
  }

  private BetDto createBet() {
    val random = ThreadLocalRandom.current();
    return BetDto.builder()
        .userId(this.playerId)
        .amount(random.nextDouble(0, 1000))
        .number(random.nextInt(0, 11))
        .build();
  }

}
