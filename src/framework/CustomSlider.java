package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import javax.swing.JSlider;
import javax.swing.plaf.metal.MetalSliderUI;

import resources.ColorSet;

public class CustomSlider extends JSlider{

	public CustomSlider(int begin, int end) {
		super(begin, end);
		setPreferredSize(Framework.MODE_PANEL_DIMENSION);
		setFont(new CustomFont(10));
		setBackground(ColorSet.CustomPanelBackgroundDarkBlue);
		/* Design the Desired UI for the JSlider */
		setUI(new MetalSliderUI() {

			@Override
			protected Dimension getThumbSize() {
				return new Dimension(35,35);
			}

			@Override
			public void paintThumb(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				Rectangle handler = thumbRect;

				/* Center of the Handler */
				Point2D center = new Point2D.Float(handler.x+15,handler.y+15);
				float radius = 15;
				/* Fractions of the Tracker where there's a division (Here we use 2 fraction because we use 2 colors)*/
				float[] fracs = {0.3f,0.9f};
				/* Which colors to mix in this Gradient(RadialLike)*/
				Color[] colors = {ColorSet.ModePanelNORMAL,ColorSet.ModePanelThumbShine};
				RadialGradientPaint doRadial = new RadialGradientPaint(center, radius, fracs, colors);
				g2d.setPaint(doRadial);
				g2d.fillOval(handler.x, handler.y, 35, 35);
			}

			@Override
			public void paintTrack(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				Rectangle tracker = trackRect;

				/* Begin-point and end-point of the Tracker */
				Point2D start = new Point2D.Float(tracker.x, tracker.y+5);
				Point2D end = new Point2D.Float(tracker.width,tracker.height-10);
				/* Fractions of the Tracker where the color achieves full form (Here we use 3 fraction because we use 3 colors)*/
				float[] fracs = {0f,0.5f,1f};
				/* Which colors to mix in this Gradient(RainbowLike)*/
				Color[] colors = {ColorSet.ModePanelHELPFUL,ColorSet.ModePanelNORMAL,ColorSet.ModePanelCHAOS};
				LinearGradientPaint doNORMAL = new LinearGradientPaint(start, end, fracs, colors);
				g2d.setPaint(doNORMAL);
				g2d.fillRoundRect(tracker.x, tracker.y+5, tracker.width, tracker.height-10, 20, 20);
			}

		});
	}
}
