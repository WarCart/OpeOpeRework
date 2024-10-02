package net.warcar.ope_ope_rework.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;
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
            vertices.add(new Vertex(sphereCenter.x() + 0.7236f * sphereSize.x(), sphereCenter.y() + -0.447215f * sphereSize.y(), sphereCenter.z() + 0.52572f * sphereSize.z(), 0.272728f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.276385f * sphereSize.x(), sphereCenter.y() + -0.447215f * sphereSize.y(), sphereCenter.z() + 0.85064f * sphereSize.z(), 0.09091f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.894425f * sphereSize.x(), sphereCenter.y() + -0.447215f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.363637f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + -0.276385f * sphereSize.x(), sphereCenter.y() + -0.447215f * sphereSize.y(), sphereCenter.z() + -0.85064f * sphereSize.z(), 0.454546f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.7236f * sphereSize.x(), sphereCenter.y() + -0.447215f * sphereSize.y(), sphereCenter.z() + -0.52572f * sphereSize.z(), 0.909091f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + 0.276385f * sphereSize.x(), sphereCenter.y() + 0.447215f * sphereSize.y(), sphereCenter.z() + 0.85064f * sphereSize.z(), 1.0f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.7236f * sphereSize.x(), sphereCenter.y() + 0.447215f * sphereSize.y(), sphereCenter.z() + 0.52572f * sphereSize.z(), 0.818182f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + -0.7236f * sphereSize.x(), sphereCenter.y() + 0.447215f * sphereSize.y(), sphereCenter.z() + -0.52572f * sphereSize.z(), 0.727273f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + 0.276385f * sphereSize.x(), sphereCenter.y() + 0.447215f * sphereSize.y(), sphereCenter.z() + -0.85064f * sphereSize.z(), 0.636364f, 0.157461f));
            vertices.add(new Vertex(sphereCenter.x() + 0.894425f * sphereSize.x(), sphereCenter.y() + 0.447215f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.545455f, 0.0f));
            vertices.add(new Vertex(sphereCenter.x() + 0.0f * sphereSize.x(), sphereCenter.y() + 1.0f * sphereSize.y(), sphereCenter.z() + 0.0f * sphereSize.z(), 0.363637f, 0.314921f));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(1), vertices.get(2)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(2), vertices.get(3)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(3), vertices.get(4)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(4), vertices.get(5)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(0), vertices.get(5), vertices.get(1)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(11), vertices.get(10), vertices.get(9)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(11), vertices.get(9), vertices.get(8)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(11), vertices.get(8), vertices.get(7)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(11), vertices.get(7), vertices.get(6)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(11), vertices.get(6), vertices.get(10)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(1), vertices.get(2), vertices.get(6)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(2), vertices.get(3), vertices.get(7)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(3), vertices.get(4), vertices.get(8)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(4), vertices.get(5), vertices.get(9)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(5), vertices.get(1), vertices.get(10)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(1), vertices.get(6), vertices.get(10)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(2), vertices.get(7), vertices.get(6)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(3), vertices.get(8), vertices.get(7)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(4), vertices.get(9), vertices.get(8)}, false, this.sphereCenter));
            this.polygons.add(new Polygon(new Vertex[]{vertices.get(5), vertices.get(10), vertices.get(9)}, false, this.sphereCenter));
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
