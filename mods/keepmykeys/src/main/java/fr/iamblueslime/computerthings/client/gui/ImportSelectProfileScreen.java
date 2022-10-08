package fr.iamblueslime.computerthings.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iamblueslime.computerthings.logic.ControlProfileRegistry;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class ImportSelectProfileScreen extends OptionsSubScreen {
    public static final TranslatableComponent TITLE_MESSAGE = new TranslatableComponent("options.keepmykeys.importselectprofile.title");

    private ControlProfileSelectionList list;
    private Button applyButton;
    private List<Component> toolTip;

    public ImportSelectProfileScreen(Screen pLastScreen, Options pOptions) {
        super(pLastScreen, pOptions, TITLE_MESSAGE);
    }

    @Override
    protected void init() {
        this.list = new ControlProfileSelectionList(this, this.minecraft, this.width, this.height, 32, this.height - 36);
        this.addWidget(this.list);
        this.applyButton = this.addRenderableWidget(new Button(this.width / 2 + 4, this.height - 28, 150, 20, CommonComponents.GUI_PROCEED, (button) -> {
            ControlProfileRegistry.INSTANCE.applyKeyBindMap(this.list.getKeyBindMap());
            this.minecraft.setScreen(this.lastScreen);
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 154, this.height - 28, 150, 20, CommonComponents.GUI_CANCEL, (button) ->
            this.minecraft.setScreen(this.lastScreen)));
        this.updateButtonStatus(false);
        this.list.refreshControlProfiles();
    }

    @Override
    public void render(PoseStack postStack, int mouseX, int mouseY, float partialTick) {
        this.toolTip = null;
        this.renderBackground(postStack);
        this.list.render(postStack, mouseX, mouseY, partialTick);
        drawCenteredString(postStack, this.font, this.title, this.width / 2, 13, 16777215);
        super.render(postStack, mouseX, mouseY, partialTick);

        if (this.toolTip != null) {
            this.renderComponentTooltip(postStack, this.toolTip, mouseX, mouseY);
        }
    }

    public void updateButtonStatus(boolean active) {
        this.applyButton.active = active;
    }

    public void setToolTip(List<Component> toolTip) {
        this.toolTip = toolTip;
    }
}
