package net.warcar.ope_ope_rework.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class KRoomModel extends EntityModel<Entity> {
	private final ModelRenderer bone;

	public KRoomModel() {
		bone = new ModelRenderer(this);
		bone.setPos(0, 0, 0);
		bone.xRot = -1.5708F;
		bone.texOffs(0, 0).addBox(-0.5F, -4, -4.0F, 0.0F, 39.5F, 5.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		matrixStack.pushPose();
		matrixStack.translate(0, 0, 0.25);
		bone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		matrixStack.popPose();
	}
}