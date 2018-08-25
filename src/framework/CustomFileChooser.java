package framework;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.stage.FileChooser;

public class CustomFileChooser extends JFileChooser{
	
	/**
	 * Saves a file or loads a file with the ".collection" extension.
	 */
	public CustomFileChooser() {
		super(new File("My Collections"));
		setFileFilter(new FileNameExtensionFilter("Card Collection (*.collection)","collection"));
	}
	
	
}
