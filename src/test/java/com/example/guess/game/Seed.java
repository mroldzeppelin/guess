package com.example.guess.game;

import com.example.guess.dto.BetDto;

public class Seed {

  public static BetDto createBet() {
    return BetDto.builder()
        .userId("Guffy")
        .amount(100.0)
        .number(2)
        .build();
  }

}
