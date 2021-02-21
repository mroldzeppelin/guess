package com.example.guess.game.utils;

import com.example.guess.dto.BetDto;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.validation.Validator;


@Component
@RequiredArgsConstructor
public class AnnotationBetValidator implements BetValidator {

  private final Validator validator;

  @Override
  public String getErrorMessage(BetDto dto) {
    val result = validator.validate(dto);
    if (result == null || result.isEmpty()) {
      return null;
    }
    return result
        .stream()
        .findAny()
        .orElse(null)
        .getMessage();
  }

}
