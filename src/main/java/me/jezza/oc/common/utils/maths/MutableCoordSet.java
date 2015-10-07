package me.jezza.oc.common.utils.maths;

public class MutableCoordSet extends CoordSet {
	protected transient ImmutableCoordSet lockSet;
	protected int x, y, z;

	protected MutableCoordSet(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public CoordSet add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	@Override
	public CoordSet subtract(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	@Override
	public CoordSet multiply(int x, int y, int z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	@Override
	public CoordSet divide(int x, int y, int z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}

	@Override
	public CoordSet chunkCoords() {
		x >>= 4;
		y >>= 4;
		z >>= 4;
		return this;
	}

	@Override
	public CoordSet blockCoords() {
		x <<= 4;
		y <<= 4;
		z <<= 4;
		return this;
	}

	@Override
	public CoordSet chunkOffset() {
		x = x < 0 ? x % 16 + 16 & 15 : x % 16;
		y = y < 0 ? y % 16 + 16 & 15 : y % 16;
		z = z < 0 ? z % 16 + 16 & 15 : z % 16;
		return this;
	}

	public static void main(String[] args) {
		CoordSet two = CoordSet.of(2);
		System.out.println(two);
		CoordSet lock = two.lock();
		System.out.println(lock == two.lock());
		System.out.println(two.add(2).at(lock));
		System.out.println(lock);
	}

	@Override
	public CoordSet lock() {
		return lockSet != null ? lockSet : (lockSet = new ImmutableCoordSet());
	}

	@Override
	public String toPacketString() {
		return x + ":" + y + ":" + z;
	}

	@Override
	public String toString() {
		return '[' + toPacketString() + ']';
	}

	@Override
	public int hashCode() {
		return ((713 + x) * 31 + y) * 31 + z;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof CoordSet && at((CoordSet) o);
	}

	@Override
	public CoordSet copy() {
		return clone();
	}

	public class ImmutableCoordSet extends CoordSet {

		@Override
		public int getX() {
			return MutableCoordSet.this.getX();
		}

		@Override
		public int getY() {
			return MutableCoordSet.this.getY();
		}

		@Override
		public int getZ() {
			return MutableCoordSet.this.getZ();
		}

		@Override
		public CoordSet add(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet subtract(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet multiply(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet divide(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet chunkCoords() {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet blockCoords() {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet chunkOffset() {
			throw new UnsupportedOperationException();
		}

		@Override
		public CoordSet lock() {
			return this;
		}

		@Override
		public String toPacketString() {
			return MutableCoordSet.this.toPacketString();
		}

		@Override
		public String toString() {
			return MutableCoordSet.this.toString();
		}

		@Override
		public int hashCode() {
			return MutableCoordSet.this.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof CoordSet && at((CoordSet) o);
		}
	}
}
