package framework;

import java.awt.Color;

import javax.swing.JLabel;

import resources.ColorSet;

public class CustomLabel extends JLabel{

	public CustomLabel(String text, boolean center) {
		setFont(new CustomFont(18));
		setText(text);
		if(center) setHorizontalAlignment(JLabel.CENTER);
	}
	
	public CustomLabel(String text, int desiredFontSize, boolean center) {
		setFont(new CustomFont(desiredFontSize));
		setText(text);
		if(center) setHorizontalAlignment(JLabel.CENTER);
	}
	
	
	public CustomLabel HELPFUL_MODE() {
		setForeground(ColorSet.ModePanelHELPFUL);
		setFont(new CustomFont(40));
		return this;
	}
	
	public CustomLabel NORMAL_MODE() {
		setForeground(ColorSet.ModePanelNORMAL);
		setFont(new CustomFont(40));
		return this;
	}
	
	public CustomLabel CHAOS_MODE() {
		setForeground(ColorSet.ModePanelCHAOS);
		setFont(new CustomFont(40));
		return this;
	}
}
