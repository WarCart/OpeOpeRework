package net.warcar.ope_ope_rework.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3f;
import xyz.pixelatedw.mineminenomi.api.animations.Animation;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;

public class SlicedAnimation extends Animation<LivingEntity, BipedModel<?>> {
    public SlicedAnimation(AnimationId<SlicedAnimation> animId) {
        super(animId);
        this.setAnimationSetup(this::setup);
        this.setAnimationAngles(this::angles);
    }

    public void angles(LivingEntity entity, BipedModel<?> entityModel, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RendererHelper.resetHumanoidModelToDefaultPivots(entityModel);
        entityModel.head.y = 1.5F;
        entityModel.head.xRot = (float)Math.toRadians(50.0);
        ModelRenderer var10000 = entityModel.rightArm;
        var10000.xRot += -0.62831855F;
        var10000 = entityModel.leftArm;
        var10000.xRot += -0.62831855F;
        entityModel.rightLeg.visible = false;
        entityModel.leftLeg.visible = false;
    }

    public void setup(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, float rotationYaw, float partialTicks) {
        matrixStack.mulPose(Vector3f.XN.rotationDegrees(90.0F));
        matrixStack.translate(0.0, -0.5, 1.4);
    }
}
