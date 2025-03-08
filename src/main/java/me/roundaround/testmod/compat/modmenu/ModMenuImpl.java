package me.roundaround.testmod.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.roundaround.roundalib.gradle.api.annotation.Entrypoint;
import me.roundaround.testmod.config.PerWorldTestModConfig;
import me.roundaround.testmod.config.TestModConfig;
import me.roundaround.testmod.generated.Constants;
import me.roundaround.testmod.roundalib.client.gui.screen.ConfigScreen;

@Entrypoint(Entrypoint.MOD_MENU)
public class ModMenuImpl implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return (parent) -> new ConfigScreen(
        parent,
        Constants.MOD_ID,
        TestModConfig.getInstance(),
        PerWorldTestModConfig.getInstance()
    );
  }
}
