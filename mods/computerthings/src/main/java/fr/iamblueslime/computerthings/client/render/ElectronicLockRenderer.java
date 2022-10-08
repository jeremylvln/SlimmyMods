package fr.iamblueslime.computerthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iamblueslime.computerthings.blockentity.ElectronicLockBlockEntity;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadData;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ElectronicLockRenderer implements BlockEntityRenderer<ElectronicLockBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ElectronicLockRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(ElectronicLockBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Optional<ElectronicPadData> padData = blockEntity.getPadData();
        Direction direction = blockEntity.getDirection();

        if (padData.isEmpty())
            return;

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1) {
            ElectronicPadEntry entry = padData.get().getEntry(i);

            Vec3 renderPosition = ElectronicPadEntry.getEntryRelativeVec(entry.getPadPosition(), direction);

            if (blockEntity.isClicked(i))
                renderPosition = renderPosition.add(getClickedTranslate(direction));

            poseStack.pushPose();
            poseStack.translate(renderPosition.x, renderPosition.y, renderPosition.z);
            poseStack.scale(0.0625F * 2, 0.0625F * 2, 0.0625F * 2);
            poseStack.translate(0.25D, 0.25D, 0.25D);
            this.itemRenderer.renderStatic(entry.getStack(), ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, 1);
            poseStack.popPose();
        }
    }

    private static Vec3 getClickedTranslate(Direction direction) {
        return switch (direction) {
            case SOUTH -> new Vec3(0.0D, 0.0D, -0.015625D);
            case NORTH -> new Vec3(0.0D, 0.0D, 0.015625D);
            case WEST -> new Vec3(0.015625D, 0.0D, 0.0D);
            case EAST -> new Vec3(-0.015625D, 0.0D, 0.0D);
            default -> throw new IllegalStateException("Invalid orientation");
        };
    }
}
