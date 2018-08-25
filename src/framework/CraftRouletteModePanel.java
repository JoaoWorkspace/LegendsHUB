package framework;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Hashtable;

import javax.swing.JLabel;
import objects.CraftRouletteMode;

public class CraftRouletteModePanel extends CustomPanel{
	/*RESOURCES*/
	private static CraftRouletteMode MODE;
	private static final CustomSlider MODESLIDER = new CustomSlider(0,2);
	/*INSTANCE*/
	private static final CraftRouletteModePanel CRAFTMODEPANEL = new CraftRouletteModePanel();
	
	private CraftRouletteModePanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		add(MODESLIDER);
		addLabels();
	}

	/* Set JLabels for specific Values*/
	private void addLabels() {
		Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
		position.put(0, new CustomLabel("HELPFUL",false).HELPFUL_MODE());
		position.put(1, new CustomLabel("NORMAL",false).NORMAL_MODE());
		position.put(2, new CustomLabel("CHAOS",false).CHAOS_MODE());
		MODESLIDER.setLabelTable(position);
		MODESLIDER.setPaintLabels(true);
	}
	
	public static CraftRouletteMode getMODE() {
		if(MODESLIDER.getValue() == 0) MODE = CraftRouletteMode.HELPFUL;
		if(MODESLIDER.getValue() == 1) MODE = CraftRouletteMode.NORMAL;
		if(MODESLIDER.getValue() == 2) MODE = CraftRouletteMode.CHAOS;
		return MODE;
	}
	
	public static CraftRouletteModePanel getInstance() {
		return CRAFTMODEPANEL;
	}
}
