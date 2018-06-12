package framework;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class CustomButton extends JButton{
	private ActionListener actionlistener;

	public CustomButton(String text) {
		super(text);
		setFont(new CustomFont(30));
		setForeground(Color.BLACK);
		setBackground(new Color(80, 190, 237));
		setFocusPainted(false);
	}

	public void makeThisAHintButton(ArrayList<String> hintlist) {
		removeActionListener(actionlistener);
		actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for(String hint : hintlist) if(getText().equals(hint)) found = true;
				if(!found) setText(hintlist.get(0));
				else {
					int hintIndex = 0;
					for(int i=0;i<hintlist.size();i++) {
						if(getText().equals(hintlist.get(i))) hintIndex = i;
					}
					if(hintIndex==0 || hintIndex<hintlist.size()-1) setText(hintlist.get(hintIndex+1));
					else setText(hintlist.get(0));
				}
			}
		};	
		addActionListener(actionlistener);
	}
}
