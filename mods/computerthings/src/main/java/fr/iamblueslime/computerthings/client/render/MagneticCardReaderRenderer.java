package fr.iamblueslime.computerthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import fr.iamblueslime.computerthings.blockentity.ElectronicLockBlockEntity;
import fr.iamblueslime.computerthings.blockentity.MagneticCardReaderBlockEntity;
import fr.iamblueslime.computerthings.init.ModItems;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class MagneticCardReaderRenderer implements BlockEntityRenderer<MagneticCardReaderBlockEntity> {
    private static final double ANIMATION_STEP = 0.5D / MagneticCardReaderBlockEntity.ANIMATION_TICKS;

    private final ItemRenderer itemRenderer;

    public MagneticCardReaderRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(MagneticCardReaderBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getSwipeAnimationCard().isEmpty() || blockEntity.getSwipeAnimationTicks().isEmpty())
            return;

        Direction direction = blockEntity.getDirection();
        ItemStack card = blockEntity.getSwipeAnimationCard().get();
        int animationTicks = blockEntity.getSwipeAnimationTicks().get();

        Vec3 cardPosition = getCardRelativeVec(direction);
        Vec3 animationDelta = new Vec3(0.0D, ANIMATION_STEP * animationTicks, 0.0D);

        poseStack.pushPose();
        poseStack.translate(cardPosition.x, cardPosition.y, cardPosition.z);
        poseStack.translate(animationDelta.x, animationDelta.y, animationDelta.z);
        poseStack.scale(0.42F, 0.42F, 0.42F);
        poseStack.mulPose(getHRotationQuaternion(direction));
        poseStack.mulPose(getVRotationQuaternion(direction));
        this.itemRenderer.renderStatic(card, ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, 1);
        poseStack.popPose();
    }

    private static Vec3 getCardRelativeVec(Direction direction) {
        return switch (direction) {
            case NORTH -> new Vec3(0.4525D, 0.35D, 0.875D);
            case SOUTH -> new Vec3(0.5475D, 0.35D, 0.125D);
            case WEST -> new Vec3(0.875D, 0.35D, 0.5475D);
            case EAST -> new Vec3(0.125D, 0.35D, 0.4525D);
            default -> throw new IllegalStateException("Invalid direction");
        };
    }

    private static Quaternion getHRotationQuaternion(Direction direction) {
        float hAngle = switch (direction) {
            case NORTH, SOUTH -> 90.0F;
            case WEST, EAST -> 0.0F;
            default -> throw new IllegalStateException("Invalid direction");
        };

        return Vector3f.YP.rotationDegrees(hAngle);
    }

    private static Quaternion getVRotationQuaternion(Direction direction) {
        Vector3f vVec = switch (direction) {
            case NORTH, EAST -> Vector3f.ZN;
            case SOUTH, WEST -> Vector3f.ZP;
            default -> throw new IllegalStateException("Invalid direction");
        };

        return vVec.rotationDegrees(90);
    }
}
