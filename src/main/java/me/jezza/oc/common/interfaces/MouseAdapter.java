package me.jezza.oc.common.interfaces;

/**
 * @author Jezza
 */
public interface MouseAdapter {
	void onClick(int mouseX, int mouseY, int key);

	void onRelease(int mouseX, int mouseY, int key);

	void mouseChange(int mouseX, int mouseY);
}
