package com.example.guess.events;

import com.example.guess.dto.BetDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class RoundEndedEvent {
  private final Integer number;
  private final Map<String, BetDto> roundInfo;
}
