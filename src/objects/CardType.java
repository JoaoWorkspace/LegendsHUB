package objects;

import javax.swing.ImageIcon;


import resources.RescaledImageIcon;

/**
 * Represents the different card types there is, LEGENDARY, EPIC, RARE AND COMMON. UNIQUELEGENDARY represents the LEGENDARY type, only a player can only make use of 1 copy, 
 * which means having more is completely useless. This class is a component of the object Card.
 * <hr></hr>
 * You can get the basevalue of a card (used for probability sorting) with the method {@link #getBaseValue()}.
 * <hr></hr>
 * The method {@link #getImage()} is called by the CollectionManager to support representing the different rarities with corresponding gems.
 * @author João Mendonça
 */
public enum CardType {
	UNIQUELEGENDARY, LEGENDARY, EPIC, RARE, COMMON;

	public int getBaseValue(){
		int toreturn=0;
		switch(this) {
		case UNIQUELEGENDARY:
		case LEGENDARY: 
			toreturn = 6561;
			break;
		case EPIC:      
			toreturn =  729;
			break;
		case RARE:      
			toreturn =  81;
			break;
		case COMMON:    
			toreturn =  9;
			break;	
		default:
			break;
		}
		return toreturn;
	}

	public ImageIcon getImage() {
		ImageIcon icon = new ImageIcon();
		RescaledImageIcon rescaler;
		switch(this) {
		case UNIQUELEGENDARY: 
			rescaler = new RescaledImageIcon("utility/icons/legendary.png", 20, 20);
			icon = rescaler.getRESCALEDICON();
			break;
		case LEGENDARY: 
			rescaler = new RescaledImageIcon("utility/icons/legendary.png", 20, 20);
			icon = rescaler.getRESCALEDICON();
			break;
		case EPIC:
			rescaler = new RescaledImageIcon("utility/icons/epic.png", 20, 20);
			icon = rescaler.getRESCALEDICON();
			break;
		case RARE:
			rescaler = new RescaledImageIcon("utility/icons/rare.png", 20, 20);
			icon = rescaler.getRESCALEDICON();
			break;
		case COMMON:
			rescaler = new RescaledImageIcon("utility/icons/common.png", 20, 20);
			icon = rescaler.getRESCALEDICON();
			break;
		}
		return icon;
	}

}
