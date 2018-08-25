package framework;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import client.Client;
import resources.Splitscreen;
import hub.Hub;
import javafx.scene.image.Image;
import objects.Card;
import server.CraftRouletteServer;

public class Framework extends JFrame{
	/*Preferred Sizes of the Components*/
	public static final Dimension CONTROL_PANEL_DIMENSION = new Dimension(430,680); 
	public static final Dimension MODE_PANEL_DIMENSION = new Dimension(810,95);    
	public static final Dimension CARD_PANEL_DIMENSION = new Dimension(370,680);
	/*Components*/
	private static final JPanel main = new JPanel(new BorderLayout());
	private static final Menu MENUBAR = Menu.getInstance();
	private static final ControlPanel CONTROLPANEL = ControlPanel.getInstance();
	private static final CraftRoulettePanel CRAFTROULETTE = CraftRoulettePanel.getInstance();
	private static final MetaDecksPanel METADECKS = MetaDecksPanel.getInstance();
	private static final CustomButton HINTMESSAGES = new CustomButton("Click me for Hints!",false);
	private static final CraftRouletteModePanel CRAFTMODEPANEL = CraftRouletteModePanel.getInstance();
	private static Splitscreen split;
	/*INSTANCE*/
	private static Framework FRAMEWORK = new Framework();
	
	
	/**
	 * Creates a new Framework for this project. This framework comes equipped with all the Java Swing components necessary for the CraftRoulette to work.
	 * @author João Mendonça
	 *
	 */
	private Framework() {
		setTitle("LegendsHub");
		setIconImage(new ImageIcon("utility/icons/TESL.png").getImage());
		addComponents();
		checkRatingForAllCards();
		start();
	}

	private void addComponents(){
		addMenu();
		addSplitscreen();
	}

	private void addMenu() {
		add(MENUBAR,BorderLayout.NORTH);
	}

	private void addSplitscreen() {
		split = new Splitscreen(METADECKS,CONTROLPANEL,JSplitPane.HORIZONTAL_SPLIT,null);
		Splitscreen split2 = new Splitscreen(split,CRAFTMODEPANEL,JSplitPane.VERTICAL_SPLIT,null);
		
		HINTMESSAGES.makeThisAHintButton(Hub.getHintlist());
		Splitscreen splitscreen = new Splitscreen(HINTMESSAGES,split2,JSplitPane.VERTICAL_SPLIT,null);
		add(splitscreen,BorderLayout.CENTER);
	}
	
	private void checkRatingForAllCards(){
		for(Card card : Hub.getCardlist().values()) card.checkRating();
	}

	/**
	 * Initializes the framework. Since the components are already made when an instance of {@link #Framework()} is created,
	 *  this method adds the main JPanel to our JFrame, and sets it ready to be visualized and interacted with.
	 */
	private void start(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static void swap(boolean isCraftRoulette) {
		if(isCraftRoulette) {
			split.remove(METADECKS);
			split.add(CRAFTROULETTE);
		}
		else {
			split.remove(CRAFTROULETTE);
			split.add(METADECKS);
		}
	}
	
	public static Framework getInstance() {
		return FRAMEWORK;
	}
}
