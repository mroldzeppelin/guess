package com.example.guess.game.storage;

import com.example.guess.dto.BetDto;

import java.util.Map;

public interface GuessGameStorage {
  void placeBet(BetDto dto);
  Map<String, BetDto> nextRound();
}
