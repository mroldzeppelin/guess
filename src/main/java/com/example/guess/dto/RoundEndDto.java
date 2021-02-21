package com.example.guess.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundEndDto {
  public enum Result {
    WIN, LOSING
  }

  private Double winCoefficient;
  private Integer number;
  private List<String> winners;
  private BetDto bet;

  public static RoundEndDto create(
      BetDto bet, List<String> winners,
      Integer number, Double winCoefficient) {
    return RoundEndDto.builder()
        .bet(bet)
        .winners(winners)
        .winCoefficient(winCoefficient)
        .number(number)
        .build();
  }

  public String getUserId() {
    return bet != null
        ? bet.getUserId()
        : null;
  }

  public Result getResult() {
    val userId = getUserId();
    return winners != null && winners.contains(userId)
        ? Result.WIN
        : Result.LOSING;
  }

  public Double getAmount() {
    return bet != null && getResult() == Result.WIN
        ? bet.getAmount() * winCoefficient
        : 0;
  }
}
