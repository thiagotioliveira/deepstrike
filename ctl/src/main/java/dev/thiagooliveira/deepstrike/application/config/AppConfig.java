package dev.thiagooliveira.deepstrike.application.config;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class AppConfig {

  @Bean
  PromptProvider promptProvider() {
    return () ->
        new AttributedString(
            "deepstrike:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
  }
}
