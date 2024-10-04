package net.warcar.ope_ope_rework.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.ope_ope_rework.models.SphereModel;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;

public class RoomRenderer<T extends RoomProjectile, M extends EntityModel<T>> extends EntityRenderer<T> {
    protected M model;
    public RoomRenderer(EntityRendererManager renderManager, M model) {
        super(renderManager);
        this.model = model;
    }

    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.scale(entity.getSize(), entity.getSize(), entity.getSize());
        IVertexBuilder builder = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR);
        this.model.renderToBuffer(matrixStack, builder, packedLight, OverlayTexture.NO_OVERLAY, 0, 0.75f, 1, 0.2f);
        matrixStack.popPose();
    }

    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return null;
    }

    public static class Factory implements IRenderFactory<RoomProjectile> {
        protected EntityModel model;

        public Factory() {
            this.model = new SphereModel();
        }

        public EntityRenderer<? super RoomProjectile> createRenderFor(EntityRendererManager manager) {
            return new RoomRenderer(manager, this.model);
        }
    }
}
