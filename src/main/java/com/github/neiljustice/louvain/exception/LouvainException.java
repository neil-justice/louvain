package com.github.neiljustice.louvain.exception;

public class LouvainException extends RuntimeException {
  public LouvainException(String s) {
    super(s);
  }

  public LouvainException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
