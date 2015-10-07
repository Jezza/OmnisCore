package me.jezza.oc.api.channel;

import io.netty.buffer.ByteBuf;
import me.jezza.oc.common.utils.maths.CoordSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author Jezza
 */
public interface OutputBuffer {
	OutputBuffer writeBoolean(boolean value);
	OutputBuffer writeChar(char value);
	OutputBuffer writeByte(byte value);
	OutputBuffer writeShort(short value);
	OutputBuffer writeVarShort(short value);
	OutputBuffer writeInt(int value);
	OutputBuffer writeVarInt(int value, int maxSize);
	OutputBuffer writeLong(long value);
	OutputBuffer writeFloat(float value);
	OutputBuffer writeDouble(double value);
	OutputBuffer writeString(String value);

	OutputBuffer writeBytes(byte[] array);
	OutputBuffer writeShorts(short[] array);
	OutputBuffer writeInts(int[] array);
	OutputBuffer writeInts(int x, int y, int z);

	OutputBuffer writeCoordSet(CoordSet coordSet);
	OutputBuffer writeNBT(NBTTagCompound tag);
	OutputBuffer writeItemStack(ItemStack stack);
	OutputBuffer writeFluidStack(FluidStack fluid);

	ByteBuf buffer();
}
