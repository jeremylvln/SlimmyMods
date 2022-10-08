package fr.iamblueslime.computerthings.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.client.screen.widget.WidgetButton;
import fr.iamblueslime.computerthings.client.screen.widget.WidgetIcon;
import fr.iamblueslime.computerthings.init.ModNetwork;
import fr.iamblueslime.computerthings.menu.ElectronicPadDesignerMenu;
import fr.iamblueslime.computerthings.network.ServerboundCraftElectronicPadPacket;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class ElectronicPadDesignerScreen extends AbstractContainerScreen<ElectronicPadDesignerMenu> {
    private final ResourceLocation GUI = new ResourceLocation(ComputerThings.MODID, "textures/gui/electronic_pad_designer.png");
    private static final int BACKGROUND_WIDTH = 176;
    private static final int BACKGROUND_HEIGHT = 214;

    private WidgetButton confirmButton;

    public ElectronicPadDesignerScreen(ElectronicPadDesignerMenu menu, Inventory playerInventory, Component name) {
        super(menu, playerInventory, name);
    }

    @Override
    protected void init() {
        super.init();

        this.imageWidth = BACKGROUND_WIDTH;
        this.imageHeight = BACKGROUND_HEIGHT;
        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = BACKGROUND_HEIGHT - 96 + 2;

        this.confirmButton = new WidgetButton(this.leftPos + 128, this.topPos + 57, WidgetIcon.ARROW_DOWN, (button) -> {
            System.out.println("BUTTON CALLBACK");
            ModNetwork.sendToServer(new ServerboundCraftElectronicPadPacket());
        });
        this.confirmButton.active = false;
        this.renderables.add(this.confirmButton);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.confirmButton.active = this.menu.isCorrectlyFilled();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (this.confirmButton.mouseClicked(mouseX, mouseY, button))
            return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int middleX = (this.width - this.imageWidth) / 2;
        int middleY = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, middleX, middleY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
