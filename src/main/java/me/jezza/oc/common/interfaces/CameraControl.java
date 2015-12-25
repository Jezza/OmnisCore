package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface CameraControl {
	int LOCK = 0b1000000000000000000;
	int TIME_MASK = 0b111111111111111111;
	int TIME_MAX = TIME_MASK;

	void destination(double x, double y, double z);

	void onStart();

	void onEnd();

	CameraProperties properties();

	CameraProperties properties(int flags);

	void lookAt(double x, double y, double z);

	void execute();
}
