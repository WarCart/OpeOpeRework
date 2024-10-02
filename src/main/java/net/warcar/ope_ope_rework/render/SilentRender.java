package net.warcar.ope_ope_rework.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.ope_ope_rework.config.CommonConfig;
import net.warcar.ope_ope_rework.models.SphereModel;
import net.warcar.ope_ope_rework.projectiles.SilentProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;

@OnlyIn(Dist.CLIENT)
public class SilentRender<T extends SilentProjectile> extends EntityRenderer<T> {
    public SilentRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        IVertexBuilder builder = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR2);
        matrixStack.pushPose();
        matrixStack.scale(entity.getSize(), entity.getSize(), entity.getSize());
        if (!(CommonConfig.INSTANCE.getInvisibleSilent() && Minecraft.getInstance().player != entity.getOwner())) {
            new SphereModel().renderToBuffer(matrixStack, builder, packedLight, OverlayTexture.NO_OVERLAY, 0.25f, 0, 0.25f, 0.25f);
        }
        matrixStack.popPose();
    }

    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return null;
    }

    public static class Factory implements IRenderFactory<SilentProjectile> {
        public EntityRenderer<? super SilentProjectile> createRenderFor(EntityRendererManager manager) {
            return new SilentRender<>(manager);
        }
    }
}
