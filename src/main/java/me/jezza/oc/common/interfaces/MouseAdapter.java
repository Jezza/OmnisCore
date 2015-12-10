package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface MouseAdapter {
	void onClick(int x, int y, int key);

	void onRelease(int x, int y, int key);

	void mouseChange(int x, int y);
}
