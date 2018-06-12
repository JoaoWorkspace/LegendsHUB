package resources;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSplitPane;

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
	}
}
