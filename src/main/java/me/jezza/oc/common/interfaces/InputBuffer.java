package me.jezza.oc.common.interfaces;

import io.netty.buffer.ByteBuf;
import me.jezza.oc.common.utils.maths.CoordSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author Jezza
 */
public interface InputBuffer {
	boolean readBoolean();

	char readChar();

	byte readByte();

	short readUByte();

	short readShort();

	int readUShort();

	int readVarShort();

	int readInt();

	long readUInt();

	int readVarInt(int maxSize);

	long readLong();

	float readFloat();

	double readDouble();

	String readString();

	boolean[] readBooleans(int length);

	byte[] readBytes(int length);

	short[] readShorts(int length);

	int[] readInts(int length);

	double[] readDoubles(int length);

	CoordSet readCoordSet();

	NBTTagCompound readNBT();

	ItemStack readItemStack();

	FluidStack readFluidStack();

	ByteBuf raw();
}
