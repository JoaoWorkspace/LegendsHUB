package framework;

import java.awt.Font;

public class CustomFont extends Font{

	/**
	 * This represents the font most widely used in the whole application framework.
	 * Calibri, BOLD and size is defined by you.
	 * @param size - The size of the Font
	 * @author João Mendonça
	 */
	public CustomFont(int size) {
		super("Calibri",BOLD,size);
	}
}
