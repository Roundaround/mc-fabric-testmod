package me.roundaround.testmod;

import me.roundaround.roundalib.gradle.api.annotation.Entrypoint;
import me.roundaround.testmod.config.PerWorldTestModConfig;
import me.roundaround.testmod.config.TestModConfig;
import me.roundaround.testmod.generated.Constants;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entrypoint(Entrypoint.MAIN)
public final class TestMod implements ModInitializer {
  public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_ID);

  @Override
  public void onInitialize() {
    TestModConfig.getInstance().init();
    PerWorldTestModConfig.getInstance().init();
  }
}
