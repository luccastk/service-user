package br.com.pulsar.service_user.exception;

public class MissingTokenException extends RuntimeException {
  public MissingTokenException(String message) {
    super(message);
  }
}
