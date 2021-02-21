package com.example.guess;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class GuessGameApplication {

  public static void main(String[] args) {
    val app = new SpringApplication(GuessGameApplication.class);
    app.addListeners(new ApplicationPidFileWriter());
    app.run(args);
  }

}
