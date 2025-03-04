package me.roundaround.testmod.client.screen.demo;

import me.roundaround.testmod.generated.Constants;
import me.roundaround.testmod.roundalib.asset.icon.BuiltinIcon;
import me.roundaround.testmod.roundalib.client.gui.GuiUtil;
import me.roundaround.testmod.roundalib.client.gui.layout.linear.LinearLayoutWidget;
import me.roundaround.testmod.roundalib.client.gui.layout.screen.ThreeSectionLayoutWidget;
import me.roundaround.testmod.roundalib.client.gui.util.Axis;
import me.roundaround.testmod.roundalib.client.gui.widget.IconButtonWidget;
import me.roundaround.testmod.roundalib.client.gui.widget.NarratableEntryListWidget;
import me.roundaround.testmod.roundalib.client.gui.widget.drawable.DrawableWidget;
import me.roundaround.testmod.roundalib.client.gui.widget.drawable.LabelWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Divider;

import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class AdvancedLayoutDemoScreen extends Screen implements DemoScreen {
  private static final Text TITLE_TEXT = Text.translatable("testmod.advancedlayoutdemoscreen.title");
  private static final int BUTTON_HEIGHT = 20;

  private final Screen parent;
  private final ThreeSectionLayoutWidget layout = new ThreeSectionLayoutWidget(this);

  public AdvancedLayoutDemoScreen(Screen parent) {
    super(TITLE_TEXT);
    this.parent = parent;
  }

  @Override
  protected void init() {
    this.layout.addHeader(this.textRenderer, this.title);

    this.layout.getBody().flowAxis(Axis.HORIZONTAL).spacing(2 * GuiUtil.PADDING);

    LinearLayoutWidget leftPane = this.layout.addBody(LinearLayoutWidget.vertical()
        .defaultOffAxisContentAlignEnd()
        .spacing(GuiUtil.PADDING), (parent, self) -> {
      Divider divider = new Divider(parent.getWidth() - parent.getSpacing(), 2);
      self.setDimensions(divider.nextInt(), parent.getHeight());
    });

    LinearLayoutWidget searchRow = leftPane.add(LinearLayoutWidget.horizontal().spacing(GuiUtil.PADDING),
        (parent, self) -> self.setDimensions(leftPane.getWidth() - GuiUtil.PADDING, BUTTON_HEIGHT)
    );

    searchRow.add(new TextFieldWidget(this.textRenderer, 0, BUTTON_HEIGHT, Text.of("Search")),
        (parent, self) -> self.setWidth(parent.getWidth() - parent.getSpacing() - IconButtonWidget.SIZE_V)
    );

    searchRow.add(IconButtonWidget.builder(BuiltinIcon.FILTER_18, Constants.MOD_ID)
        .vanillaSize()
        .messageAndTooltip(Text.of("Filter"))
        .onPress((button) -> GuiUtil.playClickSound())
        .build());

    PlaceholderListWidget listWidget = leftPane.add(new PlaceholderListWidget(this.client),
        (parent, self) -> self.setDimensions(parent.getWidth(),
            parent.getHeight() - parent.getSpacing() - BUTTON_HEIGHT
        )
    );
    listWidget.addRows(15);

    LinearLayoutWidget rightPane = this.layout.addBody(LinearLayoutWidget.vertical()
        .spacing(GuiUtil.PADDING)
        .defaultOffAxisContentAlignCenter(), (parent, self) -> {
      Divider divider = new Divider(parent.getWidth() - parent.getSpacing(), 2);
      divider.skip(1);
      self.setDimensions(divider.nextInt(), parent.getHeight());
    });

    LabelWidget label = rightPane.add(LabelWidget.builder(this.textRenderer, Text.of("Label"))
            .alignTextCenterX()
            .alignTextCenterY()
            .hideBackground()
            .showShadow()
            .build(),
        (parent, self) -> self.setDimensions(parent.getWidth(), LabelWidget.getDefaultHeight(this.textRenderer))
    );

    rightPane.add(new DrawableWidget() {
      @Override
      protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int left = this.getX();
        int top = this.getY();
        int width = this.getWidth();
        int height = this.getHeight();
        context.fill(left, top, left + width, top + height, GuiUtil.genColorInt(0, 0, 0));
        context.drawBorder(left, top, width, height, GuiUtil.genColorInt(0.8f, 0.2f, 0.6f));
      }

      @Override
      protected boolean isValidClickButton(int button) {
        return button == 0;
      }

      @Override
      public void onClick(double mouseX, double mouseY) {
        listWidget.clearEntries();
        listWidget.addRows(15);
      }
    }, (parent, self) -> self.setDimensions(parent.getWidth() - 2 * GuiUtil.PADDING,
        parent.getHeight() - label.getHeight() - IconButtonWidget.SIZE_V - 2 * parent.getSpacing()
    ));

    LinearLayoutWidget controlsRow = LinearLayoutWidget.horizontal()
        .spacing(GuiUtil.PADDING)
        .defaultOffAxisContentAlignCenter();
    controlsRow.add(IconButtonWidget.builder(BuiltinIcon.PREV_18, Constants.MOD_ID)
        .vanillaSize()
        .messageAndTooltip(Text.of("Previous"))
        .build());
    controlsRow.add(LabelWidget.builder(this.textRenderer,
                Text.of(String.format("%s total items", listWidget.getEntryCount()))
            )
            .alignTextCenterX()
            .alignTextCenterY()
            .hideBackground()
            .showShadow()
            .overflowBehavior(LabelWidget.OverflowBehavior.SCROLL)
            .build(),
        (parent, self) -> self.setWidth(parent.getWidth() - 2 * (2 * GuiUtil.PADDING + IconButtonWidget.SIZE_V))
    );
    controlsRow.add(IconButtonWidget.builder(BuiltinIcon.NEXT_18, Constants.MOD_ID)
        .vanillaSize()
        .messageAndTooltip(Text.of("Next"))
        .build());
    rightPane.add(controlsRow, (parent, self) -> {
      self.setWidth(parent.getWidth());
    });

    this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close()).build());

    this.layout.forEachChild(this::addDrawableChild);
    this.initTabNavigation();
  }

  @Override
  protected void initTabNavigation() {
    this.layout.refreshPositions();
  }

  @Override
  public void close() {
    Objects.requireNonNull(this.client).setScreen(this.parent);
  }

  @Environment(value = EnvType.CLIENT)
  public static class PlaceholderListWidget extends NarratableEntryListWidget<PlaceholderListWidget.Entry> {
    public PlaceholderListWidget(MinecraftClient client) {
      super(client, 0, 0, 0, 0);
    }

    public void addRows(int num) {
      for (int i = 0; i < num; i++) {
        this.addEntry((index, left, top, width) -> new Entry(this.client.textRenderer, index, left, top, width));
      }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Entry extends NarratableEntryListWidget.Entry {
      public Entry(TextRenderer textRenderer, int index, int left, int top, int width) {
        super(index, left, top, width, 36);

        LinearLayoutWidget layout = this.addLayout(LinearLayoutWidget.horizontal().spacing(GuiUtil.PADDING), (self) -> {
          self.setPosition(this.getContentLeft(), this.getContentTop());
          self.setDimensions(this.getContentWidth(), this.getContentHeight());
        });

        layout.add(new DrawableWidget() {
          @Override
          protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            int rectWidth = (int) (0.7f * this.getWidth());
            int left = this.getX() + (this.getWidth() - rectWidth) / 2;
            context.fill(left, this.getY(), left + rectWidth, this.getBottom(), Colors.BLACK);
            context.drawBorder(left, this.getY(), rectWidth, this.getHeight(), GuiUtil.genColorInt(0.8f, 0.2f, 0.6f));
          }
        }, (parent, self) -> self.setDimensions(this.getContentHeight(), this.getContentHeight()));

        int num = index + 1;
        List<Text> lines = List.of(Text.of(String.format("Row #%s", num)),
            Text.of(String.format("%s * %s = %s", num, num, num * num)),
            Text.literal("Very long line of text that is sure to overflow outside the container")
                .setStyle(Style.EMPTY.withItalic(true).withColor(Colors.GRAY))
        );
        LabelWidget label = LabelWidget.builder(textRenderer, lines)
            .alignTextLeft()
            .alignTextCenterY()
            .overflowBehavior(LabelWidget.OverflowBehavior.TRUNCATE)
            .hideBackground()
            .showShadow()
            .build();
        layout.add(label, (parent, self) -> {
          self.setDimensions(this.getContentWidth() - GuiUtil.PADDING - this.getContentHeight(),
              this.getContentHeight()
          );
        });

        layout.forEachChild(this::addDrawable);
      }

      @Override
      public Text getNarration() {
        return ScreenTexts.EMPTY;
      }
    }
  }
}
