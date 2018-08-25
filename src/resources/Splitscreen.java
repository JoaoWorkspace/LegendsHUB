package resources;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import framework.CraftRoulettePanel;
import framework.MetaDecksPanel;

public class Splitscreen extends JSplitPane {

	/**
	 * Creates a JSplitPane between 2 Components. 
	 * @param left - Component that will be on the left division
	 * @param right - Component that will be on the right division
	 * @param split - Defines if the splitscreen will have a horizontal(JSplitPane.HORIZONTAL_SPLIT) or vertical(JSplitPane.VERTICAL_SPLIT) split.
	 * @param dimension - Defines the Dimension you wish both components share to make them symmetrical, or null if you wish to use the component's preferred dimension.
	 * @author João Mendonça
	 */
	public Splitscreen(Component left, Component right,int split, Dimension dimension) {
		super(split);
		if(dimension!=null) {
			right.setPreferredSize(dimension);
			left.setPreferredSize(dimension);
		}
		if(dimension==null) {
			left.setPreferredSize(left.getPreferredSize());
			right.setPreferredSize(right.getPreferredSize());
		}
		add(left);
		add(right);
		setResizeWeight(1.0);
		setEnabled(false);
		if(split==HORIZONTAL_SPLIT)setDividerUI();
		else setDividerSize(0);
		setBorder(null);
	}

	private void setDividerUI() {
		setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {

				return new BasicSplitPaneDivider(this) {
					public void setBorder(Border b) {
					}
					@Override
					public void paint(Graphics g) {
						/* Begin-point and end-point of the Divider */
						Point2D start = new Point2D.Float(0, 0);
						Point2D end = new Point2D.Float(getSize().width, 0);
						Graphics2D g2d = (Graphics2D)g;
						float[] fracs = {0f,1f};
						/* Which colors to mix in this Gradient(RainbowLike)*/
						Color[] colors = {new Color(44, 62, 80),new Color(189, 195, 199)};
						LinearGradientPaint doNORMAL = new LinearGradientPaint(start, end, fracs, colors);
						g2d.setPaint(doNORMAL);
						g2d.fillRect(0, 0, getSize().width, getSize().height);
					}
				};
			}
		});
	}
}
