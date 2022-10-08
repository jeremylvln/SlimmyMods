package fr.iamblueslime.computerthings.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;

public class WidgetButton extends Button {
    private final WidgetIcon icon;

    public WidgetButton(int x, int y, WidgetIcon icon, Button.OnPress onPress) {
        super(x, y, WidgetIcon.BUTTON_IDLE.getWidth(), WidgetIcon.BUTTON_IDLE.getHeight(), new TextComponent(""), onPress);
        this.icon = icon;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        WidgetIcon backgroundIcon = WidgetIcon.BUTTON_IDLE;

        if (!this.active)
            backgroundIcon = WidgetIcon.BUTTON_DISABLED;
        else if (this.isHoveredOrFocused())
            backgroundIcon = WidgetIcon.BUTTON_HOVER;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WidgetIcon.TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, this.x, this.y, backgroundIcon.getX(), backgroundIcon.getY(), this.width, this.height);
        this.blit(poseStack, this.x, this.y, this.icon.getX(), this.icon.getY(), this.width, this.height);
    }
}
