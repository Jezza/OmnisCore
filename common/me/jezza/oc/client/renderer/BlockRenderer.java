package me.jezza.oc.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockRenderer {
    public static float OFFSET_1 = 0.0625F;
    public static float OFFSET_2 = 0.125F;
    public static float OFFSET_3 = 0.1875F;
    public static float OFFSET_4 = 0.25F;
    public static float OFFSET_5 = 0.3125F;
    public static float OFFSET_6 = 0.375F;
    public static float OFFSET_7 = 0.4375F;
    public static float OFFSET_8 = 0.5F;
    public static float OFFSET_9 = 0.5625F;
    public static float OFFSET_10 = 0.625F;
    public static float OFFSET_11 = 0.6875F;
    public static float OFFSET_12 = 0.75F;
    public static float OFFSET_13 = 0.8125F;
    public static float OFFSET_14 = 0.875F;
    public static float OFFSET_15 = 0.9375F;

    public BlockRenderer() {
    }

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon icon, boolean st) {
        drawFaces(renderblocks, block, icon, icon, icon, icon, icon, icon, st);
    }

    public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon i1, IIcon i2, IIcon i3, IIcon i4, IIcon i5, IIcon i6, boolean solidTop) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, i1);
        tessellator.draw();
        if (solidTop) {
            GL11.glDisable(3008);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, i2);
        tessellator.draw();
        if (solidTop) {
            GL11.glEnable(3008);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, i3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, i4);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, i5);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, i6);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static int setBrightness(IBlockAccess blockAccess, int i, int j, int k, Block block) {
        return setBrightness(blockAccess, i, j, k, block, 1.0F);
    }

    public static int setBrightness(IBlockAccess blockAccess, int i, int j, int k, Block block, float brightness) {
        Tessellator tessellator = Tessellator.instance;
        int mb = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
        mb = 14680112;
        tessellator.setBrightness(mb);
        int l = block.colorMultiplier(blockAccess, i, j, k);
        float f1 = (float) (l >> 16 & 255) / 255.0F;
        float f2 = (float) (l >> 8 & 255) / 255.0F;
        float f3 = (float) (l & 255) / 255.0F;
        if (EntityRenderer.anaglyphEnable) {
            float f6 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f7 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f6;
            f2 = f4;
            f3 = f7;
        }

        tessellator.setColorOpaque_F(brightness * f1, brightness * f2, brightness * f3);
        return mb;
    }

    protected static void renderAllSides(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex) {
        renderAllSides(world, x, y, z, block, renderer, tex, true);
    }

    protected static void renderAllSides(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex, boolean allSides) {
        if (allSides || block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
            renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, tex);
        }

        if (allSides || block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
            renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, tex);
        }

        if (allSides || block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
            renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, tex);
        }

        if (allSides || block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
            renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, tex);
        }

        if (allSides || block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
            renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, tex);
        }

        if (allSides || block.shouldSideBeRendered(world, x, y - 1, z, 6)) {
            renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, tex);
        }

    }

    protected static void renderAllSides(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, boolean allSides) {
        if (allSides || block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
            renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, block.getIcon(world, x, y, z, 5));
        }

        if (allSides || block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
            renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, block.getIcon(world, x, y, z, 4));
        }

        if (allSides || block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
            renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, block.getIcon(world, x, y, z, 3));
        }

        if (allSides || block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
            renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, block.getIcon(world, x, y, z, 2));
        }

        if (allSides || block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
            renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, block.getIcon(world, x, y, z, 1));
        }

        if (allSides || block.shouldSideBeRendered(world, x, y - 1, z, 6)) {
            renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, block.getIcon(world, x, y, z, 0));
        }

    }

    protected static void renderAllSides(int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex) {
        renderer.renderFaceXPos(block, (double) (x - 1), (double) y, (double) z, tex);
        renderer.renderFaceXNeg(block, (double) (x + 1), (double) y, (double) z, tex);
        renderer.renderFaceZPos(block, (double) x, (double) y, (double) (z - 1), tex);
        renderer.renderFaceZNeg(block, (double) x, (double) y, (double) (z + 1), tex);
        renderer.renderFaceYPos(block, (double) x, (double) (y - 1), (double) z, tex);
        renderer.renderFaceYNeg(block, (double) x, (double) (y + 1), (double) z, tex);
    }
}
