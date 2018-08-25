package resources;

import java.awt.Color;

public class ColorUtilities {
	
	/**
	 * Mixes 2 colors (c0 and c1) and returns the resultant color of them mixed together
	 * @param c0 - Color 1
	 * @param c1 - Color 2
	 * @return Returns the color blend of those 2 colors
	 */
	public static Color blend(Color c0, Color c1) {
		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;

		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());

		return new Color((int) r, (int) g, (int) b, (int) a);
	}
}
