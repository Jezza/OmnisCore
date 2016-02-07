package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface RoundingMethod {
	RoundingMethod FLOOR = new RoundingMethod() {
		@Override
		public int round(double value) {
			return (int) Math.floor(value);
		}
	};
	RoundingMethod ROUND = new RoundingMethod() {
		@Override
		public int round(double value) {
			return (int) Math.round(value);
		}
	};
	RoundingMethod CEIL = new RoundingMethod() {
		@Override
		public int round(double value) {
			return (int) Math.ceil(value);
		}
	};

	int round(double value);
}
