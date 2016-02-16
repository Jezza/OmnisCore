package me.jezza.oc.common.core.channel;

import java.util.BitSet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.maths.CoordSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author Jezza
 */
public class OmnisBuffer implements InputBuffer, OutputBuffer {
	private final ByteBuf buffer;

	public OmnisBuffer() {
		buffer = Unpooled.buffer();
	}

	public OmnisBuffer(ByteBuf buffer) {
		this.buffer = buffer;
	}

	@Override
	public boolean readBoolean() {
		return buffer.readBoolean();
	}

	@Override
	public char readChar() {
		return buffer.readChar();
	}

	@Override
	public byte readByte() {
		return buffer.readByte();
	}

	@Override
	public short readUByte() {
		return buffer.readUnsignedByte();
	}

	@Override
	public short readShort() {
		return buffer.readShort();
	}

	@Override
	public int readUShort() {
		return buffer.readUnsignedShort();
	}

	@Override
	public int readVarShort() {
		return ByteBufUtils.readVarShort(buffer);
	}

	@Override
	public int readInt() {
		return buffer.readInt();
	}

	@Override
	public long readUInt() {
		return buffer.readUnsignedInt();
	}

	@Override
	public int readVarInt(int maxSize) {
		return ByteBufUtils.readVarInt(buffer, maxSize);
	}

	@Override
	public long readLong() {
		return buffer.readLong();
	}

	@Override
	public float readFloat() {
		return buffer.readFloat();
	}

	@Override
	public double readDouble() {
		return buffer.readDouble();
	}

	@Override
	public String readString() {
		return ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public boolean[] readBooleans(int length) {
		byte[] bytes = readBytes((int) Math.ceil((double) length / 8D));
		BitSet set = BitSet.valueOf(bytes);
		boolean[] values = new boolean[length];
		for (int i = 0; i < length; i++)
			values[i] = set.get(i);
		return values;
	}

	@Override
	public byte[] readBytes(int length) {
		return buffer.readBytes(length).array();
	}

	@Override
	public short[] readShorts(int length) {
		short[] values = new short[length];
		for (int i = 0; i < length; i++)
			values[i] = buffer.readShort();
		return values;
	}

	@Override
	public int[] readInts(int length) {
		int[] values = new int[length];
		for (int i = 0; i < length; i++)
			values[i] = buffer.readInt();
		return values;
	}

	@Override
	public double[] readDoubles(int length) {
		double[] values = new double[length];
		for (int i = 0; i < length; i++)
			values[i] = buffer.readDouble();
		return values;
	}

	@Override
	public CoordSet readCoordSet() {
		return CoordSet.of(readInts(3));
	}

	@Override
	public NBTTagCompound readNBT() {
		return ByteBufUtils.readTag(buffer);
	}

	@Override
	public ItemStack readItemStack() {
		return ByteBufUtils.readItemStack(buffer);
	}

	@Override
	public FluidStack readFluidStack() {
		short fluidID = buffer.readShort();
		if (fluidID < 0)
			return null;
		Fluid fluid = FluidRegistry.getFluid(fluidID);
		return fluid != null ? new FluidStack(fluid, readVarInt(5), readNBT()) : null;
	}

	@Override
	public OutputBuffer writeBoolean(boolean value) {
		buffer.writeBoolean(value);
		return this;
	}

	@Override
	public OutputBuffer writeChar(char value) {
		buffer.writeChar(value);
		return this;
	}

	@Override
	public OutputBuffer writeByte(byte value) {
		buffer.writeByte(value);
		return this;
	}

	@Override
	public OutputBuffer writeShort(short value) {
		buffer.writeShort(value);
		return this;
	}

	@Override
	public OutputBuffer writeVarShort(short value) {
		ByteBufUtils.writeVarShort(buffer, value);
		return this;
	}

	@Override
	public OutputBuffer writeInt(int value) {
		buffer.writeInt(value);
		return this;
	}

	@Override
	public OutputBuffer writeVarInt(int value, int maxSize) {
		ByteBufUtils.writeVarInt(buffer, value, maxSize);
		return this;
	}

	@Override
	public OutputBuffer writeLong(long value) {
		buffer.writeLong(value);
		return this;
	}

	@Override
	public OutputBuffer writeFloat(float value) {
		buffer.writeFloat(value);
		return this;
	}

	@Override
	public OutputBuffer writeDouble(double value) {
		buffer.writeDouble(value);
		return this;
	}

	@Override
	public OutputBuffer writeString(String value) {
		ByteBufUtils.writeUTF8String(buffer, value);
		return this;
	}

	@Override
	public OutputBuffer writeBooleans(boolean[] array) {
		BitSet set = new BitSet(array.length);
		for (int i = 0; i < array.length; i++)
			set.set(i, array[i]);
		buffer.writeBytes(set.toByteArray());
		return this;
	}

	@Override
	public OutputBuffer writeBytes(byte[] array) {
		buffer.writeBytes(array);
		return this;
	}

	@Override
	public OutputBuffer writeShorts(short[] array) {
		for (short value : array)
			buffer.writeShort(value);
		return this;
	}

	@Override
	public OutputBuffer writeInts(int[] array) {
		for (int value : array)
			buffer.writeInt(value);
		return this;
	}

	@Override
	public OutputBuffer writeInts(int x, int y, int z) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		return this;
	}

	@Override
	public OutputBuffer writeDoubles(double[] array) {
		for (double value : array)
			buffer.writeDouble(value);
		return this;
	}

	@Override
	public OutputBuffer writeCoordSet(CoordSet coordSet) {
		return writeInts(coordSet.asArray());
	}

	@Override
	public OutputBuffer writeNBT(NBTTagCompound tag) {
		ByteBufUtils.writeTag(buffer, tag);
		return this;
	}

	@Override
	public OutputBuffer writeItemStack(ItemStack itemStack) {
		ByteBufUtils.writeItemStack(buffer, itemStack);
		return this;
	}

	@Override
	public OutputBuffer writeFluidStack(FluidStack fluid) {
		if (fluid == null) {
			buffer.writeShort(-1);
		} else {
			buffer.writeShort(fluid.getFluidID());
			ByteBufUtils.writeVarInt(buffer, fluid.amount, 5);
			ByteBufUtils.writeTag(buffer, fluid.tag);
		}
		return this;
	}

	@Override
	public ByteBuf raw() {
		return buffer;
	}

	public ByteBuf copy() {
		return buffer.copy();
	}
}
