package me.roundaround.testmod.testgenerated;

public enum Variant {
  BASE(""), EXPERIMENTAL("experimental");

  private final String id;

  Variant(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return this.id;
  }
}
