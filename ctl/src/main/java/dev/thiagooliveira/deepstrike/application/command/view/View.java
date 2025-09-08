package dev.thiagooliveira.deepstrike.application.command.view;

public interface View<T> {
  String render(T model);
}
