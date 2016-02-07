package me.jezza.oc.common.utils.maths;

import io.netty.buffer.ByteBuf;
import me.jezza.oc.common.interfaces.Copyable;
import me.jezza.oc.common.interfaces.RoundingMethod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.regex.Pattern;

/**
 * @author Jezza
 */
public abstract class CoordSet implements Copyable<CoordSet> {
	public static final String COORD_SET_NBT_TAG = "CoordSet";
	public static final Pattern COORD_SET_PATTERN = Pattern.compile("-?\\d+?:-?\\d+?:-?\\d*");

	public abstract int x();

	public abstract int y();

	public abstract int z();

	public CoordSet add(int value) {
		return add(value, value, value);
	}

	public CoordSet add(CoordSet set) {
		return add(set.x(), set.y(), set.z());
	}

	public CoordSet add(ForgeDirection direction) {
		return add(direction.offsetX, direction.offsetY, direction.offsetZ);
	}

	public abstract CoordSet add(int x, int y, int z);

	public CoordSet subtract(int value) {
		return subtract(value, value, value);
	}

	public CoordSet subtract(CoordSet set) {
		return subtract(set.x(), set.y(), set.z());
	}

	public CoordSet subtract(ForgeDirection direction) {
		return subtract(direction.offsetX, direction.offsetY, direction.offsetZ);
	}

	public abstract CoordSet subtract(int x, int y, int z);

	public CoordSet multiply(int value) {
		return multiply(value, value, value);
	}

	public CoordSet multiply(CoordSet set) {
		return multiply(set.x(), set.y(), set.z());
	}

	public CoordSet multiply(ForgeDirection direction) {
		return multiply(direction.offsetX, direction.offsetY, direction.offsetZ);
	}

	public abstract CoordSet multiply(int x, int y, int z);

	public CoordSet divide(int value) {
		return divide(value, value, value);
	}

	public CoordSet divide(CoordSet set) {
		return divide(set.x(), set.y(), set.z());
	}

	public CoordSet divide(ForgeDirection direction) {
		return divide(direction.offsetX, direction.offsetY, direction.offsetZ);
	}

	public abstract CoordSet divide(int x, int y, int z);

	public boolean at(CoordSet set) {
		return at(set.x(), set.y(), set.z());
	}

	public boolean at(int x, int y, int z) {
		return x == x() && y == y() && z == z();
	}

	public boolean isAdjacent(CoordSet coordSet) {
		return isAdjacent(coordSet.x(), coordSet.y(), coordSet.z());
	}

	public boolean isAdjacent(int x, int y, int z) {
		int ourX = x();
		int ourY = y();
		int ourZ = z();
		if (ourX == x && ourY == y)
			return ourZ == z - 1 || ourZ == z + 1;
		if (ourX == x && ourZ == z)
			return ourY == y - 1 || ourY == y + 1;
		return ourZ == z && ourY == y && (ourX == x - 1 || ourX == x + 1);
	}

	public boolean within(CoordSet set, double range) {
		return withinSq(set.x(), set.y(), set.z(), range * range);
	}

	public boolean within(int x, int y, int z, double range) {
		return withinSq(x, y, z, range * range);
	}

	public boolean withinSq(CoordSet set, double rangeSq) {
		return withinSq(set.x(), set.y(), set.z(), rangeSq);
	}

	public boolean withinSq(int x, int y, int z, double rangeSq) {
		return getDistanceSq(x, y, z) <= rangeSq;
	}

	public double getDistance(CoordSet set) {
		return Math.sqrt(getDistanceSq(set.x(), set.y(), set.z()));
	}

	public double getDistance(int x, int y, int z) {
		return Math.sqrt(getDistanceSq(x, y, z));
	}

	public double getDistanceSq(CoordSet set) {
		return getDistanceSq(set.x(), set.y(), set.z());
	}

	public double getDistanceSq(int x, int y, int z) {
		double x2 = x - x();
		double y2 = y - y();
		double z2 = z - z();
		return x2 * x2 + y2 * y2 + z2 * z2;
	}

	public boolean isAirBlock(World world) {
		return world.isAirBlock(x(), y(), z());
	}

	public boolean setBlockToAir(World world) {
		return world.setBlockToAir(x(), y(), z());
	}

	public Block getBlock(IBlockAccess world) {
		return world.getBlock(x(), y(), z());
	}

	public boolean hasTileEntity(IBlockAccess world) {
		return getTileEntity(world) != null;
	}

	public TileEntity getTileEntity(IBlockAccess world) {
		return world.getTileEntity(x(), y(), z());
	}

	public abstract CoordSet chunkCoords();

	public abstract CoordSet blockCoords();

	public abstract CoordSet chunkOffset();

	public abstract CoordSet lock();

	public int[] asArray() {
		return new int[]{x(), y(), z()};
	}

	public void writeBytes(ByteBuf bytes) {
		bytes.writeInt(x());
		bytes.writeInt(y());
		bytes.writeInt(z());
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setIntArray(COORD_SET_NBT_TAG, asArray());
	}

	@Override
	protected final CoordSet clone() {
		try {
			return (CoordSet) super.clone();
		} catch (CloneNotSupportedException e) {
			// Should never happen. Ever....
			throw new IllegalStateException(e);
		}
	}

	@Override
	public CoordSet copy() {
		return clone();
	}

	@Override
	public String toString() {
		return Integer.toString(x()) + ':' + Integer.toString(y()) + ':' + Integer.toString(z());
	}

	public String toPacketString() {
		return "[" + toString() + ']';
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	public static CoordSet of(int value) {
		return new MutableCoordSet(value, value, value);
	}

	public static CoordSet of(int x, int y, int z) {
		return new MutableCoordSet(x, y, z);
	}

	public static CoordSet of(ChunkCoordinates coord) {
		return new MutableCoordSet(coord.posX, coord.posY, coord.posZ);
	}

	public static CoordSet of(TileEntity tile) throws NullPointerException {
		return new MutableCoordSet(tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public static CoordSet of(ByteBuf bytes) {
		return new MutableCoordSet(bytes.readInt(), bytes.readInt(), bytes.readInt());
	}

	public static CoordSet of(int[] coords) throws ArrayIndexOutOfBoundsException {
		return new MutableCoordSet(coords[0], coords[1], coords[2]);
	}

	public static CoordSet of(String x, String y, String z) throws NumberFormatException {
		return new MutableCoordSet(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
	}

	public static CoordSet of(NBTTagCompound tag) {
		int[] coords = tag.getIntArray(COORD_SET_NBT_TAG);
		if (coords.length != 0)
			return new MutableCoordSet(coords[0], coords[1], coords[2]);
		return new MutableCoordSet(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
	}

	public static CoordSet of(String data) throws IllegalArgumentException {
		if (!COORD_SET_PATTERN.matcher(data).matches())
			throw new IllegalArgumentException("Not a valid CoordSet! " + data);
		String[] coords = data.split(":");
		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);
		int z = Integer.parseInt(coords[2]);
		return new MutableCoordSet(x, y, z);
	}

	public static CoordSet of(EntityPlayer player) {
		return of(player, RoundingMethod.FLOOR);
	}

	public static CoordSet of(EntityPlayer player, RoundingMethod roundingMethod) {
		return new MutableCoordSet(roundingMethod.round(player.posX), roundingMethod.round(player.posY), roundingMethod.round(player.posZ));
	}
}
