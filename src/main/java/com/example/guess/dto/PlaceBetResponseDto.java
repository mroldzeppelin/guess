package com.example.guess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceBetResponseDto {

  public enum Result {
    CREATED, FAILED
  }

  private Result result;
  private String errorMessage;
  private BetDto bet;

  public static PlaceBetResponseDto failed(BetDto dto, String errMsg) {
    return PlaceBetResponseDto.builder()
        .bet(dto)
        .errorMessage(errMsg)
        .result(Result.FAILED)
        .build();
  }

  public static PlaceBetResponseDto created(BetDto dto) {
    return PlaceBetResponseDto.builder()
        .bet(dto)
        .result(Result.CREATED)
        .build();
  }

  public String getUserId() {
    return bet != null ? bet.getUserId() : null;
  }
}
