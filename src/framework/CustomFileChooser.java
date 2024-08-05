package framework;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class CustomFileChooser extends JFileChooser{
	
	/**
	 * Saves a file or loads a file with the ".collection" extension.
	 */
	public CustomFileChooser() {
		super(new File("My Collections"));
		setFileFilter(new FileNameExtensionFilter("Card Collection (*.collection)","collection"));
	}
	
	
}
