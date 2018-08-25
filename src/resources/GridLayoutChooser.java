package resources;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;

public class GridLayoutChooser {
	/*COLLECTION*/
	private static final LinkedHashMap<Integer, Integer> squareRootOf = new LinkedHashMap<Integer, Integer>();
	/*INSTANCE*/
	private static final GridLayoutChooser INSTANCE = new GridLayoutChooser();
	
	private GridLayoutChooser() {
		feedCollection();
	}
	
	private void feedCollection() {
		for(int i = 1;i<=100000;i++) squareRootOf.put(i, (int) Math.pow(i, 2));
	}
	
	/**
	 * Calculates the root square of the number of components that will be added to this Layout, to try and get a gridlayout looking like a square as much as possible 
	 * so the number of components feels compacted in the JFrame.
	 * @param numberOfObjects - Number of components to be added to the Panel
	 * @return A GridLayout with a suitable number of rows and columns for the amount of components we'll work with.
	 */
	public static GridLayout generateAppropriateGridLayout(int numberOfObjects) {
		GridLayout appropriate = new GridLayout(1, 1);
		double rootTheNumberOfObjects = Math.sqrt(numberOfObjects);
		for(Integer root : squareRootOf.keySet()) {
			if(Math.ceil(rootTheNumberOfObjects)==Double.valueOf(root)) {
				/*Means we can't use 5 x 5 in case of 26, for example, but 6 x 5, since the rootTheNumberOfObjects returns 5.1*/
				if(rootTheNumberOfObjects > root) appropriate = new GridLayout(root+1,root);
				/*Means we can't use 5 x 5 in case of 24, for example, but 5 x 4, since the rootTheNumberOfObjects returns 4.9*/
				else if(rootTheNumberOfObjects < root) appropriate = new GridLayout(root,root-1);
				else appropriate = new GridLayout(root,root);
			}
		}
		return appropriate;
	}
	
	public static GridLayout generateAppropriatePurchasableLabelGridLayout(int numberOfObjects) {
		int numberOfRows = numberOfObjects/8;
		double division = numberOfObjects/8.0;
		if(division>numberOfRows) numberOfRows++;
		GridLayout appropriate = new GridLayout(numberOfRows, 8);
		return appropriate;
	}

	public static GridLayoutChooser getInstance() {
		return INSTANCE;
	}
}
