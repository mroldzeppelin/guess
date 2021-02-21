package com.example.player.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetDto {
  private String userId;
  private Integer number;
  private Double amount;
}
