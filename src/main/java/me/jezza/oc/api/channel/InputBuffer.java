package me.jezza.oc.api.channel;

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

	byte[] readBytes(int length);
	short[] readShorts(int length);
	int[] readInts(int length);

	CoordSet readCoordSet();
	NBTTagCompound readNBT();
	ItemStack readItemStack();
	FluidStack readFluidStack();

	ByteBuf buffer();
}
