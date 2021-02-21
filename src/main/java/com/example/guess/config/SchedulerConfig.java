package com.example.guess.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
  public static final String ROUND_TIME_MILLIS = "#{T(java.time.Duration).parse('PT' + '${guess.number.round_time}').toMillis()}";
}
