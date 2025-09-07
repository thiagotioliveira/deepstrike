package dev.thiagooliveira.deepstrike;

import org.springframework.boot.SpringApplication;

public class TestDeepstrikeApplication {

  public static void main(String[] args) {
    SpringApplication.from(DeepstrikeApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
