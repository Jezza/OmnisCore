package me.jezza.oc.common.core.channel.internal;

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
    // TODO CoordSet readCoordSet(CoordSet coordSet);
    NBTTagCompound readNBT();
    ItemStack readItemStack();
    FluidStack readFluidStack();
}
