package me.roundaround.testmod.config;

import me.roundaround.testmod.generated.Constants;
import me.roundaround.testmod.roundalib.config.ConfigPath;
import me.roundaround.testmod.roundalib.config.manage.ModConfigImpl;
import me.roundaround.testmod.roundalib.config.manage.store.GameScopedFileStore;
import me.roundaround.testmod.roundalib.config.option.*;
import me.roundaround.testmod.roundalib.config.value.Difficulty;
import me.roundaround.testmod.roundalib.config.value.Position;
import me.roundaround.testmod.roundalib.nightconfig.core.Config;

import java.util.Arrays;
import java.util.Map;

public class TestModConfig extends ModConfigImpl implements GameScopedFileStore {
  private static TestModConfig instance = null;

  public static TestModConfig getInstance() {
    if (instance == null) {
      instance = new TestModConfig();
    }
    return instance;
  }

  public BooleanConfigOption first;
  public BooleanConfigOption second;
  public EnumConfigOption<Difficulty> third;
  public StringConfigOption fourth;
  public IntConfigOption fifth;
  public IntConfigOption sixth;
  public IntConfigOption seventh;
  public FloatConfigOption eighth;
  public FloatConfigOption ninth;
  public PositionConfigOption tenth;
  public PositionConfigOption eleventh;
  public IntConfigOption twelfth;
  public BooleanConfigOption thirteenth;

  private TestModConfig() {
    super(Constants.MOD_ID, 3);
  }

  @Override
  protected void registerOptions() {
    this.first = this.register(BooleanConfigOption.builder(ConfigPath.of("group0", "testOption0"))
        .setDefaultValue(true)
        .build());

    this.second = this.register(BooleanConfigOption.builder(ConfigPath.of("group0", "testOption1"))
        .setDefaultValue(true)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build());

    this.third = this.register(EnumConfigOption.builder(ConfigPath.of("group0", "testOption2"),
            Arrays.stream(Difficulty.values()).toList()
        )
        .setDefaultValue(Difficulty.getDefault())
        .build());

    this.fourth = this.register(StringConfigOption.builder(ConfigPath.of("group0", "testOption3"))
        .setDefaultValue("foo")
        .setMinLength(3)
        .setMaxLength(12)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .valueWhenDisabled((option) -> "wowie!")
        .build());

    this.fifth = this.register(IntConfigOption.builder(ConfigPath.of("group1", "testOption4"))
        .setDefaultValue(5)
        .setMinValue(0)
        .setMaxValue(100)
        .setStep(5)
        .addValidator((value, option) -> value % 25 != 0)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .defaultWhenDisabled()
        .build());

    this.sixth = this.register(IntConfigOption.builder(ConfigPath.of("group1", "testOption5"))
        .setDefaultValue(5)
        .setMinValue(0)
        .setMaxValue(100)
        .setStep(null)
        .addValidator((value, option) -> value % 25 != 0)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build());

    this.seventh = this.register(IntConfigOption.builder(ConfigPath.of("group1", "testOption6"))
        .setDefaultValue(5)
        .setMinValue(0)
        .setMaxValue(100)
        .setStep(5)
        .setUseSlider(true)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build());

    this.eighth = this.register(FloatConfigOption.builder(ConfigPath.of("group1", "testOption7"))
        .setDefaultValue(5)
        .setMinValue(0)
        .setMaxValue(100)
        .build());

    this.ninth = this.register(FloatConfigOption.builder(ConfigPath.of("group1", "testOption8"))
        .setDefaultValue(5)
        .setMinValue(0)
        .setMaxValue(100)
        .setUseSlider(true)
        .build());

    this.tenth = this.register(PositionConfigOption.builder(ConfigPath.of("group2", "testOption9")).build());

    this.eleventh = this.register(PositionConfigOption.builder(ConfigPath.of("group2", "testOption10"))
        .setDefaultValue(new Position(50, 50))
        .build());

    this.twelfth = this.register(IntConfigOption.builder(ConfigPath.of("group2", "testOption11"))
        .setDefaultValue(5)
        .setMinValue(0)
        .setMaxValue(100)
        .setStep(5)
        .setUseSlider(true)
        .build());

    this.thirteenth = this.register(BooleanConfigOption.builder(ConfigPath.of("group3", "testOption12"))
        .setDefaultValue(true)
        .build());
  }

  @Override
  public boolean performConfigUpdate(int versionSnapshot, Config inMemoryConfigSnapshot) {
    if (versionSnapshot == 1) {
      // Added a new group1.testOption5 so everything after needs shifting.
      // testOption8 is now also part of group1 rather than group2

      inMemoryConfigSnapshot.set(this.getLegacyPath(2, 11), inMemoryConfigSnapshot.get(this.getLegacyPath(2, 10)));
      inMemoryConfigSnapshot.set(this.getLegacyPath(2, 10), inMemoryConfigSnapshot.get(this.getLegacyPath(2, 9)));
      inMemoryConfigSnapshot.set(this.getLegacyPath(2, 9), inMemoryConfigSnapshot.get(this.getLegacyPath(2, 8)));
      inMemoryConfigSnapshot.remove(this.getLegacyPath(2, 8));
      inMemoryConfigSnapshot.set(this.getLegacyPath(1, 8), inMemoryConfigSnapshot.get(this.getLegacyPath(1, 7)));
      inMemoryConfigSnapshot.set(this.getLegacyPath(1, 7), inMemoryConfigSnapshot.get(this.getLegacyPath(1, 6)));
      inMemoryConfigSnapshot.set(this.getLegacyPath(1, 6), inMemoryConfigSnapshot.get(this.getLegacyPath(1, 5)));
      inMemoryConfigSnapshot.remove(this.getLegacyPath(1, 5));

      return this.performConfigUpdate(2, inMemoryConfigSnapshot);
    }

    if (versionSnapshot == 2) {
      // Removed modId prefixing on paths

      Map.copyOf(inMemoryConfigSnapshot.valueMap()).forEach((path, value) -> {
        if (path.startsWith(this.modId + ".")) {
          inMemoryConfigSnapshot.set(this.removeFirstSegment(path), value);
          inMemoryConfigSnapshot.remove(path);
        }
      });

      return true;
    }

    return false;
  }

  private ConfigPath getPath(int groupNum, int idNum) {
    return ConfigPath.of("group" + groupNum, "testOption" + idNum);
  }

  private String removeFirstSegment(String path) {
    return ConfigPath.parse(path).getId();
  }

  private String getLegacyPath(int groupNum, int idNum) {
    return this.modId + "." + this.getPath(groupNum, idNum);
  }
}
