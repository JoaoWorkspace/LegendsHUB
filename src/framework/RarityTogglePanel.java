package framework;

import javax.swing.JPanel;
import resources.ColorSet;

public class RarityTogglePanel extends CustomPanel{
	/*RESOURCES*/
	private static final CustomRadioButton toggleLegendary = new CustomRadioButton("Legendary");
	private static final CustomRadioButton toggleEpic = new CustomRadioButton("Epic");
	private static final CustomRadioButton toggleRare = new CustomRadioButton("Rare");
	private static final CustomRadioButton toggleCommon = new CustomRadioButton("Common");
	/*INSTANCE*/
	private static final RarityTogglePanel RarityTogglePanel = new RarityTogglePanel();
	
	private RarityTogglePanel() {
		add(toggleLegendary);
		add(toggleEpic);
		add(toggleRare);
		add(toggleCommon);
		setBackground(ColorSet.ControlPanelElementsBackground);
	}
	
	public static CustomRadioButton getToggleLegendary() {
		return toggleLegendary;
	}
	
	public static CustomRadioButton getToggleEpic() {
		return toggleEpic;
	}
	
	public static CustomRadioButton getToggleRare() {
		return toggleRare;
	}
	
	public static CustomRadioButton getToggleCommon() {
		return toggleCommon;
	}
	
	public static RarityTogglePanel getInstance() {
		return RarityTogglePanel;
	}
}
