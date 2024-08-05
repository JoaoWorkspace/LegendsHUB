package framework;

import hub.Hub;
import objects.Card;
import resources.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PurchasableDecksPanel extends  JPanel{
	/*OBJECTS*/
	private static final Dimension ButtonDimension = new Dimension(128,256);
	private static final Dimension CardDimension = new Dimension(100,166);
	private static final HashMap<String,Boolean> PurchasableDeckAcquiredState = new HashMap<String,Boolean>();
	private static final HashMap<String, CustomLabel> PurchasableDeckAcquiredStateLabel = new HashMap<String, CustomLabel>();
	private static final HashMap<String, JButton> PurchasableDeckButton = new HashMap<String, JButton>();
	private static final HashMap<String, String> PurchasableDeckPath = new HashMap<String, String>();
	private static final HashMap<String, ImageIcon> PurchasableDeck = new HashMap<String, ImageIcon>();
	/*INSTANCE*/
	private static final PurchasableDecksPanel INSTANCE = new PurchasableDecksPanel();

	public PurchasableDecksPanel() {
		init();
	}

	private void init() {
		File premadeDecksFolder = new File("utility/decks/buy/");
		ArrayList<File> PremadeDecks = new ArrayList<File>();
		for(File deck : premadeDecksFolder.listFiles()) for(File file : deck.listFiles())PremadeDecks.add(file);
		int numberOfLayoutRows = (int)Math.ceil(PremadeDecks.size()/5.0);
		setLayout(new GridLayout(numberOfLayoutRows,1));
		addDecks(PremadeDecks,numberOfLayoutRows);
	}

	private void addDecks(ArrayList<File> PremadeDecks, int LayoutRows) {
		int deckCounter = 0;
		for(int i=0;i<LayoutRows;i++) {
			JPanel IconPanel = new JPanel();
			IconPanel.setOpaque(false);
			for(int j=deckCounter;j<PremadeDecks.size();j++) {
				String path = PremadeDecks.get(j).getPath();
				ImageIcon thisDeckButton = new RescaledImageIcon(path+"/button.png",ButtonDimension.width,ButtonDimension.height).getRESCALEDICON();
				ImageIcon thisDeck = new RescaledImageIcon(path+"/deck.png",ButtonDimension.width,ButtonDimension.height).getRESCALEDICON();
				JButton deckButton = new JButton(thisDeckButton);
				deckButton = makePretty(deckButton);
				String deckname = path.split("\\\\")[path.split("\\\\").length-1];
				PurchasableDeckButton.put(deckname, deckButton);
				PurchasableDeck.put(deckname, thisDeck);
				
				Splitscreen splitscreen = new Splitscreen(deckButton,getAcquisitionStatus(path),JSplitPane.VERTICAL_SPLIT,null);
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
		
		String deckname = path.split("\\\\")[path.split("\\\\").length-1];
		PurchasableDeckAcquiredState.put(deckname,false);
		PurchasableDeckAcquiredStateLabel.put(deckname, acquired);
		PurchasableDeckPath.put(deckname, path);
		return acquired;
	}

	public static Dimension whatFrameDimensionToUse() {
		int x = 870;
		int y = 760;
		return new Dimension(x,y);
	}

	public static PurchasableDecksPanel getInstance() {
		return INSTANCE;
	}

	
	/**
	 * Updates the state of a Deck to true(Acquired) or false(Not Acquired) depending on the user's collection. If he has all the copies for all the cards in a deck,
	 * it's for sure guaranteed he has bought/doesn't need this collection.
	 */
	public static void update() {
		for(String deckname : PurchasableDeckPath.keySet()) {
			String path = PurchasableDeckPath.get(deckname);
			boolean isPurchased = true;
			try {
				HashMap<String, Integer> deck = new ReadTXT(path+"/"+deckname+".txt").getDeckContents();
				for(String key : deck.keySet()) {
					if(!Hub.getCollection().containsKey(key)) System.out.println("Key Missing: "+key+" in deck "+deckname);
					Card card = Hub.getCollection().get(key);
					if(card.getMissing()!=0) isPurchased = false;
				}
			} catch (IOException e) {Alerts.launchException(e.getMessage());}
			if(isPurchased) updateAcquiredState(deckname, true);
			else updateAcquiredState(deckname, false);
		}
	}

	/**
	 * Updates the state of a Deck to true(Acquired) or false(Not Acquired).
	 * @param deckname - The deck to which the new state applies.
	 * @param newState - The new state of the acquisiton.
	 */
	public static void updateAcquiredState(String deckname, boolean newState) {
		if(!PurchasableDeckAcquiredStateLabel.containsKey(deckname)) Alerts.noDeckWithThatName(deckname);
		PurchasableDeckAcquiredState.put(deckname,newState);
		updateAcquiredStateLabel(deckname);
	}

	/**
	 * Updates the JLabel related to this Deck.
	 * @param deckname - The deck to update
	 */
	private static void updateAcquiredStateLabel(String deckname) {
		CustomLabel expansionState = PurchasableDeckAcquiredStateLabel.get(deckname);
		if(PurchasableDeckAcquiredState.get(deckname)) {
			expansionState.setText("Purchased");
			expansionState.setForeground(Color.BLUE);
		}
		else {
			expansionState.setText("Not Purchased");
			expansionState.setForeground(Color.GRAY);
		}
	}

	private JButton makePretty(JButton button){
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		return button;
	}
	
	public static JPanel openDeck(String deck) {
		JPanel OpenDeckPanel = new JPanel(new FlowLayout());
		try {
			HashMap<String, Integer> deckContent = new ReadTXT(PurchasableDeckPath.get(deck)+"/"+deck+".txt").getDeckContents();
			JPanel DeckCards = new JPanel(GridLayoutChooser.generateAppropriatePurchasableLabelGridLayout(deckContent.size()));
			for(String cardname : deckContent.keySet()) {
				ImageIcon cardDisplay = new RescaledImageIcon(Hub.getCardlist().get(cardname).getPathToImage(),CardDimension.width,CardDimension.height).getRESCALEDICON();
				DeckCards.add(new JLabel(cardDisplay));
			}
			OpenDeckPanel.add(DeckCards);
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
		return OpenDeckPanel;
	}

	public static HashMap<String, CustomLabel> getPurchasableDeckAcquiredStateLabels() {
		return PurchasableDeckAcquiredStateLabel;
	}

	public static HashMap<String, Boolean> getPurchasableDeckAcquiredStates() {
		return PurchasableDeckAcquiredState;
	}

	public static HashMap<String, JButton> getPurchasableDeckButtons() {
		return PurchasableDeckButton;
	}

	public static HashMap<String, String> getPurchasableDeckPath() {
		return PurchasableDeckPath;
	}

	public static HashMap<String, ImageIcon> getPurchasableDeck() {
		return PurchasableDeck;
	}
}
