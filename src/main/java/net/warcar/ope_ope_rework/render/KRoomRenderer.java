package net.warcar.ope_ope_rework.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.ope_ope_rework.abilities.KRoomAbility;
import net.warcar.ope_ope_rework.models.KRoomModel;
import net.warcar.ope_ope_rework.projectiles.KRoomProjectile;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.models.abilities.CubeModel;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;

@OnlyIn(Dist.CLIENT)
public class KRoomRenderer<T extends KRoomProjectile, M extends EntityModel<T>> extends AbilityProjectileRenderer<T, M> {
    public KRoomRenderer(EntityRendererManager renderManager) {
        super(renderManager, null, null);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        if (entity.getThrower() != null && entity.getThrower().isAlive()) {
            Vector3d stretchVec = entity.getThrower().position().vectorTo(entity.position());
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks - 180.0F));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks));
            matrixStack.mulPose(new Quaternion(Vector3f.ZP, 180.0F, true));
            KRoomModel model = new KRoomModel();
            float size = (float) stretchVec.length();
            matrixStack.scale(1, 1, size * 1.75f);
            matrixStack.translate(0, 0, 0.3);
            KRoomAbility ability = AbilityDataCapability.get(entity.getThrower()).getEquippedAbility(KRoomAbility.INSTANCE);
            Vector3f color;
            if (ability != null && ability.isShock()) {
                color = new Vector3f(1, 0.75f, 0);
            } else {
                color = new Vector3f(0, 0.75f, 1);
            }
            matrixStack.pushPose();
            matrixStack.scale(0.1f, 1f, 01f);
            new CubeModel().renderToBuffer(matrixStack, buffer.getBuffer(ModRenderTypes.ENERGY), packedLight, OverlayTexture.NO_OVERLAY, color.x(), color.y(), color.z(), 0.5f);
            matrixStack.popPose();
            matrixStack.scale(1, 1, -1);
            model.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation("ope_rework", "textures/models/kroom.png"))),
                    packedLight, OverlayTexture.NO_OVERLAY, 1, 1,1, 1);
            matrixStack.popPose();
        }
    }

    public static class Factory implements IRenderFactory<KRoomProjectile> {
        public EntityRenderer<? super KRoomProjectile> createRenderFor(EntityRendererManager manager) {
            return new KRoomRenderer<>(manager);
        }
    }
}
