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
	public int x() {
		return x;
	}

	@Override
	public int y() {
		return y;
	}

	@Override
	public int z() {
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

	@Override
	public CoordSet lock() {
		return lockSet != null ? lockSet : (lockSet = new ImmutableCoordSet());
	}

	@Override
	public int hashCode() {
		return ((713 + x) * 31 + y) * 31 + z;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof CoordSet && at((CoordSet) o);
	}

	public class ImmutableCoordSet extends CoordSet {

		@Override
		public int x() {
			return MutableCoordSet.this.x();
		}

		@Override
		public int y() {
			return MutableCoordSet.this.y();
		}

		@Override
		public int z() {
			return MutableCoordSet.this.z();
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
		public int hashCode() {
			return MutableCoordSet.this.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return o == this || o instanceof CoordSet && at((CoordSet) o);
		}
	}
}
