package framework;

import javax.swing.JRadioButton;

import resources.ColorSet;

public class SetTogglePanel extends CustomPanel{
	/*ELEMENTS*/
	private static final CustomRadioButton setCore = new CustomRadioButton("Core");
	private static final CustomRadioButton setHeroesOfSkyrim = new CustomRadioButton("Heroes of Skyrim");
	private static final CustomRadioButton setHousesOfMorrowind = new CustomRadioButton("Houses Of Morrowind");
	private static final CustomRadioButton setMonthlyRewards = new CustomRadioButton("Monthly Rewards");
	private static final CustomRadioButton setStarterCards = new CustomRadioButton("Starter Cards");
	/*INSTANCES*/
	private static final SetTogglePanel setPanelInstance_1 = new SetTogglePanel(1);
	private static final SetTogglePanel setPanelInstance_2 = new SetTogglePanel(2);
	private static final SetTogglePanel setPanelInstance_3 = new SetTogglePanel(3);
	
	
	private SetTogglePanel(int which) {
		if(which==1) {
			add(setCore);
			add(setHeroesOfSkyrim);
			add(setHousesOfMorrowind);
		}
		if(which==2) {
			add(setMonthlyRewards);
			add(setStarterCards);
		}
		if(which==3) {
			// Future
		}
		setBackground(ColorSet.ControlPanelElementsBackground);
	}
	
	public static CustomRadioButton getSetCore() {
		return setCore;
	}
	
	public static CustomRadioButton getSetHeroesOfSkyrim() {
		return setHeroesOfSkyrim;
	}
	
	public static CustomRadioButton getSetHousesOfMorrowind() {
		return setHousesOfMorrowind;
	}
	
	public static CustomRadioButton getSetMonthlyRewards() {
		return setMonthlyRewards;
	}
	
	public static CustomRadioButton getSetStarterCards() {
		return setStarterCards;
	}
	
	public static SetTogglePanel getFirstInstance() {
		return setPanelInstance_1;
	}
	
	public static SetTogglePanel getSecondInstance() {
		return setPanelInstance_2;
	}
	
}
