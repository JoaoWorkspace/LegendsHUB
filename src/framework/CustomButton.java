package framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.metal.MetalButtonUI;

import resources.ColorSet;

public class CustomButton extends JButton{
	private ActionListener actionlistener;
	private boolean isMetaButton = false;
	private boolean isPossibleToBuildDeckButton = false;

	public CustomButton(String text,boolean isMetaButton) {
		super(text);
		setFont(new CustomFont(30));
		setForeground(Color.BLACK);
		setFocusPainted(false);
		if(isMetaButton) {
			setBackground(new Color(0,0,0,0));
			setOpaque(false);
//			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		}
		else {
			setBackground(ColorSet.CraftRouletteTheme);
		}
		this.isMetaButton=isMetaButton;
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
	
	public void setMetaButton(boolean isMetaButton) {
		this.isMetaButton = isMetaButton;
	}
	
	public void setPossibleToBuildDeckButton(boolean trueORfalse) {
		isPossibleToBuildDeckButton=trueORfalse;
	}
	
	public void makeThisABackButton() {
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(isPossibleToBuildDeckButton) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			
			Color opaque = new Color(0,255,0,50);
			Color opaquefaded = new Color(100,255,100,40);
			Color faded = new Color(255,255,255,0);
			Point2D start = new Point2D.Float(getX(), getY());
			Point2D end = new Point2D.Float(getWidth(),getY());
			float[] fracs = {0f,0.1f,0.5f,0.8f,1f};
			Color[] colors = {faded,opaquefaded,opaque,opaquefaded,faded};
			LinearGradientPaint doBackground = new LinearGradientPaint(start, end, fracs, colors);
			g2d.setPaint(doBackground);
			g2d.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		else if(isMetaButton) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			
			Color opaque = new Color(0,0,0,50);
			Color opaquefaded = new Color(100,100,100,40);
			Color faded = new Color(255,255,255,0);
			Point2D start = new Point2D.Float(getX(), getY());
			Point2D end = new Point2D.Float(getWidth(),getY());
			float[] fracs = {0f,0.1f,0.5f,0.8f,1f};
			Color[] colors = {faded,opaquefaded,opaque,opaquefaded,faded};
			LinearGradientPaint doBackground = new LinearGradientPaint(start, end, fracs, colors);
			g2d.setPaint(doBackground);
			g2d.fillRect(getX(), getY(), getWidth(), getHeight());
		}
	}
}
