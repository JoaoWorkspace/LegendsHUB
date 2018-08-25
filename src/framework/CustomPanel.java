package framework;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import resources.ColorSet;

public class CustomPanel extends JPanel{

	public CustomPanel(LayoutManager layout) {
		setLayout(layout);
	}
	
	public CustomPanel() {
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		Point2D start = new Point2D.Float(getX(), getY());
		Point2D end = new Point2D.Float(getX(),getPreferredSize().height);
		float[] fracs = {0f,1f};
		Color[] colors = {ColorSet.CustomPanelBackgroundLightBlue,ColorSet.CustomPanelBackgroundDarkBlue};
		LinearGradientPaint doBackground = new LinearGradientPaint(start, end, fracs, colors);
		g2d.setPaint(doBackground);
		g2d.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
