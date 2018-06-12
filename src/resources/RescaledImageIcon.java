package resources;

import java.awt.Image;

import javax.swing.ImageIcon;

public class RescaledImageIcon {
	private ImageIcon RESCALEDICON;
	
	/**
	 * Will rescale the image to whatever pixel size you desire.
	 * @param pathToImageIcon - Path to the original image
	 * @param width - Desired Width of the Image
	 * @param height - Desired Height of the Image
	 * @author João Mendonça
	 */
	public RescaledImageIcon(String pathToImageIcon, int width, int height) {
		RESCALEDICON = new ImageIcon(pathToImageIcon); // load the image to a imageIcon
		Image image = RESCALEDICON.getImage(); // transform it 
		Image newimg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale it the smooth way  
		RESCALEDICON = new ImageIcon(newimg);  // transform it back
	}

	public ImageIcon getRESCALEDICON() {
		return RESCALEDICON;
	}
}
