package me.jezza.oc.common.tile;

import me.jezza.oc.common.utils.maths.CoordSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileAbstract extends TileEntity {
	protected transient CoordSet coordSet;

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    public void fireEvent(int id, int process) {
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), id, process);
    }

    @Override
    public boolean receiveClientEvent(int id, int process) {
        return false;
    }

    public CoordSet getCoordSet() {
        return coordSet != null ? coordSet : (coordSet = CoordSet.of(xCoord, yCoord, zCoord).lock());
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + getCoordSet();
    }
}