package com.example.guess.game;

import com.example.guess.dto.BetDto;
import com.example.guess.dto.PlaceBetResponseDto;

public interface GuessGameService {
  PlaceBetResponseDto placeBet(BetDto dto);
  void startNewRound();
}
