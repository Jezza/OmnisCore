package me.jezza.oc.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class BlockRenderer {
    public static final float OFFSET_1 = 0.0625F;
    public static final float OFFSET_2 = 0.125F;
    public static final float OFFSET_3 = 0.1875F;
    public static final float OFFSET_4 = 0.25F;
    public static final float OFFSET_5 = 0.3125F;
    public static final float OFFSET_6 = 0.375F;
    public static final float OFFSET_7 = 0.4375F;
    public static final float OFFSET_8 = 0.5F;
    public static final float OFFSET_9 = 0.5625F;
    public static final float OFFSET_10 = 0.625F;
    public static final float OFFSET_11 = 0.6875F;
    public static final float OFFSET_12 = 0.75F;
    public static final float OFFSET_13 = 0.8125F;
    public static final float OFFSET_14 = 0.875F;
    public static final float OFFSET_15 = 0.9375F;

    public static final float[] DEFAULT_OFFSETS = new float[6];
    private static Tessellator instance = Tessellator.instance;

    public BlockRenderer() {
    }

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static void drawFaces(double x, double y, double z, ResourceLocation texture) {
        drawFaces(x, y, z, texture, texture, texture, texture, texture, texture, DEFAULT_OFFSETS);
    }

    public static void drawFaces(double x, double y, double z, ResourceLocation texture, float[] offsets) {
        drawFaces(x, y, z, texture, texture, texture, texture, texture, texture, offsets);
    }

    public static void drawFaces(double x, double y, double z, ResourceLocation xNegTexture, ResourceLocation xPosTexture, ResourceLocation yNegTexture, ResourceLocation yPosTexture, ResourceLocation zNegTexture, ResourceLocation zPosTexture, float[] offsets) {
        glPushMatrix();
        glTranslated(x, y, z);

        drawFaceXNeg(xNegTexture, offsets[0]);
        drawFaceXPos(xPosTexture, offsets[1]);
        drawFaceYNeg(yNegTexture, offsets[2]);
        drawFaceYPos(yPosTexture, offsets[3]);
        drawFaceZNeg(zNegTexture, offsets[4]);
        drawFaceZPos(zPosTexture, offsets[5]);

        glPopMatrix();
    }

    public static void drawFaceXNeg(ResourceLocation texture, float offset) {
        bindTexture(texture);
        instance.startDrawingQuads();
        instance.setNormal(-1.0F, 0.0F, 0.0F);

        instance.addVertexWithUV(0.0D - offset, 0.0D, 0.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(0.0D - offset, 0.0D, 1.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(0.0D - offset, 1.0D, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(0.0D - offset, 1.0D, 0.0D, 0.0D, 0.0D);

        instance.draw();
    }

    public static void drawFaceXPos(ResourceLocation texture, float offset) {
        bindTexture(texture);
        instance.startDrawingQuads();
        instance.setNormal(1.0F, 0.0F, 0.0F);

        instance.addVertexWithUV(1.0D + offset, 0.0D, 1.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(1.0D + offset, 0.0D, 0.0D, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D + offset, 1.0D, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D + offset, 1.0D, 1.0D, 0.0D, 0.0D);

        instance.draw();
    }

    public static void drawFaceYNeg(ResourceLocation texture, float offset) {
        bindTexture(texture);
        instance.startDrawingQuads();
        instance.setNormal(0.0F, -1.0F, 0.0F);

        instance.addVertexWithUV(0.0D, 0.0D - offset, 0.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D - offset, 0.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D - offset, 1.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 0.0D - offset, 1.0D, 1.0D, 1.0D);

        instance.draw();
    }

    public static void drawFaceYPos(ResourceLocation texture, float offset) {
        bindTexture(texture);
        instance.startDrawingQuads();
        instance.setNormal(0.0F, 1.0F, 0.0F);

        instance.addVertexWithUV(0.0D, 1.0D + offset, 1.0D, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D + offset, 1.0D, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D + offset, 0.0D, 0.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 1.0D + offset, 0.0D, 1.0D, 1.0D);

        instance.draw();
    }

    public static void drawFaceZNeg(ResourceLocation texture, float offset) {
        bindTexture(texture);
        instance.startDrawingQuads();
        instance.setNormal(0.0F, 0.0F, -1.0F);

        instance.addVertexWithUV(0.0D, 1.0D, 0.0D - offset, 1.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 0.0D - offset, 0.0D, 0.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 0.0D - offset, 0.0D, 1.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 0.0D - offset, 1.0D, 1.0D);

        instance.draw();
    }

    public static void drawFaceZPos(ResourceLocation texture, float offset) {
        bindTexture(texture);
        instance.startDrawingQuads();
        instance.setNormal(0.0F, 0.0F, 1.0F);

        instance.addVertexWithUV(0.0D, 1.0D, 1.0D + offset, 0.0D, 0.0D);
        instance.addVertexWithUV(0.0D, 0.0D, 1.0D + offset, 0.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 0.0D, 1.0D + offset, 1.0D, 1.0D);
        instance.addVertexWithUV(1.0D, 1.0D, 1.0D + offset, 1.0D, 0.0D);

        instance.draw();
    }
}
