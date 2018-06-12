package framework;

import javax.swing.JLabel;

public class CustomLabel extends JLabel{

	public CustomLabel(String text) {
		setFont(new CustomFont(18));
		setText(text);
	}
}
