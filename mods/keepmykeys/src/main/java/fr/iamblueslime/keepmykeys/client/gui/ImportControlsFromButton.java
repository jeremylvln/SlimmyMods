package fr.iamblueslime.keepmykeys.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class ImportControlsFromButton extends Button {
    public static final TranslatableComponent[] MESSAGE_COMPONENTS = {
            new TranslatableComponent("options.keepmykeys.import.vanilla"),
            new TranslatableComponent("options.keepmykeys.import.curseforge"),
    };

    private int currentTextIndex = 0;
    private long titleTicks = 0;

    public ImportControlsFromButton(Screen screen, int pX, int pY, int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight, MESSAGE_COMPONENTS[0], (button) -> {
            screen.getMinecraft().setScreen(new ImportSelectProfileScreen(screen, screen.getMinecraft().options));
        });
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.titleTicks += 1;

        if (this.titleTicks == 100) {
            this.titleTicks = 0;
            this.currentTextIndex += 1;

            if (this.currentTextIndex == MESSAGE_COMPONENTS.length) {
                this.currentTextIndex = 0;
            }

            this.setMessage(MESSAGE_COMPONENTS[this.currentTextIndex]);
        }

        super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
