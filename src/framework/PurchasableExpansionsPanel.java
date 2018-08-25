package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import hub.Hub;
import objects.Card;
import objects.CardType;
import resources.Alerts;
import resources.GridLayoutChooser;
import resources.ReadTXT;
import resources.RescaledImageIcon;
import resources.Splitscreen;

public class PurchasableExpansionsPanel extends JPanel{
	/*OBJECTS*/
	private static final Dimension ButtonDimension = new Dimension(128,256);
	private static final Dimension CardDimension = new Dimension(100,166);
	private static final HashMap<String,Boolean> PurchasableExpansionAcquiredState = new HashMap<String,Boolean>();
	private static final HashMap<String, CustomLabel> PurchasableExpansionAcquiredStateLabel = new HashMap<String, CustomLabel>();
	private static final HashMap<String, JButton> PurchasableExpansionButton = new HashMap<String, JButton>();
	private static final HashMap<String, String> PurchasableExpansionPath = new HashMap<String, String>();
	private static final HashMap<String, ImageIcon> PurchasableExpansion = new HashMap<String, ImageIcon>();
	/*INSTANCE*/
	private static final PurchasableExpansionsPanel INSTANCE = new PurchasableExpansionsPanel();

	public PurchasableExpansionsPanel() {
		init();
	}

	private void init() {
		File expansionsFolder = new File("utility/expansions/");
		ArrayList<File> Expansions = new ArrayList<File>();
		for(File file : expansionsFolder.listFiles()) Expansions.add(file);
		int numberOfLayoutRows = (int)Math.ceil(Expansions.size()/5.0);
		setLayout(new GridLayout(numberOfLayoutRows,1));
		addDecks(Expansions,numberOfLayoutRows);
	}

	private void addDecks(ArrayList<File> PremadeDecks, int LayoutRows) {
		int deckCounter = 0;
		for(int i=0;i<LayoutRows;i++) {
			JPanel IconPanel = new JPanel();
			IconPanel.setOpaque(false);
			for(int j=deckCounter;j<PremadeDecks.size();j++) {
				String path = PremadeDecks.get(j).getPath();
				ImageIcon thisExpansionButton = new RescaledImageIcon(path+"/button.png",ButtonDimension.width,ButtonDimension.height).getRESCALEDICON();
				ImageIcon thisExpansion = new ImageIcon(path+"/expansion.png");
				JButton expansionButton = new JButton(thisExpansionButton);
				expansionButton = makePretty(expansionButton);
				String expansion = path.split("\\\\")[path.split("\\\\").length-1];
				PurchasableExpansionButton.put(expansion, expansionButton);
				PurchasableExpansion.put(expansion, thisExpansion);
				Splitscreen splitscreen = new Splitscreen(expansionButton,getAcquisitionStatus(path),JSplitPane.VERTICAL_SPLIT,null);
				IconPanel.add(splitscreen);
				add(IconPanel);
				deckCounter++;
				if(deckCounter%5==0) break;
			}
		}
	}

	private CustomLabel getAcquisitionStatus(String path) {
		CustomLabel acquired = new CustomLabel("Not Purchased",true);
		acquired.setForeground(Color.GRAY);
		
		String expansion = path.split("\\\\")[path.split("\\\\").length-1];
		PurchasableExpansionAcquiredState.put(expansion,false);
		PurchasableExpansionAcquiredStateLabel.put(expansion, acquired);
		PurchasableExpansionPath.put(expansion, path);
		return acquired;
	}

	public static Dimension whatFrameDimensionToUse() {
		int x = 870;
		int y = 760;
		return new Dimension(x,y);
	}
	
	public static PurchasableExpansionsPanel getInstance() {
		return INSTANCE;
	}

	/**
	 * Updates the state of an Expansion to true(Acquired) or false(Not Acquired) depending on the user's collection. If he has at least one card from the Expansion that means he already bought
	 * the expansion. This is because until you buy these expansions, it's impossible for you to craft these cards anyway.
	 */
	public static void update() {
		for(String expansion : PurchasableExpansionPath.keySet()) {
			String path = PurchasableExpansionPath.get(expansion);
			boolean isPurchased = true;
			try {
				HashMap<String, Integer> deck = new ReadTXT(path+"/"+expansion+".txt").getDeckContents();
				int CountIfACardBelongsToTheExpansion = 0;
				for(String key : deck.keySet()) {
					if(!Hub.getCollection().containsKey(key)) System.out.println("Key Missing in Cardlist: "+key+" from deck "+expansion);
					Card card = Hub.getCollection().get(key);
					if(card.getCopies()>0) CountIfACardBelongsToTheExpansion++;
				}
				if(CountIfACardBelongsToTheExpansion==0) isPurchased=false; //You can't have expansions cards if you didn't buy that expansion.
			} catch (IOException e) {Alerts.launchException(e.getMessage());}
			if(isPurchased) updateAcquiredState(expansion, true);
			else updateAcquiredState(expansion, false);
		}
	}

	/**
	 * Updates the state of an Expansion to true(Acquired) or false(Not Acquired).
	 * @param expansion - The expansion to which the new state applies.
	 * @param newState - The new state of the acquisiton.
	 */
	public static void updateAcquiredState(String expansion, boolean newState) {
		if(!PurchasableExpansionAcquiredStateLabel.containsKey(expansion)) Alerts.noExpansionWithThatName(expansion);
		PurchasableExpansionAcquiredState.put(expansion,newState);
		updateAcquiredStateLabel(expansion);
	}

	/**
	 * Updates the JLabel related to this expansion.
	 * @param expansion - The expansion to update
	 */
	private static void updateAcquiredStateLabel(String expansion) {
		CustomLabel expansionState = PurchasableExpansionAcquiredStateLabel.get(expansion);
		if(PurchasableExpansionAcquiredState.get(expansion)) {
			expansionState.setText("Purchased");
			expansionState.setForeground(Color.BLUE);
		}
		else {
			expansionState.setText("Not Purchased");
			expansionState.setForeground(Color.GRAY);
		}
	}

	private static JButton makePretty(JButton button){
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		return button;
	}
	
	public static JPanel openExpansion(String expansion) {
		JPanel OpenExpansionPanel = new JPanel(new FlowLayout());
		try {
			HashMap<String, Integer> deck = new ReadTXT(PurchasableExpansionPath.get(expansion)+"/"+expansion+".txt").getDeckContents();
			JPanel ExpansionCards = new JPanel(GridLayoutChooser.generateAppropriatePurchasableLabelGridLayout(deck.size()));
			for(String cardname : deck.keySet()) {
				ImageIcon cardDisplay = new RescaledImageIcon(Hub.getCardlist().get(cardname).getPathToImage(),CardDimension.width,CardDimension.height).getRESCALEDICON();
				ExpansionCards.add(new JLabel(cardDisplay));
			}
			OpenExpansionPanel.add(ExpansionCards);
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
		return OpenExpansionPanel;
	}
	
	public static HashMap<String, CustomLabel> getPurchasableExpansionAcquiredStateLabels() {
		return PurchasableExpansionAcquiredStateLabel;
	}

	public static HashMap<String, Boolean> getPurchasableExpansionAcquiredStates() {
		return PurchasableExpansionAcquiredState;
	}

	public static HashMap<String, JButton> getPurchasableExpansionButtons() {
		return PurchasableExpansionButton;
	}

	public static HashMap<String, String> getPurchasableExpansionPath() {
		return PurchasableExpansionPath;
	}

	public static HashMap<String, ImageIcon> getPurchasableExpansion() {
		return PurchasableExpansion;
	}
}
