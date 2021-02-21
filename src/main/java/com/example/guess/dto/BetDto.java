package com.example.guess.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class BetDto {
  @NotBlank(message = "User id cannot be empty")
  private String userId;
  @Positive(message = "Please specify the positive number")
  @Max(value = 10, message = "Number should be equal or less 10")
  private Integer number;
  @Positive(message = "Please specify the positive amount of money")
  @Max(value = 1000, message = "This bet is too large")
  private Double amount;
}
