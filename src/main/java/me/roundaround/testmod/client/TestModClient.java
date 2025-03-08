package me.roundaround.testmod.client;

import me.roundaround.roundalib.gradle.api.annotation.Entrypoint;
import me.roundaround.testmod.client.screen.ExamplePositionEditScreen;
import me.roundaround.testmod.client.screen.demo.DemoScreen;
import me.roundaround.testmod.client.screen.demo.DemoSelectScreen;
import me.roundaround.testmod.roundalib.client.event.ScreenInputEvent;
import me.roundaround.testmod.roundalib.client.gui.GuiUtil;
import me.roundaround.testmod.roundalib.client.gui.widget.config.ControlRegistry;
import me.roundaround.testmod.roundalib.client.gui.widget.config.SubScreenControl;
import me.roundaround.testmod.roundalib.config.option.PositionConfigOption;
import me.roundaround.testmod.roundalib.config.value.Position;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

@Entrypoint(Entrypoint.CLIENT)
public class TestModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    try {
      ControlRegistry.register("testOption9", TestModClient::getSubScreenControl);
      ControlRegistry.register("testOption10", TestModClient::getSubScreenControl);
    } catch (ControlRegistry.RegistrationException e) {
      throw new RuntimeException(e);
    }

    ScreenInputEvent.EVENT.register((screen, keyCode, scanCode, modifiers) -> {
      if (screen instanceof DemoScreen) {
        return false;
      }
      if (Screen.hasControlDown() && Screen.hasShiftDown() && keyCode == GLFW.GLFW_KEY_D) {
        GuiUtil.setScreen(new DemoSelectScreen(screen));
        return true;
      }
      return false;
    });
  }

  private static SubScreenControl<Position, PositionConfigOption> getSubScreenControl(
      MinecraftClient client, PositionConfigOption option, int width, int height
  ) {
    return new SubScreenControl<>(
        client,
        option,
        width,
        height,
        SubScreenControl.getValueDisplayMessageFactory(),
        ExamplePositionEditScreen.getSubScreenFactory()
    );
  }
}
