package com.example.guess.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;


@Configuration
public class ValidatorConfig {

  @Bean
  public Validator getValidator() {
    val validatorFactory = Validation.buildDefaultValidatorFactory();
    return validatorFactory.usingContext().getValidator();
  }

}
