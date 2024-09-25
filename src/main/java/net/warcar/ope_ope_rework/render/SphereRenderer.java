package net.warcar.ope_ope_rework.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SphereRenderer extends ModelRenderer {
    protected final List<SphereModel> spheres = new ArrayList<>();
    public SphereRenderer(Model model) {
        super(model);
    }

    public void addSphere(SphereModel model) {
        this.spheres.add(model);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float r, float g, float b, float a) {
        super.render(stack, builder, light, overlay, r, g, b, a);
        if (this.visible) {
            this.additionalCompile(stack.last(), builder, light, overlay, r, g, b, a);
        }
    }

    protected void additionalCompile(MatrixStack.Entry entry, IVertexBuilder builder, int light, int overlay, float r, float g, float b, float a) {
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();

        for(SphereModel model : this.spheres) {
            for(Polygon polygon : model.polygons) {
                Vector3f vector3f = polygon.normal.copy();
                vector3f.transform(matrix3f);

                for (Vertex vertex : polygon.vertices) {
                    Vector4f vector4f = new Vector4f(vertex.pos.x() / 16f, vertex.pos.y() / 16f, vertex.pos.z() / 16f, 1);
                    vector4f.transform(matrix4f);
                    builder.vertex(vector4f.x(), vector4f.y(), vector4f.z(), r, g, b, a, vertex.u, vertex.v, overlay, light, vector3f.x(), vector3f.y(), vector3f.z());
                }
            }
        }
    }

    public static class SphereModel {
        protected List<Polygon> polygons = new ArrayList<>();
        protected Vector3f sphereCenter;
        protected Vector3f sphereSize;

        public SphereModel(float p_i225950_3_, float p_i225950_4_, float p_i225950_5_, float p_i225950_6_, float p_i225950_7_, float p_i225950_8_, float p_i225950_9_, float p_i225950_10_, float p_i225950_11_, boolean p_i225950_12_, float p_i225950_13_, float p_i225950_14_) {
            this.sphereCenter = new Vector3f(p_i225950_3_, p_i225950_4_, p_i225950_5_);
            this.sphereSize = new Vector3f(p_i225950_6_, p_i225950_7_, p_i225950_8_);
            List<Vertex> vertices = new ArrayList<>();
            vertices.add(new Vertex(sphereCenter.x() + 0.0f * sphereSize.x(), sphereCenter.y() + -1.0f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.181819f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + 0.723607f * sphereSize.x(), sphereCenter.y() + -0.44722f * sphereSize.y(), sphereCenter.z() + 0.525725f * sphereSize.z(), 0.204546f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + -0.276388f * sphereSize.x(), sphereCenter.y() + -0.44722f * sphereSize.y(), sphereCenter.z() + 0.850649f * sphereSize.z(), 0.159092f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + -0.894426f * sphereSize.x(), sphereCenter.y() + -0.447216f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.272728f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.276388f * sphereSize.x(), sphereCenter.y() + -0.44722f * sphereSize.y(), sphereCenter.z() + -0.850649f * sphereSize.z(), 0.295455f, 0.118096f));
            vertices.add(new Vertex(sphereCenter.x() + 0.723607f * sphereSize.x(), sphereCenter.y() + -0.44722f * sphereSize.y(), sphereCenter.z() + -0.525725f * sphereSize.z(), 0.318182f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.276388f * sphereSize.x(), sphereCenter.y() + 0.44722f * sphereSize.y(), sphereCenter.z() + 0.850649f * sphereSize.z(), 0.909091f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + -0.723607f * sphereSize.x(), sphereCenter.y() + 0.44722f * sphereSize.y(), sphereCenter.z() + 0.525725f * sphereSize.z(), 0.931818f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + -0.723607f * sphereSize.x(), sphereCenter.y() + 0.44722f * sphereSize.y(), sphereCenter.z() + -0.525725f * sphereSize.z(), 0.886364f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + 0.276388f * sphereSize.x(), sphereCenter.y() + 0.44722f * sphereSize.y(), sphereCenter.z() + -0.850649f * sphereSize.z(), 0.727273f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + 0.894426f * sphereSize.x(), sphereCenter.y() + 0.447216f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.75f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + 0.0f * sphereSize.x(), sphereCenter.y() + 1.0f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.704546f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + -0.232822f * sphereSize.x(), sphereCenter.y() + -0.657519f * sphereSize.y(), sphereCenter.z() + 0.716563f * sphereSize.z(), 0.545455f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + -0.162456f * sphereSize.x(), sphereCenter.y() + -0.850654f * sphereSize.y(), sphereCenter.z() + 0.499995f * sphereSize.z(), 0.568182f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + -0.077607f * sphereSize.x(), sphereCenter.y() + -0.96795f * sphereSize.y(), sphereCenter.z() + 0.238853f * sphereSize.z(), 0.522728f, 0.039365f));
            vertices.add(new Vertex(sphereCenter.x() + 0.203181f * sphereSize.x(), sphereCenter.y() + -0.96795f * sphereSize.y(), sphereCenter.z() + 0.147618f * sphereSize.z(), 0.295455f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.425323f * sphereSize.x(), sphereCenter.y() + -0.850654f * sphereSize.y(), sphereCenter.z() + 0.309011f * sphereSize.z(), 0.09091f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.609547f * sphereSize.x(), sphereCenter.y() + -0.657519f * sphereSize.y(), sphereCenter.z() + 0.442856f * sphereSize.z(), 0.136364f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.531941f * sphereSize.x(), sphereCenter.y() + -0.502302f * sphereSize.y(), sphereCenter.z() + 0.681712f * sphereSize.z(), 0.113637f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.262869f * sphereSize.x(), sphereCenter.y() + -0.525738f * sphereSize.y(), sphereCenter.z() + 0.809012f * sphereSize.z(), 0.818182f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.029639f * sphereSize.x(), sphereCenter.y() + -0.502302f * sphereSize.y(), sphereCenter.z() + 0.864184f * sphereSize.z(), 0.863636f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.812729f * sphereSize.x(), sphereCenter.y() + -0.502301f * sphereSize.y(), sphereCenter.z() + -0.295238f * sphereSize.z(), 0.840909f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.850648f * sphereSize.x(), sphereCenter.y() + -0.525736f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.636364f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.812729f * sphereSize.x(), sphereCenter.y() + -0.502301f * sphereSize.y(), sphereCenter.z() + 0.295238f * sphereSize.z(), 0.681818f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.203181f * sphereSize.x(), sphereCenter.y() + -0.96795f * sphereSize.y(), sphereCenter.z() + -0.147618f * sphereSize.z(), 0.659091f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.425323f * sphereSize.x(), sphereCenter.y() + -0.850654f * sphereSize.y(), sphereCenter.z() + -0.309011f * sphereSize.z(), 0.454546f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.609547f * sphereSize.x(), sphereCenter.y() + -0.657519f * sphereSize.y(), sphereCenter.z() + -0.442856f * sphereSize.z(), 0.5f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.753442f * sphereSize.x(), sphereCenter.y() + -0.657515f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.477273f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.52573f * sphereSize.x(), sphereCenter.y() + -0.850652f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.250001f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.251147f * sphereSize.x(), sphereCenter.y() + -0.967949f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.068182f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.483971f * sphereSize.x(), sphereCenter.y() + -0.502302f * sphereSize.y(), sphereCenter.z() + 0.716565f * sphereSize.z(), 0.795455f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.688189f * sphereSize.x(), sphereCenter.y() + -0.525736f * sphereSize.y(), sphereCenter.z() + 0.499997f * sphereSize.z(), 0.613637f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.831051f * sphereSize.x(), sphereCenter.y() + -0.502299f * sphereSize.y(), sphereCenter.z() + 0.238853f * sphereSize.z(), 0.431819f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.232822f * sphereSize.x(), sphereCenter.y() + -0.657519f * sphereSize.y(), sphereCenter.z() + -0.716563f * sphereSize.z(), 0.181819f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.162456f * sphereSize.x(), sphereCenter.y() + -0.850654f * sphereSize.y(), sphereCenter.z() + -0.499995f * sphereSize.z(), 0.227273f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.077607f * sphereSize.x(), sphereCenter.y() + -0.96795f * sphereSize.y(), sphereCenter.z() + -0.238853f * sphereSize.z(), 0.204546f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.831051f * sphereSize.x(), sphereCenter.y() + -0.502299f * sphereSize.y(), sphereCenter.z() + -0.238853f * sphereSize.z(), 0.0f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.688189f * sphereSize.x(), sphereCenter.y() + -0.525736f * sphereSize.y(), sphereCenter.z() + -0.499997f * sphereSize.z(), 0.045455f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.483971f * sphereSize.x(), sphereCenter.y() + -0.502302f * sphereSize.y(), sphereCenter.z() + -0.716565f * sphereSize.z(), 0.022727f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.029639f * sphereSize.x(), sphereCenter.y() + -0.502302f * sphereSize.y(), sphereCenter.z() + -0.864184f * sphereSize.z(), 0.727273f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.262869f * sphereSize.x(), sphereCenter.y() + -0.525738f * sphereSize.y(), sphereCenter.z() + -0.809012f * sphereSize.z(), 0.772727f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.531941f * sphereSize.x(), sphereCenter.y() + -0.502302f * sphereSize.y(), sphereCenter.z() + -0.681712f * sphereSize.z(), 0.75f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.956626f * sphereSize.x(), sphereCenter.y() + 0.251149f * sphereSize.y(), sphereCenter.z() + 0.147618f * sphereSize.z(), 0.545455f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.951058f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + 0.309013f * sphereSize.z(), 0.590909f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.860698f * sphereSize.x(), sphereCenter.y() + -0.251151f * sphereSize.y(), sphereCenter.z() + 0.442858f * sphereSize.z(), 0.568182f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.860698f * sphereSize.x(), sphereCenter.y() + -0.251151f * sphereSize.y(), sphereCenter.z() + -0.442858f * sphereSize.z(), 0.363637f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.951058f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + -0.309013f * sphereSize.z(), 0.409091f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.956626f * sphereSize.x(), sphereCenter.y() + 0.251149f * sphereSize.y(), sphereCenter.z() + -0.147618f * sphereSize.z(), 0.386364f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.155215f * sphereSize.x(), sphereCenter.y() + 0.251152f * sphereSize.y(), sphereCenter.z() + 0.955422f * sphereSize.z(), 0.431819f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + 0.0f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + 1.0f * sphereSize.z(), 0.477273f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + -0.155215f * sphereSize.x(), sphereCenter.y() + -0.251152f * sphereSize.y(), sphereCenter.z() + 0.955422f * sphereSize.z(), 0.454546f, 0.472382f));
            vertices.add(new Vertex(sphereCenter.x() + 0.687159f * sphereSize.x(), sphereCenter.y() + -0.251152f * sphereSize.y(), sphereCenter.z() + 0.681715f * sphereSize.z(), 0.409091f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.587786f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + 0.809017f * sphereSize.z(), 0.454546f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.436007f * sphereSize.x(), sphereCenter.y() + 0.251152f * sphereSize.y(), sphereCenter.z() + 0.864188f * sphereSize.z(), 0.431819f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.860698f * sphereSize.x(), sphereCenter.y() + 0.251151f * sphereSize.y(), sphereCenter.z() + 0.442858f * sphereSize.z(), 0.5f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + -0.951058f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + 0.309013f * sphereSize.z(), 0.477273f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.956626f * sphereSize.x(), sphereCenter.y() + -0.251149f * sphereSize.y(), sphereCenter.z() + 0.147618f * sphereSize.z(), 0.522728f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.436007f * sphereSize.x(), sphereCenter.y() + -0.251152f * sphereSize.y(), sphereCenter.z() + 0.864188f * sphereSize.z(), 0.454546f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.587786f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + 0.809017f * sphereSize.z(), 0.5f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.687159f * sphereSize.x(), sphereCenter.y() + 0.251152f * sphereSize.y(), sphereCenter.z() + 0.681715f * sphereSize.z(), 0.613637f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + -0.687159f * sphereSize.x(), sphereCenter.y() + 0.251152f * sphereSize.y(), sphereCenter.z() + -0.681715f * sphereSize.z(), 0.659091f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + -0.587786f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + -0.809017f * sphereSize.z(), 0.636364f, 0.472382f));
            vertices.add(new Vertex(sphereCenter.x() + -0.436007f * sphereSize.x(), sphereCenter.y() + -0.251152f * sphereSize.y(), sphereCenter.z() + -0.864188f * sphereSize.z(), 0.590909f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + -0.956626f * sphereSize.x(), sphereCenter.y() + -0.251149f * sphereSize.y(), sphereCenter.z() + -0.147618f * sphereSize.z(), 0.636364f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + -0.951058f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + -0.309013f * sphereSize.z(), 0.613637f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.860698f * sphereSize.x(), sphereCenter.y() + 0.251151f * sphereSize.y(), sphereCenter.z() + -0.442858f * sphereSize.z(), 0.681818f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.436007f * sphereSize.x(), sphereCenter.y() + 0.251152f * sphereSize.y(), sphereCenter.z() + -0.864188f * sphereSize.z(), 0.659091f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.587786f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + -0.809017f * sphereSize.z(), 0.704546f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.687159f * sphereSize.x(), sphereCenter.y() + -0.251152f * sphereSize.y(), sphereCenter.z() + -0.681715f * sphereSize.z(), 0.636364f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.155215f * sphereSize.x(), sphereCenter.y() + -0.251152f * sphereSize.y(), sphereCenter.z() + -0.955422f * sphereSize.z(), 0.681818f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.0f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + -1.0f * sphereSize.z(), 0.795455f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + 0.155215f * sphereSize.x(), sphereCenter.y() + 0.251152f * sphereSize.y(), sphereCenter.z() + -0.955422f * sphereSize.z(), 0.840909f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + 0.831051f * sphereSize.x(), sphereCenter.y() + 0.502299f * sphereSize.y(), sphereCenter.z() + 0.238853f * sphereSize.z(), 0.818182f, 0.472382f));
            vertices.add(new Vertex(sphereCenter.x() + 0.688189f * sphereSize.x(), sphereCenter.y() + 0.525736f * sphereSize.y(), sphereCenter.z() + 0.499997f * sphereSize.z(), 0.772727f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.483971f * sphereSize.x(), sphereCenter.y() + 0.502302f * sphereSize.y(), sphereCenter.z() + 0.716565f * sphereSize.z(), 0.818182f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.029639f * sphereSize.x(), sphereCenter.y() + 0.502302f * sphereSize.y(), sphereCenter.z() + 0.864184f * sphereSize.z(), 0.795455f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.262869f * sphereSize.x(), sphereCenter.y() + 0.525738f * sphereSize.y(), sphereCenter.z() + 0.809012f * sphereSize.z(), 0.863636f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + -0.531941f * sphereSize.x(), sphereCenter.y() + 0.502302f * sphereSize.y(), sphereCenter.z() + 0.681712f * sphereSize.z(), 0.840909f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.812729f * sphereSize.x(), sphereCenter.y() + 0.502301f * sphereSize.y(), sphereCenter.z() + 0.295238f * sphereSize.z(), 0.886364f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + -0.850648f * sphereSize.x(), sphereCenter.y() + 0.525736f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.818182f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.812729f * sphereSize.x(), sphereCenter.y() + 0.502301f * sphereSize.y(), sphereCenter.z() + -0.295238f * sphereSize.z(), 0.863636f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.531941f * sphereSize.x(), sphereCenter.y() + 0.502302f * sphereSize.y(), sphereCenter.z() + -0.681712f * sphereSize.z(), 0.909091f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.262869f * sphereSize.x(), sphereCenter.y() + 0.525738f * sphereSize.y(), sphereCenter.z() + -0.809012f * sphereSize.z(), 0.068182f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + 0.029639f * sphereSize.x(), sphereCenter.y() + 0.502302f * sphereSize.y(), sphereCenter.z() + -0.864184f * sphereSize.z(), 0.113637f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + 0.483971f * sphereSize.x(), sphereCenter.y() + 0.502302f * sphereSize.y(), sphereCenter.z() + -0.716565f * sphereSize.z(), 0.09091f, 0.472382f));
            vertices.add(new Vertex(sphereCenter.x() + 0.688189f * sphereSize.x(), sphereCenter.y() + 0.525736f * sphereSize.y(), sphereCenter.z() + -0.499997f * sphereSize.z(), 0.045455f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.831051f * sphereSize.x(), sphereCenter.y() + 0.502299f * sphereSize.y(), sphereCenter.z() + -0.238853f * sphereSize.z(), 0.09091f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.077607f * sphereSize.x(), sphereCenter.y() + 0.96795f * sphereSize.y(), sphereCenter.z() + 0.238853f * sphereSize.z(), 0.068182f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.162456f * sphereSize.x(), sphereCenter.y() + 0.850654f * sphereSize.y(), sphereCenter.z() + 0.499995f * sphereSize.z(), 0.136365f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.232822f * sphereSize.x(), sphereCenter.y() + 0.657519f * sphereSize.y(), sphereCenter.z() + 0.716563f * sphereSize.z(), 0.113637f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.753442f * sphereSize.x(), sphereCenter.y() + 0.657515f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.159092f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.52573f * sphereSize.x(), sphereCenter.y() + 0.850652f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.090909f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.251147f * sphereSize.x(), sphereCenter.y() + 0.967949f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.136364f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + -0.203181f * sphereSize.x(), sphereCenter.y() + 0.96795f * sphereSize.y(), sphereCenter.z() + 0.147618f * sphereSize.z(), 0.250001f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + -0.425323f * sphereSize.x(), sphereCenter.y() + 0.850654f * sphereSize.y(), sphereCenter.z() + 0.309011f * sphereSize.z(), 0.295455f, 0.433017f));
            vertices.add(new Vertex(sphereCenter.x() + -0.609547f * sphereSize.x(), sphereCenter.y() + 0.657519f * sphereSize.y(), sphereCenter.z() + 0.442856f * sphereSize.z(), 0.272728f, 0.472382f));
            vertices.add(new Vertex(sphereCenter.x() + -0.203181f * sphereSize.x(), sphereCenter.y() + 0.96795f * sphereSize.y(), sphereCenter.z() + -0.147618f * sphereSize.z(), 0.227273f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + -0.425323f * sphereSize.x(), sphereCenter.y() + 0.850654f * sphereSize.y(), sphereCenter.z() + -0.309011f * sphereSize.z(), 0.272728f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + -0.609547f * sphereSize.x(), sphereCenter.y() + 0.657519f * sphereSize.y(), sphereCenter.z() + -0.442856f * sphereSize.z(), 0.250001f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.077607f * sphereSize.x(), sphereCenter.y() + 0.96795f * sphereSize.y(), sphereCenter.z() + -0.238853f * sphereSize.z(), 0.318182f, 0.393651f));
            vertices.add(new Vertex(sphereCenter.x() + 0.162456f * sphereSize.x(), sphereCenter.y() + 0.850654f * sphereSize.y(), sphereCenter.z() + -0.499995f * sphereSize.z(), 0.295455f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.232822f * sphereSize.x(), sphereCenter.y() + 0.657519f * sphereSize.y(), sphereCenter.z() + -0.716563f * sphereSize.z(), 0.34091f, 0.354286f));
            vertices.add(new Vertex(sphereCenter.x() + 0.3618f * sphereSize.x(), sphereCenter.y() + 0.894429f * sphereSize.y(), sphereCenter.z() + -0.262863f * sphereSize.z(), 0.272728f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.638194f * sphereSize.x(), sphereCenter.y() + 0.72361f * sphereSize.y(), sphereCenter.z() + -0.262864f * sphereSize.z(), 0.318182f, 0.314921f));
            vertices.add(new Vertex(sphereCenter.x() + 0.447209f * sphereSize.x(), sphereCenter.y() + 0.723612f * sphereSize.y(), sphereCenter.z() + -0.525728f * sphereSize.z(), 0.386364f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.138197f * sphereSize.x(), sphereCenter.y() + 0.89443f * sphereSize.y(), sphereCenter.z() + -0.425319f * sphereSize.z(), 0.409091f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.05279f * sphereSize.x(), sphereCenter.y() + 0.723612f * sphereSize.y(), sphereCenter.z() + -0.688185f * sphereSize.z(), 0.431819f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.361804f * sphereSize.x(), sphereCenter.y() + 0.723612f * sphereSize.y(), sphereCenter.z() + -0.587778f * sphereSize.z(), 0.454546f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.44721f * sphereSize.x(), sphereCenter.y() + 0.894429f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.477273f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.670817f * sphereSize.x(), sphereCenter.y() + 0.723611f * sphereSize.y(), sphereCenter.z() + -0.162457f * sphereSize.z(), 0.5f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.670817f * sphereSize.x(), sphereCenter.y() + 0.723611f * sphereSize.y(), sphereCenter.z() + 0.162457f * sphereSize.z(), 0.522728f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.138197f * sphereSize.x(), sphereCenter.y() + 0.89443f * sphereSize.y(), sphereCenter.z() + 0.425319f * sphereSize.z(), 0.568182f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.361804f * sphereSize.x(), sphereCenter.y() + 0.723612f * sphereSize.y(), sphereCenter.z() + 0.587778f * sphereSize.z(), 0.590909f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.05279f * sphereSize.x(), sphereCenter.y() + 0.723612f * sphereSize.y(), sphereCenter.z() + 0.688185f * sphereSize.z(), 0.613637f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.3618f * sphereSize.x(), sphereCenter.y() + 0.894429f * sphereSize.y(), sphereCenter.z() + 0.262863f * sphereSize.z(), 0.636364f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.447209f * sphereSize.x(), sphereCenter.y() + 0.723612f * sphereSize.y(), sphereCenter.z() + 0.525728f * sphereSize.z(), 0.659091f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.638194f * sphereSize.x(), sphereCenter.y() + 0.72361f * sphereSize.y(), sphereCenter.z() + 0.262864f * sphereSize.z(), 0.681818f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.861804f * sphereSize.x(), sphereCenter.y() + 0.276396f * sphereSize.y(), sphereCenter.z() + -0.425322f * sphereSize.z(), 0.704546f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.809019f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + -0.587782f * sphereSize.z(), 0.75f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.670821f * sphereSize.x(), sphereCenter.y() + 0.276397f * sphereSize.y(), sphereCenter.z() + -0.688189f * sphereSize.z(), 0.772727f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.138199f * sphereSize.x(), sphereCenter.y() + 0.276397f * sphereSize.y(), sphereCenter.z() + -0.951055f * sphereSize.z(), 0.795455f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.309016f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + -0.951057f * sphereSize.z(), 0.818182f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.447215f * sphereSize.x(), sphereCenter.y() + 0.276397f * sphereSize.y(), sphereCenter.z() + -0.850649f * sphereSize.z(), 0.840909f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.947213f * sphereSize.x(), sphereCenter.y() + 0.276396f * sphereSize.y(), sphereCenter.z() + -0.162458f * sphereSize.z(), 0.863636f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -1.0f * sphereSize.x(), sphereCenter.y() + 1e-06f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.886364f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.947213f * sphereSize.x(), sphereCenter.y() + 0.276397f * sphereSize.y(), sphereCenter.z() + 0.162458f * sphereSize.z(), 0.022727f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.447216f * sphereSize.x(), sphereCenter.y() + 0.276397f * sphereSize.y(), sphereCenter.z() + 0.850648f * sphereSize.z(), 0.045455f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.309017f * sphereSize.x(), sphereCenter.y() + -1e-06f * sphereSize.y(), sphereCenter.z() + 0.951056f * sphereSize.z(), 0.068182f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.138199f * sphereSize.x(), sphereCenter.y() + 0.276397f * sphereSize.y(), sphereCenter.z() + 0.951055f * sphereSize.z(), 0.09091f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.67082f * sphereSize.x(), sphereCenter.y() + 0.276396f * sphereSize.y(), sphereCenter.z() + 0.68819f * sphereSize.z(), 0.113637f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.809019f * sphereSize.x(), sphereCenter.y() + -2e-06f * sphereSize.y(), sphereCenter.z() + 0.587783f * sphereSize.z(), 0.136364f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.861804f * sphereSize.x(), sphereCenter.y() + 0.276394f * sphereSize.y(), sphereCenter.z() + 0.425323f * sphereSize.z(), 0.159092f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.309017f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + -0.951056f * sphereSize.z(), 0.204546f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + 0.447216f * sphereSize.x(), sphereCenter.y() + -0.276398f * sphereSize.y(), sphereCenter.z() + -0.850648f * sphereSize.z(), 0.227273f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.138199f * sphereSize.x(), sphereCenter.y() + -0.276398f * sphereSize.y(), sphereCenter.z() + -0.951055f * sphereSize.z(), 0.250001f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.809018f * sphereSize.x(), sphereCenter.y() + -0.0f * sphereSize.y(), sphereCenter.z() + -0.587783f * sphereSize.z(), 0.272728f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.670819f * sphereSize.x(), sphereCenter.y() + -0.276397f * sphereSize.y(), sphereCenter.z() + -0.688191f * sphereSize.z(), 0.295455f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.861803f * sphereSize.x(), sphereCenter.y() + -0.276396f * sphereSize.y(), sphereCenter.z() + -0.425324f * sphereSize.z(), 0.318182f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.809018f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + 0.587783f * sphereSize.z(), 0.34091f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.861803f * sphereSize.x(), sphereCenter.y() + -0.276396f * sphereSize.y(), sphereCenter.z() + 0.425324f * sphereSize.z(), 0.545455f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.670819f * sphereSize.x(), sphereCenter.y() + -0.276397f * sphereSize.y(), sphereCenter.z() + 0.688191f * sphereSize.z(), 0.522728f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.309017f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + 0.951056f * sphereSize.z(), 0.568182f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.138199f * sphereSize.x(), sphereCenter.y() + -0.276398f * sphereSize.y(), sphereCenter.z() + 0.951055f * sphereSize.z(), 0.545455f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.447216f * sphereSize.x(), sphereCenter.y() + -0.276398f * sphereSize.y(), sphereCenter.z() + 0.850648f * sphereSize.z(), 0.590909f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 1.0f * sphereSize.x(), sphereCenter.y() + 0.0f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.727273f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.947213f * sphereSize.x(), sphereCenter.y() + -0.276396f * sphereSize.y(), sphereCenter.z() + 0.162458f * sphereSize.z(), 0.704546f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.947213f * sphereSize.x(), sphereCenter.y() + -0.276396f * sphereSize.y(), sphereCenter.z() + -0.162458f * sphereSize.z(), 0.75f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.361803f * sphereSize.x(), sphereCenter.y() + -0.723612f * sphereSize.y(), sphereCenter.z() + -0.587779f * sphereSize.z(), 0.727273f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.138197f * sphereSize.x(), sphereCenter.y() + -0.894429f * sphereSize.y(), sphereCenter.z() + -0.425321f * sphereSize.z(), 0.772727f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.052789f * sphereSize.x(), sphereCenter.y() + -0.723611f * sphereSize.y(), sphereCenter.z() + -0.688186f * sphereSize.z(), 0.931818f, 0.275556f));
            vertices.add(new Vertex(sphereCenter.x() + -0.447211f * sphereSize.x(), sphereCenter.y() + -0.723612f * sphereSize.y(), sphereCenter.z() + -0.525727f * sphereSize.z(), 0.909091f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.361801f * sphereSize.x(), sphereCenter.y() + -0.894429f * sphereSize.y(), sphereCenter.z() + -0.262863f * sphereSize.z(), 0.886364f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.638195f * sphereSize.x(), sphereCenter.y() + -0.723609f * sphereSize.y(), sphereCenter.z() + -0.262863f * sphereSize.z(), 0.954545f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + -0.638195f * sphereSize.x(), sphereCenter.y() + -0.723609f * sphereSize.y(), sphereCenter.z() + 0.262864f * sphereSize.z(), 0.931818f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.361801f * sphereSize.x(), sphereCenter.y() + -0.894428f * sphereSize.y(), sphereCenter.z() + 0.262864f * sphereSize.z(), 0.977273f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + -0.447211f * sphereSize.x(), sphereCenter.y() + -0.72361f * sphereSize.y(), sphereCenter.z() + 0.525729f * sphereSize.z(), 0.909091f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.670817f * sphereSize.x(), sphereCenter.y() + -0.723611f * sphereSize.y(), sphereCenter.z() + -0.162457f * sphereSize.z(), 0.954545f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.670818f * sphereSize.x(), sphereCenter.y() + -0.72361f * sphereSize.y(), sphereCenter.z() + 0.162458f * sphereSize.z(), 1.0f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.447211f * sphereSize.x(), sphereCenter.y() + -0.894428f * sphereSize.y(), sphereCenter.z() + 1e-06f * sphereSize.z(), 0.181819f, 0.236191f));
            vertices.add(new Vertex(sphereCenter.x() + 0.05279f * sphereSize.x(), sphereCenter.y() + -0.723612f * sphereSize.y(), sphereCenter.z() + 0.688185f * sphereSize.z(), 0.159092f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.138199f * sphereSize.x(), sphereCenter.y() + -0.894429f * sphereSize.y(), sphereCenter.z() + 0.425321f * sphereSize.z(), 0.204546f, 0.196826f));
            vertices.add(new Vertex(sphereCenter.x() + 0.361805f * sphereSize.x(), sphereCenter.y() + -0.723611f * sphereSize.y(), sphereCenter.z() + 0.587779f * sphereSize.z(), 0.181819f, 0.157461f));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(15), vertices.get(14)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(1), vertices.get(17), vertices.get(23)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(14), vertices.get(29)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(29), vertices.get(35)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(35), vertices.get(24)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(1), vertices.get(23), vertices.get(44)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(2), vertices.get(20), vertices.get(50)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(3), vertices.get(32), vertices.get(56)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(4), vertices.get(38), vertices.get(62)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(5), vertices.get(41), vertices.get(68)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(1), vertices.get(44), vertices.get(51)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(2), vertices.get(50), vertices.get(57)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(3), vertices.get(56), vertices.get(63)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(4), vertices.get(62), vertices.get(69)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(5), vertices.get(68), vertices.get(45)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(6), vertices.get(74), vertices.get(89)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(7), vertices.get(77), vertices.get(95)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(8), vertices.get(80), vertices.get(98)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(9), vertices.get(83), vertices.get(101)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(10), vertices.get(86), vertices.get(90)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(92), vertices.get(99), vertices.get(11)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(91), vertices.get(102), vertices.get(92)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(90), vertices.get(103), vertices.get(91)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(92), vertices.get(102), vertices.get(99)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(102), vertices.get(100), vertices.get(99)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(91), vertices.get(103), vertices.get(102)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(103), vertices.get(104), vertices.get(102)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(102), vertices.get(104), vertices.get(100)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(104), vertices.get(101), vertices.get(100)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(90), vertices.get(86), vertices.get(103)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(86), vertices.get(85), vertices.get(103)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(103), vertices.get(85), vertices.get(104)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(85), vertices.get(84), vertices.get(104)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(104), vertices.get(84), vertices.get(101)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(84), vertices.get(9), vertices.get(101)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(99), vertices.get(96), vertices.get(11)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(100), vertices.get(105), vertices.get(99)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(101), vertices.get(106), vertices.get(100)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(99), vertices.get(105), vertices.get(96)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(105), vertices.get(97), vertices.get(96)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(100), vertices.get(106), vertices.get(105)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(106), vertices.get(107), vertices.get(105)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(105), vertices.get(107), vertices.get(97)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(107), vertices.get(98), vertices.get(97)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(101), vertices.get(83), vertices.get(106)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(83), vertices.get(82), vertices.get(106)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(106), vertices.get(82), vertices.get(107)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(82), vertices.get(81), vertices.get(107)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(107), vertices.get(81), vertices.get(98)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(81), vertices.get(8), vertices.get(98)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(96), vertices.get(93), vertices.get(11)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(97), vertices.get(108), vertices.get(96)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(98), vertices.get(109), vertices.get(97)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(96), vertices.get(108), vertices.get(93)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(108), vertices.get(94), vertices.get(93)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(97), vertices.get(109), vertices.get(108)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(109), vertices.get(110), vertices.get(108)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(108), vertices.get(110), vertices.get(94)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(110), vertices.get(95), vertices.get(94)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(98), vertices.get(80), vertices.get(109)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(80), vertices.get(79), vertices.get(109)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(109), vertices.get(79), vertices.get(110)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(79), vertices.get(78), vertices.get(110)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(110), vertices.get(78), vertices.get(95)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(78), vertices.get(7), vertices.get(95)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(93), vertices.get(87), vertices.get(11)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(94), vertices.get(111), vertices.get(93)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(95), vertices.get(112), vertices.get(94)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(93), vertices.get(111), vertices.get(87)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(111), vertices.get(88), vertices.get(87)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(94), vertices.get(112), vertices.get(111)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(112), vertices.get(113), vertices.get(111)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(111), vertices.get(113), vertices.get(88)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(113), vertices.get(89), vertices.get(88)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(95), vertices.get(77), vertices.get(112)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(77), vertices.get(76), vertices.get(112)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(112), vertices.get(76), vertices.get(113)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(76), vertices.get(75), vertices.get(113)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(113), vertices.get(75), vertices.get(89)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(75), vertices.get(6), vertices.get(89)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(87), vertices.get(92), vertices.get(11)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(88), vertices.get(114), vertices.get(87)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(89), vertices.get(115), vertices.get(88)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(87), vertices.get(114), vertices.get(92)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(114), vertices.get(91), vertices.get(92)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(88), vertices.get(115), vertices.get(114)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(115), vertices.get(116), vertices.get(114)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(114), vertices.get(116), vertices.get(91)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(116), vertices.get(90), vertices.get(91)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(89), vertices.get(74), vertices.get(115)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(74), vertices.get(73), vertices.get(115)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(115), vertices.get(73), vertices.get(116)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(73), vertices.get(72), vertices.get(116)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(116), vertices.get(72), vertices.get(90)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(72), vertices.get(10), vertices.get(90)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(47), vertices.get(86), vertices.get(10)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(46), vertices.get(117), vertices.get(47)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(45), vertices.get(118), vertices.get(46)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(47), vertices.get(117), vertices.get(86)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(117), vertices.get(85), vertices.get(86)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(46), vertices.get(118), vertices.get(117)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(118), vertices.get(119), vertices.get(117)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(117), vertices.get(119), vertices.get(85)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(119), vertices.get(84), vertices.get(85)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(45), vertices.get(68), vertices.get(118)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(68), vertices.get(67), vertices.get(118)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(118), vertices.get(67), vertices.get(119)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(67), vertices.get(66), vertices.get(119)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(119), vertices.get(66), vertices.get(84)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(66), vertices.get(9), vertices.get(84)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(71), vertices.get(83), vertices.get(9)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(70), vertices.get(120), vertices.get(71)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(69), vertices.get(121), vertices.get(70)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(71), vertices.get(120), vertices.get(83)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(120), vertices.get(82), vertices.get(83)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(70), vertices.get(121), vertices.get(120)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(121), vertices.get(122), vertices.get(120)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(120), vertices.get(122), vertices.get(82)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(122), vertices.get(81), vertices.get(82)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(69), vertices.get(62), vertices.get(121)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(62), vertices.get(61), vertices.get(121)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(121), vertices.get(61), vertices.get(122)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(61), vertices.get(60), vertices.get(122)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(122), vertices.get(60), vertices.get(81)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(60), vertices.get(8), vertices.get(81)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(65), vertices.get(80), vertices.get(8)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(64), vertices.get(123), vertices.get(65)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(63), vertices.get(124), vertices.get(64)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(65), vertices.get(123), vertices.get(80)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(123), vertices.get(79), vertices.get(80)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(64), vertices.get(124), vertices.get(123)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(124), vertices.get(125), vertices.get(123)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(123), vertices.get(125), vertices.get(79)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(125), vertices.get(78), vertices.get(79)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(63), vertices.get(56), vertices.get(124)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(56), vertices.get(55), vertices.get(124)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(124), vertices.get(55), vertices.get(125)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(55), vertices.get(54), vertices.get(125)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(125), vertices.get(54), vertices.get(78)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(54), vertices.get(7), vertices.get(78)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(59), vertices.get(77), vertices.get(7)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(58), vertices.get(126), vertices.get(59)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(57), vertices.get(127), vertices.get(58)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(59), vertices.get(126), vertices.get(77)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(126), vertices.get(76), vertices.get(77)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(58), vertices.get(127), vertices.get(126)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(127), vertices.get(128), vertices.get(126)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(126), vertices.get(128), vertices.get(76)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(128), vertices.get(75), vertices.get(76)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(57), vertices.get(50), vertices.get(127)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(50), vertices.get(49), vertices.get(127)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(127), vertices.get(49), vertices.get(128)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(49), vertices.get(48), vertices.get(128)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(128), vertices.get(48), vertices.get(75)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(48), vertices.get(6), vertices.get(75)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(53), vertices.get(74), vertices.get(6)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(52), vertices.get(129), vertices.get(53)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(51), vertices.get(130), vertices.get(52)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(53), vertices.get(129), vertices.get(74)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(129), vertices.get(73), vertices.get(74)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(52), vertices.get(130), vertices.get(129)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(130), vertices.get(131), vertices.get(129)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(129), vertices.get(131), vertices.get(73)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(131), vertices.get(72), vertices.get(73)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(51), vertices.get(44), vertices.get(130)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(44), vertices.get(43), vertices.get(130)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(130), vertices.get(43), vertices.get(131)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(43), vertices.get(42), vertices.get(131)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(131), vertices.get(42), vertices.get(72)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(42), vertices.get(10), vertices.get(72)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(66), vertices.get(71), vertices.get(9)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(67), vertices.get(132), vertices.get(66)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(68), vertices.get(133), vertices.get(67)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(66), vertices.get(132), vertices.get(71)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(132), vertices.get(70), vertices.get(71)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(67), vertices.get(133), vertices.get(132)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(133), vertices.get(134), vertices.get(132)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(132), vertices.get(134), vertices.get(70)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(134), vertices.get(69), vertices.get(70)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(68), vertices.get(41), vertices.get(133)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(41), vertices.get(40), vertices.get(133)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(133), vertices.get(40), vertices.get(134)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(40), vertices.get(39), vertices.get(134)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(134), vertices.get(39), vertices.get(69)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(39), vertices.get(4), vertices.get(69)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(60), vertices.get(65), vertices.get(8)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(61), vertices.get(135), vertices.get(60)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(62), vertices.get(136), vertices.get(61)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(60), vertices.get(135), vertices.get(65)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(135), vertices.get(64), vertices.get(65)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(61), vertices.get(136), vertices.get(135)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(136), vertices.get(137), vertices.get(135)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(135), vertices.get(137), vertices.get(64)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(137), vertices.get(63), vertices.get(64)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(62), vertices.get(38), vertices.get(136)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(38), vertices.get(37), vertices.get(136)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(136), vertices.get(37), vertices.get(137)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(37), vertices.get(36), vertices.get(137)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(137), vertices.get(36), vertices.get(63)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(36), vertices.get(3), vertices.get(63)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(54), vertices.get(59), vertices.get(7)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(55), vertices.get(138), vertices.get(54)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(56), vertices.get(139), vertices.get(55)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(54), vertices.get(138), vertices.get(59)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(138), vertices.get(58), vertices.get(59)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(55), vertices.get(139), vertices.get(138)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(139), vertices.get(140), vertices.get(138)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(138), vertices.get(140), vertices.get(58)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(140), vertices.get(57), vertices.get(58)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(56), vertices.get(32), vertices.get(139)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(32), vertices.get(31), vertices.get(139)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(139), vertices.get(31), vertices.get(140)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(31), vertices.get(30), vertices.get(140)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(140), vertices.get(30), vertices.get(57)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(30), vertices.get(2), vertices.get(57)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(48), vertices.get(53), vertices.get(6)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(49), vertices.get(141), vertices.get(48)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(50), vertices.get(142), vertices.get(49)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(48), vertices.get(141), vertices.get(53)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(141), vertices.get(52), vertices.get(53)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(49), vertices.get(142), vertices.get(141)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(142), vertices.get(143), vertices.get(141)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(141), vertices.get(143), vertices.get(52)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(143), vertices.get(51), vertices.get(52)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(50), vertices.get(20), vertices.get(142)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(20), vertices.get(19), vertices.get(142)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(142), vertices.get(19), vertices.get(143)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(19), vertices.get(18), vertices.get(143)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(143), vertices.get(18), vertices.get(51)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(18), vertices.get(1), vertices.get(51)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(42), vertices.get(47), vertices.get(10)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(43), vertices.get(144), vertices.get(42)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(44), vertices.get(145), vertices.get(43)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(42), vertices.get(144), vertices.get(47)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(144), vertices.get(46), vertices.get(47)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(43), vertices.get(145), vertices.get(144)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(145), vertices.get(146), vertices.get(144)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(144), vertices.get(146), vertices.get(46)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(146), vertices.get(45), vertices.get(46)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(44), vertices.get(23), vertices.get(145)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(23), vertices.get(22), vertices.get(145)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(145), vertices.get(22), vertices.get(146)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(22), vertices.get(21), vertices.get(146)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(146), vertices.get(21), vertices.get(45)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(21), vertices.get(5), vertices.get(45)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(26), vertices.get(41), vertices.get(5)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(25), vertices.get(147), vertices.get(26)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(24), vertices.get(148), vertices.get(25)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(26), vertices.get(147), vertices.get(41)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(147), vertices.get(40), vertices.get(41)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(25), vertices.get(148), vertices.get(147)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(148), vertices.get(149), vertices.get(147)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(147), vertices.get(149), vertices.get(40)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(149), vertices.get(39), vertices.get(40)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(24), vertices.get(35), vertices.get(148)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(35), vertices.get(34), vertices.get(148)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(148), vertices.get(34), vertices.get(149)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(34), vertices.get(33), vertices.get(149)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(149), vertices.get(33), vertices.get(39)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(33), vertices.get(4), vertices.get(39)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(33), vertices.get(38), vertices.get(4)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(34), vertices.get(150), vertices.get(33)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(35), vertices.get(151), vertices.get(34)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(33), vertices.get(150), vertices.get(38)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(150), vertices.get(37), vertices.get(38)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(34), vertices.get(151), vertices.get(150)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(151), vertices.get(152), vertices.get(150)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(150), vertices.get(152), vertices.get(37)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(152), vertices.get(36), vertices.get(37)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(35), vertices.get(29), vertices.get(151)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(29), vertices.get(28), vertices.get(151)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(151), vertices.get(28), vertices.get(152)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(28), vertices.get(27), vertices.get(152)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(152), vertices.get(27), vertices.get(36)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(27), vertices.get(3), vertices.get(36)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(27), vertices.get(32), vertices.get(3)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(28), vertices.get(153), vertices.get(27)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(29), vertices.get(154), vertices.get(28)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(27), vertices.get(153), vertices.get(32)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(153), vertices.get(31), vertices.get(32)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(28), vertices.get(154), vertices.get(153)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(154), vertices.get(155), vertices.get(153)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(153), vertices.get(155), vertices.get(31)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(155), vertices.get(30), vertices.get(31)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(29), vertices.get(14), vertices.get(154)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(14), vertices.get(13), vertices.get(154)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(154), vertices.get(13), vertices.get(155)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(13), vertices.get(12), vertices.get(155)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(155), vertices.get(12), vertices.get(30)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(12), vertices.get(2), vertices.get(30)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(21), vertices.get(26), vertices.get(5)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(22), vertices.get(156), vertices.get(21)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(23), vertices.get(157), vertices.get(22)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(21), vertices.get(156), vertices.get(26)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(156), vertices.get(25), vertices.get(26)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(22), vertices.get(157), vertices.get(156)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(157), vertices.get(158), vertices.get(156)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(156), vertices.get(158), vertices.get(25)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(158), vertices.get(24), vertices.get(25)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(23), vertices.get(17), vertices.get(157)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(17), vertices.get(16), vertices.get(157)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(157), vertices.get(16), vertices.get(158)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(16), vertices.get(15), vertices.get(158)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(158), vertices.get(15), vertices.get(24)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(15), vertices.get(0), vertices.get(24)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(12), vertices.get(20), vertices.get(2)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(13), vertices.get(159), vertices.get(12)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(14), vertices.get(160), vertices.get(13)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(12), vertices.get(159), vertices.get(20)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(159), vertices.get(19), vertices.get(20)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(13), vertices.get(160), vertices.get(159)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(160), vertices.get(161), vertices.get(159)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(159), vertices.get(161), vertices.get(19)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(161), vertices.get(18), vertices.get(19)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(14), vertices.get(15), vertices.get(160)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(15), vertices.get(16), vertices.get(160)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(160), vertices.get(16), vertices.get(161)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(16), vertices.get(17), vertices.get(161)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(161), vertices.get(17), vertices.get(18)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(17), vertices.get(1), vertices.get(18)}, false, this.sphereCenter));
        }
    }

    public static class Polygon {
        protected List<Vertex> vertices = new ArrayList<>();
        public final Vector3f normal;

        public Polygon(Vertex[] vertices, boolean p_i225951_8_, Vector3f center) {
            if (p_i225951_8_) {
                int i = vertices.length;

                for(int j = 0; j < i / 2; ++j) {
                    Vertex vertex = vertices[j];
                    vertices[j] = vertices[i - 1 - j];
                    vertices[i - 1 - j] = vertex;
                }
            }

            float x = 0;
            float y = 0;
            float z = 0;
            for (Vertex vertex : vertices) {
                x += vertex.pos.x();
                y += vertex.pos.y();
                z += vertex.pos.z();
            }

            Vector3f pos = new Vector3f(x / vertices.length, y / vertices.length, z / vertices.length);
            center.sub(pos);


            this.normal = center;
            if (p_i225951_8_) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }
            this.vertices.addAll(Arrays.asList(vertices));
            if (vertices.length % 2 == 1) {
                this.vertices.add(vertices[vertices.length - 1]);
            }
        }
    }

    public static class Vertex {
        public final Vector3f pos;
        public final float u;
        public final float v;
        
        public Vertex(float x, float y, float z, float u, float v) {
            this(new Vector3f(x, y, z), u, v);
        }

        public Vertex(Vector3f pos, float u, float v) {
            this.pos = pos;
            this.u = u;
            this.v = v;
        }

        public Vertex remap(float p_78240_1_, float p_78240_2_) {
            return new Vertex(this.pos, p_78240_1_, p_78240_2_);
        }
    }
}
