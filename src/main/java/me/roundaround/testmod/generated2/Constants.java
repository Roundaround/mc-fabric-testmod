package me.roundaround.testmod.generated2;

import org.jetbrains.annotations.NotNull;

public final class Constants {
  @NotNull
  public static final String ROUNDALIB_VERSION = "2.2.2";

  @NotNull
  public static final String TEST = "Hello world!";

  @NotNull
  public static final String MOD_ID = "testmod";

  public static final boolean ROUNDALIB_SNAPSHOT = true;

  @NotNull
  public static final String VERSION = "1.0.0";

  @NotNull
  public static final String MINECRAFT_VERSION = "1.20.5";

  @NotNull
  public static final String FULL_VERSION = "1.0.0+1.20.5";

  @NotNull
  public static final String GRADLE_PLUGIN_VERSION = "1.0.0";

  @NotNull
  public static final String GROUP_ID = "me.roundaround";

  @NotNull
  public static final Variant ACTIVE_VARIANT = Variant.BASE;

  private Constants() {}

  public enum Variant {
    BASE(""),
    EXPERIMENTAL("experimental"),
    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    G("g"),
    H("h"),
    I("i");

    private final String id;

    Variant(String id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return this.id;
    }
  }
}
