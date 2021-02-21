package com.example.guess.game.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class ThreadLocalNumberSupplier implements RandomNumberSupplier {
  @Override
  public int nextInt(int lowerBound, int upperBound) {
    return ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
  }
}
