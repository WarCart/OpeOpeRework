package net.warcar.ope_ope_rework.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.warcar.ope_ope_rework.render.SphereRenderer;

public class SphereModel extends EntityModel {
    private final SphereRenderer sphere;

    public SphereModel() {
        this.sphere = new SphereRenderer(this);
        sphere.setPos(0,0,0);
        sphere.addSphere(new SphereRenderer.SphereModel(0, 2, 0, 2, 2, 2, 0, 0 , 0, false, 0 , 0));
    }

    public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }

    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        p_225598_1_.pushPose();
        p_225598_1_.scale(10, 10, 10);
        this.sphere.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        p_225598_1_.popPose();
    }
}
