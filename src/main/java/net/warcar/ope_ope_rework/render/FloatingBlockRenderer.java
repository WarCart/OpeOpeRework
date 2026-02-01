package net.warcar.ope_ope_rework.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.ope_ope_rework.projectiles.FloatingBlockEntity;

import java.util.Random;

public class FloatingBlockRenderer extends EntityRenderer<FloatingBlockEntity> {
    protected FloatingBlockRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(FloatingBlockEntity entity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        BlockState blockstate = entity.getBlockState();
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(getRot(entity.getRotationX(), entity.getRotationXTick(), pPartialTicks)));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(getRot(entity.getRotationY(), entity.getRotationYTick(), pPartialTicks)));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(getRot(entity.getRotationZ(), entity.getRotationZTick(), pPartialTicks)));
        if (blockstate.getRenderShape() == BlockRenderType.MODEL) {
            World world = entity.level;
            if (blockstate != world.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
                pMatrixStack.pushPose();
                BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                pMatrixStack.translate(-0.5F, 0.0F, -0.5F);
                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();

                for(RenderType type : RenderType.chunkBufferLayers()) {
                    if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
                        ForgeHooksClient.setRenderLayer(type);
                        blockrendererdispatcher.getModelRenderer().tesselateBlock(world, blockrendererdispatcher.getBlockModel(blockstate), blockstate, blockpos, pMatrixStack, pBuffer.getBuffer(type), false, new Random(), blockstate.getSeed(entity.getOriginPos()), OverlayTexture.NO_OVERLAY);
                    }
                }

                ForgeHooksClient.setRenderLayer(null);
                pMatrixStack.popPose();
                super.render(entity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
            }
        }
        pMatrixStack.popPose();
    }

    private float getRot(float rotation, float rotationTick, float pPartialTicks) {
        return MathHelper.lerp(pPartialTicks, rotation - rotationTick, rotation);
    }

    @Override
    public ResourceLocation getTextureLocation(FloatingBlockEntity entity) {
        return PlayerContainer.BLOCK_ATLAS;
    }

    public static class Factory implements IRenderFactory<FloatingBlockEntity> {
        @Override
        public EntityRenderer<? super FloatingBlockEntity> createRenderFor(EntityRendererManager manager) {
            return new FloatingBlockRenderer(manager);
        }
    }
}
