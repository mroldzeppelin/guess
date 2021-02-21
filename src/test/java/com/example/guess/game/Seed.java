package com.example.guess.game;

import com.example.guess.dto.BetDto;
import lombok.val;

public class Seed {

  public static BetDto createBet() {
    val dto = new BetDto();
    dto.setUserId("Guffy");
    dto.setAmount(100.0);
    dto.setNumber(2);
    return dto;
  }
}
