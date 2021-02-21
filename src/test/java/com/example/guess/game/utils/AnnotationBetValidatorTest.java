package com.example.guess.game.utils;

import com.example.guess.config.ValidatorConfig;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static com.example.guess.game.Seed.createBet;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationBetValidatorTest {

  private ValidatorConfig config = new ValidatorConfig();
  private Validator validator = config.getValidator();
  private BetValidator betValidator = new AnnotationBetValidator(validator);

  @Test
  void shouldGetErrorMsgIfUserIdNull() {
    val dto = createBet();
    dto.setUserId(null);
    val errMsg = betValidator.getErrorMessage(dto);
    assertTrue(errMsg.contains("cannot be empty"));
  }

  @Test
  void shouldGetErrorMsgIfAmountLarge() {
    val dto = createBet();
    dto.setAmount(Double.MAX_VALUE);
    val errMsg = betValidator.getErrorMessage(dto);
    assertTrue(errMsg.contains("too large"));
  }

  @Test
  void shouldGetErrorMsgIfAmountSmall() {
    val dto = createBet();
    dto.setAmount(-1 * Double.MIN_VALUE);
    val errMsg = betValidator.getErrorMessage(dto);
    assertTrue(errMsg.contains("positive"));
  }

  @Test
  void shouldGetErrorMsgIfAmountZero() {
    val dto = createBet();
    dto.setAmount(0.0);
    val errMsg = betValidator.getErrorMessage(dto);
    assertTrue(errMsg.contains("positive"));
  }
}