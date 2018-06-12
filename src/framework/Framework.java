package framework;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import main.CraftRoulette;
import resources.Splitscreen;

public class Framework extends JFrame{	
	
	/*Java Swing Framework Components*/
	private static final JPanel main = new JPanel(new BorderLayout());	
	private static final ControlPanel CONTROLPANEL = ControlPanel.getInstance();
	private static final CardPanel CARDPANEL = CardPanel.getInstance();	
	private static final CustomButton HINTMESSAGES = new CustomButton("Click me for Hints!");
	/*INSTANCE*/
	private static final Framework FRAMEWORK = new Framework();
	
	/**
	 * Creates a new Framework for this project. This framework comes equipped with all the Java Swing components necessary to work.
	 * @author João Mendonça
	 *
	 */
	private Framework() {
		setTitle("TESL Craft Roulette BETA");
//		controlpanel = new ControlPanel();
		addComponents();
		start();
	}

	private void addComponents(){
		addButtons();
		addSplitscreen();
	}

	private void addButtons() {
		HINTMESSAGES.makeThisAHintButton(CraftRoulette.getHintlist());
//		main.add(HINTMESSAGES,BorderLayout.NORTH);
		add(HINTMESSAGES,BorderLayout.NORTH);
	}

	private void addSplitscreen() {
		Splitscreen splitscreen = new Splitscreen(CARDPANEL,CONTROLPANEL,JSplitPane.HORIZONTAL_SPLIT,null);
//		main.add(splitscreen,BorderLayout.CENTER);
		add(splitscreen,BorderLayout.CENTER);
	}

	/**
	 * Initializes the framework. Since the components are already made when an instance of {@link #Framework()} is created,
	 *  this method adds the main JPanel to our JFrame, and sets it ready to be visualized and interacted with.
	 */
	private void start(){
//		add(main);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static Framework getInstance() {
		return FRAMEWORK;
	}
}
