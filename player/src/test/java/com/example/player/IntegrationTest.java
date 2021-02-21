package com.example.player;

import com.example.player.data.Player;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.fail;

public class IntegrationTest {

  private final int roundTime = 10;
  private final int numberOfPlayers = 256;
  private final int numberOfRounds = 3;
  private final int numberOfResponses = numberOfPlayers * numberOfRounds * 2;

  @Test
  public void shouldReceiveResponse() {
    assertTimeout(ofSeconds(roundTime * numberOfRounds * 2), () -> {
      val latch = new CountDownLatch(numberOfResponses);
      Consumer<Throwable> onError = e -> fail();
      Consumer<JsonNode> onResponse = json -> latch.countDown();
      IntStream.rangeClosed(1, numberOfPlayers)
          .forEach(i -> {
            val playedId = String.format("Player-%d", i);
            val needLog = i == 10;
            val player = new Player(roundTime, playedId, needLog, onError, onResponse);
            player.init();
          });
      latch.await();
    });

  }

}
