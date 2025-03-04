package me.roundaround.testmod.config;

import me.roundaround.testmod.generated.Constants;
import me.roundaround.testmod.roundalib.config.ConfigPath;
import me.roundaround.testmod.roundalib.config.manage.ModConfigImpl;
import me.roundaround.testmod.roundalib.config.manage.store.WorldScopedFileStore;
import me.roundaround.testmod.roundalib.config.option.BooleanConfigOption;
import me.roundaround.testmod.roundalib.config.option.StringListConfigOption;

import java.util.List;

public class PerWorldTestModConfig extends ModConfigImpl implements WorldScopedFileStore {
  private static PerWorldTestModConfig instance = null;

  public static PerWorldTestModConfig getInstance() {
    if (instance == null) {
      instance = new PerWorldTestModConfig();
    }
    return instance;
  }

  public BooleanConfigOption first;
  public BooleanConfigOption second;
  public BooleanConfigOption third;
  public BooleanConfigOption fourth;
  public BooleanConfigOption fifth;
  public BooleanConfigOption sixth;
  public StringListConfigOption list;

  private PerWorldTestModConfig() {
    super(Constants.MOD_ID, "pw");
  }

  @Override
  protected void registerOptions() {
    this.first = this.register(BooleanConfigOption.builder(ConfigPath.of("pwTestOption0"))
        .setDefaultValue(true)
        .build());

    this.second = this.register(BooleanConfigOption.builder(ConfigPath.of("pwTestOption1"))
        .setDefaultValue(true)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build());

    this.third = this.buildRegistration(BooleanConfigOption.builder(ConfigPath.of("pwTestOption2"))
        .setDefaultValue(true)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build()).clientOnly().commit();

    this.fourth = this.buildRegistration(BooleanConfigOption.builder(ConfigPath.of("pwTestOption3"))
        .setDefaultValue(true)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build()).singlePlayerOnly().commit();

    this.fifth = this.buildRegistration(BooleanConfigOption.builder(ConfigPath.of("pwTestOption4"))
        .setDefaultValue(true)
        .onUpdate((option) -> option.setDisabled(!this.first.getPendingValue()))
        .build()).serverOnly().commit();

    this.list = this.buildRegistration(StringListConfigOption.builder(ConfigPath.of("pwTestListOption"))
        .setDefaultValue(List.of("A"))
        .build()).noGuiControl().commit();
  }
}
