package framework;

import hub.Hub;
import objects.CardType;
import objects.Deck;
import resources.Alerts;
import resources.ReadTXT;
import resources.RescaledImageIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MetaDecksPanel extends CustomPanel{
	/*FOLDERS*/
	private static final String[] MonthOrder = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	private static final LinkedHashMap<String, File[]> YEAR_SEASONS = new LinkedHashMap<String, File[]>();
	private static final LinkedHashMap<String, File[]> SEASON_TIERS = new LinkedHashMap<String, File[]>();
	private static final LinkedHashMap<String, File[]> TIERS_DECKS = new LinkedHashMap<String, File[]>();
	/*JPANELS*/
	private static final LinkedHashMap<String,JPanel> YEARS = new LinkedHashMap<String,JPanel>();
	private static final LinkedHashMap<String,JPanel> SEASONS = new LinkedHashMap<String,JPanel>();
	private static final LinkedHashMap<String,JPanel> DECKS = new LinkedHashMap<String,JPanel>();
	private static final LinkedHashMap<JPanel,CustomButton> PANEL_DECKBUTTON = new LinkedHashMap<JPanel,CustomButton>();
	/*GRIDBAGLAYOUT CONSTRAINTS*/
	private static final GridBagConstraints TITLE_CONSTRAINTS = getGridBagTitleConstraints();
	private static final GridBagConstraints LIST_CONSTRAINTS = getGridBagListConstraints();
	private static final GridBagConstraints GO_BACK_CONSTRAINTS = getGridBagBackButtonConstraints();
	/*METADECKS*/
	private static final LinkedHashMap<String,Deck> METADECKS = new LinkedHashMap<String,Deck>();
	/*INSTANCE*/
	private static final MetaDecksPanel INSTANCE = new MetaDecksPanel();

	/**
	 * Creates Years. For each Year creates Seasons. For each Season Creates the Decks, divided by Tier 1 and Tier 2. For each Deck creates the Deck page.
	 */
	private MetaDecksPanel() {
		createYearList();
		for(String YEAR : YEARS.keySet()) {
			createSeasonList(YEAR);
			for(String SEASON : SEASONS.keySet()) {
				if(SEASON.contains(YEAR)) {
					SEASON = SEASON.split(" ")[1];
					createDeckList(YEAR, SEASON);
					File[] tier1Decks = TIERS_DECKS.get(YEAR + " " + SEASON + " Tier 1");
					if (tier1Decks != null) {
						for (File deck : tier1Decks){
							createDeck("utility/decks/meta/" + YEAR + "/" + SEASON + "/Tier 1/" + deck.getName());
						}
					}
					else {
						System.out.println("No decks found for key: " + YEAR + " " + SEASON + " Tier 1");
					}
					File[] tier2Decks = TIERS_DECKS.get(YEAR + " " + SEASON + " Tier 2");
					if (tier2Decks != null) {
						for (File deck : tier2Decks){
							createDeck("utility/decks/meta/" + YEAR + "/" + SEASON + "/Tier 2/" + deck.getName());
						}
					}
					else {
						System.out.println("No decks found for key: " + YEAR + " " + SEASON + " Tier 2");
					}
				}
			}
		}
		setPreferredSize(Framework.CARD_PANEL_DIMENSION);
		start();
	}

	private void createYearList() {
		/*READ FOLDER*/
		File meta = new File("utility/decks/meta/");
		File[] years = meta.listFiles();
		for(File year : years) YEAR_SEASONS.put(year.getName(), year.listFiles());  //Add an Year and its 12 Seasons
		/*CREATE YEAR LIST*/
		for(int i=0;i<years.length;i++) {
			JPanel newPanel = createJPanel(0, years[i].getName(), null, null, null);
			YEARS.put(years[i].getName(),newPanel);
		}
	}

	private void createSeasonList(String Year) {
		/*READ FOLDER*/
		File year = new File("utility/decks/meta/"+Year);
		File[] seasons = year.listFiles();
		for(File season : seasons) SEASON_TIERS.put(Year+" "+season.getName(), season.listFiles());  //Add a Season and its Tier 1/Tier 2 decks.
		/*CREATE SEASON LIST*/
		for(int i=0;i<seasons.length;i++) {
			JPanel newPanel = createJPanel(1, Year, seasons[i].getName(), null, null);
			SEASONS.put(Year+" "+seasons[i].getName(),newPanel);
		}
		sortSeasons(Year);
	}

	private void createDeckList(String Year,String Season) {
		/*READ FOLDER*/
		File season = new File("utility/decks/meta/"+Year+"/"+Season+"/");
		File[] tiers = season.listFiles();
		if(tiers != null){
			for(File tier : tiers) {
				TIERS_DECKS.put(Year+" "+Season+" "+tier.getName(), tier.listFiles());  //Adds all the decks for each tier
			}
			/*TIER DECKLIST*/
			String tier1 = Year+" "+Season+" Tier 1";
			String tier2 = Year+" "+Season+" Tier 2";
			if(TIERS_DECKS.containsKey(tier1)){
				for(int i=0;i<TIERS_DECKS.get(tier1).length;i++) {
					JPanel newPanel = createJPanel(2, Year, Season,"Tier 1", TIERS_DECKS.get(tier1)[i].getName());
					DECKS.put(Year+" "+Season+" Tier 1 "+TIERS_DECKS.get(tier1)[i].getName(),newPanel);
				}
			}
			if(TIERS_DECKS.containsKey(tier2)) {
				for(int i=0;i<TIERS_DECKS.get(tier2).length;i++) {
					JPanel newPanel = createJPanel(2, Year, Season,"Tier 2", TIERS_DECKS.get(tier2)[i].getName());
					DECKS.put(Year+" "+Season+" Tier 2 "+TIERS_DECKS.get(tier2)[i].getName(),newPanel);
				}
			}
		}
	}

	private void createDeck(String pathToDeck) {
		Deck thisDeck = new Deck(pathToDeck);
		METADECKS.put(pathToDeck, thisDeck);
	}

	/**
	 * <p>JPanel Type 0 - Year: Implies a single JButton directing to the SeasonList.
	 * <p>JPanel Type 1 - Season: Implies a single JButton linking to the DeckList of the Season.
	 * <p>JPanel Type 2 - DeckList: Implies a JButton linking to a deck, with deck class description.
	 * @param JPanelType - Type of JPanel you want to create [0,1,2].
	 * @param Year - Year for the JPanel whenn applicable
	 * @param Season - Season for the JPanel whenn applicable
	 * @param Tier - Deck Tier when applicable
	 * @param Deck - Meta Deck when applicable
	 * @return A JPanel obeying to the type structure.
	 */
	private JPanel createJPanel(int JPanelType, String Year, String Season, String Tier, String Deck){
		JPanel newPanel = new JPanel(new BorderLayout());
		newPanel.setOpaque(false);
		if(JPanelType==0) {
			CustomButton clickForSeasons = new CustomButton(String.valueOf(Year),true);
			clickForSeasons.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					openSeasonList(Year);
					validate();
					repaint();
				}
			});
			newPanel.add(clickForSeasons);
		}
		else if(JPanelType==1) {
			CustomButton clickForDecks = new CustomButton(Season,true);
			clickForDecks.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					openDeckList(Year,Season);
					validate();
					repaint();
				}
			});
			newPanel.add(clickForDecks);
		}
		else if(JPanelType==2) {
			CustomButton clickForDeck = new CustomButton(Deck.split("\\.")[0],true);
			String pathToDeck = "utility/decks/meta/"+Year+"/"+Season+"/"+Tier+"/"+Deck;
			clickForDeck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					openDeck(pathToDeck);
					validate();
					repaint();
				}
			});
			clickForDeck.setIcon(new RescaledImageIcon("utility/icons/"+Deck.split(" ")[1].split("\\.")[0]+".png", 60, 20).getRESCALEDICON());
			PANEL_DECKBUTTON.put(newPanel, clickForDeck);
			newPanel.add(clickForDeck,BorderLayout.NORTH);
		}
		return newPanel;
	}

	private void start() {
		openYearList();
	}

	private void openYearList() {
		removeAll();
		setLayout(new GridBagLayout());
		/*TITLE*/
		CustomLabel title = new CustomLabel("Years",50,true);
		add(title,TITLE_CONSTRAINTS);
		/*YEAR LIST*/
		JPanel YearList = new JPanel(new GridLayout(YEARS.keySet().size(),1,0,10));
		YearList.setBorder(null);
		YearList.setOpaque(false);
		for(String YEAR : YEARS.keySet()) {
			YearList.add(YEARS.get(YEAR));
		}
		add(YearList,LIST_CONSTRAINTS);
	}

	private void openSeasonList(String Year) {
		removeAll();
		setLayout(new GridBagLayout());
		/*TITLE*/
		CustomLabel title = new CustomLabel(Year+" Seasons",50,true);
		add(title,TITLE_CONSTRAINTS);
		/*SEASON LIST*/
		JPanel SeasonList = new JPanel(new GridLayout(YEAR_SEASONS.get(Year).length,1,0,10));
		SeasonList.setBorder(null);
		SeasonList.setOpaque(false);
		for(String season : SEASONS.keySet()) {
			if(season.contains(Year)){
				var seasonPanel = SEASONS.get(season);
				if(seasonPanel != null) {
					SeasonList.add(seasonPanel);
				}
			}
		}
		add(SeasonList,LIST_CONSTRAINTS);
		/*GO BACK BUTTON*/
		CustomButton goBack = new CustomButton("Go Back", false);
		goBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openYearList();
				validate();
				repaint();
			}
		});
		add(goBack,GO_BACK_CONSTRAINTS);
	}

	private void openDeckList(String Year, String Season) {
		removeAll();
		setLayout(new GridBagLayout());
		/*TITLE*/
		CustomLabel title = new CustomLabel(Season+" "+Year+" Decks",35,true);
		add(title,TITLE_CONSTRAINTS);
		/*DECK LIST*/
		int GridLayoutSize = TIERS_DECKS.get(Year+" "+Season+" Tier 1").length+TIERS_DECKS.get(Year+" "+Season+" Tier 2").length+2; //2 for the Titles (Tier 1 and Tier 2)
		JPanel DeckList = new JPanel(new GridLayout(GridLayoutSize,1,0,10));
		DeckList.setBorder(null);
		DeckList.setOpaque(false);
		CustomLabel tiertitle = new CustomLabel("Tier 1",30,true);
		DeckList.add(tiertitle);
		for(String deck : DECKS.keySet()) {
			if(deck.contains(Year+" "+Season+" Tier 1")) {
				String deckPath = deck.split(" ")[4]+" "+deck.split(" ")[5];
				String pathToDeck = "utility/decks/meta/"+Year+"/"+Season+"/Tier 1/"+deckPath;
				if(isPossibleToBuildDeck(pathToDeck)) PANEL_DECKBUTTON.get(DECKS.get(deck)).setPossibleToBuildDeckButton(true);   //Changes the color of the button if you can make the deck
				else PANEL_DECKBUTTON.get(DECKS.get(deck)).setPossibleToBuildDeckButton(false);
				DeckList.add(DECKS.get(deck));
			}
			
		}
		tiertitle = new CustomLabel("Tier 2",30,true);
		DeckList.add(tiertitle);
		for(String deck : DECKS.keySet()) {
			if(deck.contains(Year+" "+Season+" Tier 2")) {
				String deckPath = deck.split(" ")[4]+" "+deck.split(" ")[5];
				String pathToDeck = "utility/decks/meta/"+Year+"/"+Season+"/Tier 2/"+deckPath;
				if(isPossibleToBuildDeck(pathToDeck)) PANEL_DECKBUTTON.get(DECKS.get(deck)).setPossibleToBuildDeckButton(true);  //Changes the color of the button if you can make the deck
				else PANEL_DECKBUTTON.get(DECKS.get(deck)).setPossibleToBuildDeckButton(false);
				DeckList.add(DECKS.get(deck));
			}
		}
		add(DeckList,LIST_CONSTRAINTS);
		/*GO BACK BUTTON*/
		CustomButton goBack = new CustomButton("Go Back", false);
		goBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSeasonList(Year);
				validate();
				repaint();
			}
		});
		add(goBack,GO_BACK_CONSTRAINTS);
	}

	private void openDeck(String pathToDeck) {
		removeAll();
		setLayout(new BorderLayout());
		/*DECK*/
		Deck thisDeck = METADECKS.get(pathToDeck);
		System.out.println(thisDeck.getName());
		/*NORTH TITLE*/
		JPanel NORTH = new JPanel(new GridLayout(1,2));
		JPanel Title = new JPanel(new GridLayout(2,1));
		JPanel Rest = new JPanel(new GridLayout(2,3));
		NORTH.setBackground(new Color(0,0,100,50));
		Title.setOpaque(false);
		Rest.setOpaque(false);
		NORTH.add(Title);
		NORTH.add(Rest);
		Title.add(new CustomLabel("Deck Name",true));
		Rest.add(new CustomLabel("Tier",true));
		Rest.add(new CustomLabel("Season",true));
		Rest.add(new CustomLabel("Year",true));
		Title.add(new CustomLabel(thisDeck.getName(),15,true));
		Rest.add(new CustomLabel(thisDeck.getTier(),15,true));
		Rest.add(new CustomLabel(thisDeck.getSeason(),15,true));
		Rest.add(new CustomLabel(thisDeck.getYear(),15,true));
		add(NORTH,BorderLayout.NORTH);		
		/*CENTER CARDLIST*/
		JPanel DeckPanel = new JPanel(new BorderLayout());
		DeckPanel.setOpaque(false);
		CustomLabel MissingSoulGems = new CustomLabel("Soul Gems Missing: "+thisDeck.getMissingSoulGems(),25,true);
		MissingSoulGems.setHorizontalTextPosition(SwingConstants.LEFT);
		MissingSoulGems.setIcon(new RescaledImageIcon("utility/icons/soulgem.png",50,50).getRESCALEDICON());
		DeckPanel.add(MissingSoulGems,BorderLayout.NORTH);
		JPanel Cards = new JPanel(new GridLayout(thisDeck.getCARDS().keySet().size()/2+1,2));
		Cards.setBackground(new Color(200,200,255,150));
		for(String cardname : thisDeck.getCARDS().keySet()) {
			CustomLabel card = new CustomLabel(thisDeck.getCARDS().get(cardname)+"x "+cardname,12,false);
			if(Hub.getCardlist().containsKey(cardname)) {
				if(Hub.getCardlist().get(cardname).getType().equals(CardType.UNIQUELEGENDARY)) card.setIcon(new RescaledImageIcon("utility/icons/LEGENDARY.png",25,25).getRESCALEDICON());
				else card.setIcon(new RescaledImageIcon("utility/icons/"+Hub.getCardlist().get(cardname).getType().name()+".png",25,25).getRESCALEDICON());
				if(thisDeck.getMissingCards().containsKey(cardname)) card.setText("<html><strike>"+card.getText()+"</strike></html>");
			}
			card.addMouseListener(new MouseListener() {
				JDialog dialog;
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				@Override
				public void mousePressed(MouseEvent e) {
				}
				@Override
				public void mouseExited(MouseEvent e) {
					dialog.dispose();
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					dialog = new JDialog(Framework.getInstance());
					dialog.setTitle(cardname);
					dialog.add(new JLabel(new ImageIcon(Hub.getCardlist().get(cardname).getPathToImage())));
					dialog.pack();
					dialog.setVisible(true);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			Cards.add(card);
		}
		DeckPanel.add(Cards);
		add(DeckPanel);
		/*SOUTH GO BACK BUTTON*/
		CustomButton goBack = new CustomButton("Go Back", false);
		goBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openDeckList(thisDeck.getYear(), thisDeck.getSeason());
				validate();
				repaint();
			}
		});
		add(goBack,BorderLayout.SOUTH);
	}

	private static GridBagConstraints getGridBagTitleConstraints() {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.weightx=0;
		constraint.weighty=0;
		constraint.anchor = GridBagConstraints.NORTH;
		return constraint;
	}

	private static GridBagConstraints getGridBagListConstraints() {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.weightx=1;
		constraint.weighty=1;
		constraint.gridy=1;
		constraint.fill=GridBagConstraints.HORIZONTAL;
		constraint.anchor = GridBagConstraints.NORTH;
		return constraint;
	}

	private static GridBagConstraints getGridBagBackButtonConstraints() {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.weightx=0;
		constraint.weighty=0;
		constraint.gridy=2;
		constraint.anchor = GridBagConstraints.SOUTH;
		return constraint;
	}

	private void sortSeasons(String Year) {
		for(String month : MonthOrder) {
			JPanel panel = SEASONS.get(Year+" "+month);
			SEASONS.remove(Year+" "+month);
			SEASONS.put(Year+" "+month,panel);
		}
	}

	private boolean isPossibleToBuildDeck(String pathToDeck){
		boolean isPossible = true;
		try {
			LinkedHashMap<String,Integer> deck = new ReadTXT(pathToDeck).getDeckContents();
			for(String cardname : deck.keySet()) {
				if(Hub.getCollection().containsKey(cardname)&&(Hub.getCollection().get(cardname).getCopies()<deck.get(cardname))) isPossible=false;
			}
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
		return isPossible;
	}
	
	public static LinkedHashMap<String, JPanel> getSeasons() {
		return SEASONS;
	}

	public static LinkedHashMap<String, Deck> getMetaDecks() {
		return METADECKS;
	}

	public static MetaDecksPanel getInstance() {
		return INSTANCE;
	}
	
	public static String[] getMonthOrder() {
		return MonthOrder;
	}
}
