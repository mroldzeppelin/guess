package com.example.guess.game.utils;

import com.example.guess.dto.BetDto;

public interface BetValidator {
  String getErrorMessage(BetDto dto);
}
