package framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import resources.ColorSet;
import resources.RescaledImageIcon;

public class CustomRadioButton extends JRadioButton{
	private static final ImageIcon toggledON = new RescaledImageIcon("utility/icons/toggleon.png",40,20).getRESCALEDICON();
	private static final ImageIcon toggledOFF = new RescaledImageIcon("utility/icons/toggleoff.png",40,20).getRESCALEDICON();

	public CustomRadioButton(String text) {
		setText(text);
		setIcon(toggledON);
		setSelected(true);
		setBackground(ColorSet.ControlPanelElementsBackground);
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isSelected()) setIcon(toggledON);
				else setIcon(toggledOFF);
			}
		});
	}
}
