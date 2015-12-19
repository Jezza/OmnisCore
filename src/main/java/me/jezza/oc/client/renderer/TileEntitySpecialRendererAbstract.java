package me.jezza.oc.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public abstract class TileEntitySpecialRendererAbstract<T extends TileEntity> extends TileEntitySpecialRenderer {
	protected final Class<T> type;

	public TileEntitySpecialRendererAbstract(Class<T> type) {
		this.type = type;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
		if (type.isInstance(tileEntity))
			renderTileAt((T) tileEntity, x, y, z, tick);
	}

	public abstract void renderTileAt(T tile, double x, double y, double z, float tick);

	@Override
	protected void bindTexture(ResourceLocation texture) {
		field_147501_a.field_147553_e.bindTexture(texture);
	}

	protected final TileEntityRendererDispatcher dispatcher() {
		return field_147501_a;
	}

	@Override
	public final void func_147496_a(World world) {
		setupRenderer(world);
	}

	public void setupRenderer(World world) {
	}

	public FontRenderer fontRenderer() {
		return func_147498_b();
	}
}
