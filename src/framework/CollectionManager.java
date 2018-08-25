package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import hub.Hub;
import javafx.scene.control.ComboBox;
import objects.Card;
import objects.CardType;
import resources.Alerts;
import resources.LineNumberingTextArea;
import resources.ReadTXT;
import resources.RescaledImageIcon;
import resources.UseTheLookAndFeelYouWant;

public class CollectionManager extends JFrame{
	/*Objects*/
	private static ArrayList<JDialog> dialogsToDispose = new ArrayList<JDialog>();
	private static final LinkedHashMap<String,Card> myCollection = Hub.getCollection();
	private static final LinkedHashMap<String,Card> cardlist = Hub.getCardlist();
	private static final PurchasableDecksPanel PURCHASABLE_DECKS = PurchasableDecksPanel.getInstance();
	private static final PurchasableExpansionsPanel PURCHASABLE_EXPANSIONS = PurchasableExpansionsPanel.getInstance();
	private static CustomTable collection;
	/*Search Bar Variables*/
	private static int LastSearchedIndex = 0;
	private static boolean ThereWereResultsForThisSearch = false;
	/*INSTANCE*/
	private static final CollectionManager COLLECTIONMANAGER = new CollectionManager();

	/**
	 * <p>The Collection Manager is the fundamental tool that connects with the rest of the application, making sure that the more appropriate cards weight more than the rest.
	 * Also allows for some neat statistics about the user's collection such as:
	 * <ul>
	 * <li> Number of Legendaries
	 * <li> Number of Epics
	 * <li> Number of Rares
	 * <li> Number of Commons
	 * <ul>
	 * @author João Mendonça
	 * @param cardlist - Receives the list of all cards
	 */
	private CollectionManager() {
		setTitle("Collection Manager");
	}

	public HashMap<String,Card> getMyCollection() {
		return myCollection;
	}

	/**
	 * Uploads the collection from a copy-paste taken from UniversalDeckTracker by extesy.
	 * The format of the extesy collection follows the Pattern: <b>"1 Nahkriin, Dragon Priest (Set1 #1566)"</b>
	 * so we take the number of cards in your collection(<b>1</b>) and the name of the card(<b>Dragon Priest</b>), ignoring the set, and
	 * with that information we can pinpoint cards (since they all have different names) and reduce the number of copies from that card that are missing.
	 */
	public void uploadCollection(){
		getContentPane().removeAll();

		/*Creates JPanel STEP that guides the users through all the steps requires to work with this functionality*/
		JPanel STEP = new JPanel(new GridLayout(5,1));
		JLabel step1 = new JLabel(" Step 1: Open the UniversalDeckTracker by extesy");
		JLabel step2 = new JLabel(" Step 2: Open The Elder Scroll Legends game in parallel");
		JLabel step3 = new JLabel(" Step 3: Scroll through your collection a bit");
		JLabel step4 = new JLabel(" Step 4: On the UniversalDeckTracker press <EXPORT FULL COLLECTION>");
		JLabel stepfinal = new JLabel(" Finally paste using [CTRL]+V inside the text area below, and press <UPLOAD>");
		STEP.add(step1);
		STEP.add(step2);
		STEP.add(step3);
		STEP.add(step4);
		STEP.add(stepfinal);

		/*Create JPanel of Paste with JLabel and Customized TextArea to fill with data from UniversalDeckTracker*/
		JPanel PASTE = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextArea pasteArea = new JTextArea(5,40);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		pasteArea.setBorder(BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		pasteArea.setBackground(Color.orange);
		JScrollPane scrollable = new JScrollPane(pasteArea);
		LineNumberingTextArea pastenumbers = new LineNumberingTextArea(pasteArea);
		scrollable.setRowHeaderView(pastenumbers);
		pasteArea.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent documentEvent)
			{
				pastenumbers.updateLineNumbers();
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent)
			{
				pastenumbers.updateLineNumbers();
			}

			@Override
			public void changedUpdate(DocumentEvent documentEvent)
			{
				pastenumbers.updateLineNumbers();
			}
		});
		JLabel info = new JLabel("Input: ");
		PASTE.add(info);
		PASTE.add(scrollable);
		PASTE.setAlignmentX(0);

		/*Create Button to upload the information given in text area to update my collection*/
		CustomButton UPLOAD = new CustomButton("UPLOAD",false);
		UPLOAD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LinkedHashMap<String,Card> editingCollection = new LinkedHashMap<String,Card>();
				for(String key : myCollection.keySet()) editingCollection.put(key, myCollection.get(key).getCopy());
				int cardsAffected = 0;
				int totalrows = 0;
				int row = 0;
				for (String line : pasteArea.getText().split("\\n")) {
					row++;
					String[] firstdivision = line.split(" \\(");
					String content = firstdivision[0];
					if(content.length()>2) {
						int ncards = Integer.parseInt(0 + content.substring(0, 1).replaceAll("\\D+",""));
						String cardname = String.valueOf(content.substring(2));
						/* Set the number of copies missing for that specific card, based on how many copies of it you have */
						if(editingCollection.containsKey(cardname)) {
							for(int i=0;i<ncards;i++) editingCollection.get(cardname).reduceMissing();
							cardsAffected++;
						}
						else {
							Alerts.IncorrectInput(row,content);
							return;
						}
					}
					else {
						Alerts.IncorrectInput(row, content);
						return;
					}
				}
				totalrows = row;
				if(cardsAffected==totalrows) {
					System.out.println("AFFECTED TOTAL");
					for(String key : myCollection.keySet()) myCollection.put(key, editingCollection.get(key));
				}
				updateData(ControlPanel.getInstance());
				PurchasableDecksPanel.update();
				PurchasableExpansionsPanel.update();
			}
		});

		/*Add and set all components/restrictions to the CollectionManager*/
		setLayout(new GridLayout(3,1));
		add(STEP);
		add(PASTE);
		add(UPLOAD);
		setTitle("Collection Manager");
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setState(NORMAL);
		setVisible(true);
	}

	/**
	 * Opens up a Table of your cards editable where you can see how many of each card you have and set it higher or lower
	 * @author João Mendonça
	 */
	public void editCollection(){
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		String[] columns = {"Card Name","Card Rarity","Set","Copies"};
		Object[][] data = new Object[myCollection.size()][columns.length];
		int row = 0;
		for(Card card : myCollection.values()) {
			data[row][0] = card.getName();    				//Card Name
			data[row][1] = card.getType().getImage();		//Card Type Image
			data[row][2] = card.getSet();					//Card Set
			data[row][3] = insertNumberOfCards(card);		//Number of Card Copies
			row++;
		}
		DefaultTableModel model = new DefaultTableModel(data, columns);
		collection = new CustomTable(model, dialogsToDispose);
		UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
		JScrollPane scrollable = new JScrollPane(collection);
		UseTheLookAndFeelYouWant.revert();
		add(scrollable);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				for(JDialog dialog : dialogsToDispose) dialog.dispose();
				super.windowClosed(e);
			}
		});

		add(createEditButtons(),BorderLayout.SOUTH);
		add(createSearchBar(),BorderLayout.NORTH);
		setResizable(false);
		pack();		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setState(NORMAL);
		setVisible(true);
	}
	
	/**
	 * The search bar allows to search for a card which contains the typed input in it's name. You can press the button or press ENTER. 
	 * If you keep pressing the button you'll get the following results until none are left and it cycles through it again.
	 * @return A Panel with the search bar and button.
	 */
	private JPanel createSearchBar() {
		JPanel search = new JPanel();
		CustomButton clickToSearch = new CustomButton("",false);
		UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
		JTextField typeHere= new JTextField(30);
		UseTheLookAndFeelYouWant.revert();
		/*Set TextField*/
		typeHere.setFont(new CustomFont(16));
		typeHere.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) clickToSearch.doClick();
				else{
					ThereWereResultsForThisSearch = false;
					LastSearchedIndex = 0;
				}
			}
		});
		/*Set Button*/
		clickToSearch.setIcon(new RescaledImageIcon("utility/icons/search.png",20,20).getRESCALEDICON());
		clickToSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searched = typeHere.getText();
				for(int i=LastSearchedIndex;i<collection.getRowCount();i++) {
					String cardName = (String)collection.getModel().getValueAt(i,0);
					if(cardName.contains(searched)) {
						collection.getSelectionModel().setSelectionInterval(i, i);
						collection.scrollRectToVisible(collection.getCellRect(i,0, true));
						if(i+1<collection.getRowCount()) LastSearchedIndex=i+1;  			//There's continuation to the list
						else LastSearchedIndex=0;                                			//Let's go back to the beginning of the cycle
						ThereWereResultsForThisSearch = true;
						break;
					}
					if(i==collection.getRowCount()-1) {
						LastSearchedIndex=0;       			 	  							//Means no result was found so let's go back to the beginning of the cycle
						if(ThereWereResultsForThisSearch) i=LastSearchedIndex;              //Searches for result.
					}
				}
			}
		});
		/*Add TextField and Button*/
		search.add(typeHere);
		search.add(clickToSearch);
		search.setAlignmentX(CENTER_ALIGNMENT);
		return search;
	}

	/**
	 * Parent mehtod: {@link #editCollection()}.
	 * Initializes the Table with the current collection information available (either by previous Edit, or by Upload).
	 * You can enter this as an Edit mode, which is accessible to all users, or as Classify mode which is only accessible to a very select group of people
	 */
	private static int insertNumberOfCards(Card c){
		int number = 0;
		if (c.getType().equals(CardType.UNIQUELEGENDARY)) number = 1-c.getMissing();
		else number = 3-c.getMissing();
		return number;
	}

	private JPanel createEditButtons() {
		JPanel buttonsPanel = new JPanel();

		JButton savebutton = new JButton("SAVE");
		savebutton.setIcon(CardCollectionManagementPanel.getSaveButton());
		savebutton.setFocusPainted(false);
		savebutton.setContentAreaFilled(false);
		savebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int row = 0;row<collection.getRowCount();row++) {
					String cardname = (String) collection.getValueAt(row, 0); //Returns the card name so it can be searched on the cardlist
					int ncopies = (int)collection.getValueAt(row, 3); //Returns the number of copies of the card you set
					myCollection.get(cardname).resetMissing();
					for(int i=0;i<ncopies;i++) myCollection.get(cardname).reduceMissing();
				}
				updateData(ControlPanel.getInstance());
				PurchasableDecksPanel.update();
				PurchasableExpansionsPanel.update();
			}
		});

		JButton completebutton = new JButton("COMPLETE ALL");
		completebutton.setIcon(CardCollectionManagementPanel.getSelectAllButton());
		completebutton.setFocusPainted(false);
		completebutton.setContentAreaFilled(false);
		completebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int row = 0;row<collection.getRowCount();row++) {
					Card inCurrentRow = cardlist.get((String.valueOf(collection.getValueAt(row, 0))));
					if(inCurrentRow.getType().equals(CardType.UNIQUELEGENDARY)) collection.setValueAt(1, row, 3);
					else collection.setValueAt(3, row, 3);
				}
			}
		});

		JButton removebutton = new JButton("REMOVE ALL");
		removebutton.setIcon(CardCollectionManagementPanel.getDeselectAllButton());
		removebutton.setFocusPainted(false);
		removebutton.setContentAreaFilled(false);
		removebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int row = 0;row<collection.getRowCount();row++) {
					collection.setValueAt(0, row, 3);
				}
			}
		});

		buttonsPanel.add(savebutton);
		buttonsPanel.add(completebutton);
		buttonsPanel.add(removebutton);
		return buttonsPanel;
	}

	public void importCollection(String pathToCollection) {
		try {
			LinkedHashMap<String,Card> editingCollection = new LinkedHashMap<String,Card>();
			editingCollection = new ReadTXT(pathToCollection).readCollectionCards();
			for(String key : myCollection.keySet()) myCollection.put(key, editingCollection.get(key));
		} catch (IOException e) {
			Alerts.launchException(e.getMessage());
		}
		updateData(ControlPanel.getInstance());
	}

	public void saveCollection(String pathToCollection) {
		try {
			new ReadTXT(pathToCollection).saveCollectionCards();
		} catch (IOException e) {
			Alerts.launchException(e.getMessage());
		}
	}

	public void managePurchasableDecks() {
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		JLabel title = new JLabel("Premade Decks");
		title.setFont(new Font("Cambria",Font.BOLD,100));
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.NORTH);

		UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
		JScrollPane scrollThoseDecks = new JScrollPane(PURCHASABLE_DECKS);
		UseTheLookAndFeelYouWant.revert();
		add(scrollThoseDecks);

		for(String deck : PurchasableDecksPanel.getPurchasableDeckButtons().keySet()) {
			PurchasableDecksPanel.getPurchasableDeckButtons().get(deck).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getContentPane().removeAll();
					UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
					JScrollPane scrollThoseDecks = new JScrollPane(PurchasableDecksPanel.openDeck(deck));
					UseTheLookAndFeelYouWant.revert();
					add(scrollThoseDecks);

					JLabel title = new JLabel(deck);
					title.setFont(new Font("Cambria",Font.BOLD,50));
					title.setHorizontalAlignment(JLabel.CENTER);
					add(title,BorderLayout.NORTH);
					add(BuyBackButtons(deck),BorderLayout.SOUTH);
					setVisible(true);
				}
			});
		}

		setPreferredSize(PurchasableDecksPanel.whatFrameDimensionToUse());
		setResizable(false);
		pack();		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setState(NORMAL);
		setVisible(true);
		setPreferredSize(null);
	}

	public void managePurchasableExpansions() {
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		JLabel title = new JLabel("Expansions");
		title.setFont(new Font("Cambria",Font.BOLD,100));
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.NORTH);

		UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
		JScrollPane scrollThoseExpansions = new JScrollPane(PURCHASABLE_EXPANSIONS);
		UseTheLookAndFeelYouWant.revert();
		add(scrollThoseExpansions);

		for(String expansion : PurchasableExpansionsPanel.getPurchasableExpansionButtons().keySet()) {
			PurchasableExpansionsPanel.getPurchasableExpansionButtons().get(expansion).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getContentPane().removeAll();
					UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
					JScrollPane scrollThoseExpansions = new JScrollPane(PurchasableExpansionsPanel.openExpansion(expansion));
					UseTheLookAndFeelYouWant.revert();
					add(scrollThoseExpansions);

					JLabel title = new JLabel(expansion);
					title.setFont(new Font("Cambria",Font.BOLD,50));
					title.setHorizontalAlignment(JLabel.CENTER);
					add(title,BorderLayout.NORTH);
					add(BuyBackButtons(expansion),BorderLayout.SOUTH);
					setVisible(true);
				}
			});
		}

		setPreferredSize(PurchasableExpansionsPanel.whatFrameDimensionToUse());
		setResizable(false);
		pack();		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setState(NORMAL);
		setVisible(true);
		setPreferredSize(null);
	}

	public static JPanel BuyBackButtons(String ExpansionOrDeck) {
		JPanel BuyBackButtons = new JPanel();
		JButton SetPurchasedButton = new CustomButton("Set as Purchased",false);
		JButton BuyButton = new CustomButton("Buy",false);
		JButton BackButton = new CustomButton("Back",false);
		SetPurchasedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(PurchasableDecksPanel.getPurchasableDeckAcquiredStates().containsKey(ExpansionOrDeck)) {
					PurchasableDecksPanel.updateAcquiredState(ExpansionOrDeck, true);
					BuyButton.setEnabled(false);
				}
				if(PurchasableExpansionsPanel.getPurchasableExpansionAcquiredStates().containsKey(ExpansionOrDeck)) {
					PurchasableExpansionsPanel.updateAcquiredState(ExpansionOrDeck,true);
					BuyButton.setEnabled(false);
				}
			}
		});
		BuyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BuyButton.setEnabled(false);
				if(PurchasableDecksPanel.getPurchasableDeck().containsKey(ExpansionOrDeck)) {
					String path = PurchasableDecksPanel.getPurchasableDeckPath().get(ExpansionOrDeck);
					try {
						HashMap<String, Integer> deck = new ReadTXT(path+"/"+ExpansionOrDeck+".txt").getDeckContents();
						for(String card : deck.keySet()) {
							for(int i=0;i<deck.get(card);i++) myCollection.get(card).reduceMissing();
						}
					} catch (IOException e1) {Alerts.launchException(e1.getMessage());}
					finally{PurchasableDecksPanel.updateAcquiredState(ExpansionOrDeck, true);}
				}
				if(PurchasableExpansionsPanel.getPurchasableExpansion().containsKey(ExpansionOrDeck)) {
					String path = PurchasableExpansionsPanel.getPurchasableExpansionPath().get(ExpansionOrDeck);
					try {
						HashMap<String, Integer> deck = new ReadTXT(path+"/"+ExpansionOrDeck+".txt").getDeckContents();
						for(String card : deck.keySet()) {
							for(int i=0;i<deck.get(card);i++) myCollection.get(card).reduceMissing();
						}
					} catch (IOException e1) {Alerts.launchException(e1.getMessage());}
					finally{PurchasableExpansionsPanel.updateAcquiredState(ExpansionOrDeck, true);}
				}
				CollectionManager.getInstance().updateData(ControlPanel.getInstance());
				
			}
		});
		BackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(PurchasableDecksPanel.getPurchasableDeckAcquiredStates().containsKey(ExpansionOrDeck)) CollectionManager.getInstance().managePurchasableDecks();
				if(PurchasableExpansionsPanel.getPurchasableExpansionAcquiredStates().containsKey(ExpansionOrDeck)) CollectionManager.getInstance().managePurchasableExpansions();
			}
		});
		if(PurchasableDecksPanel.getPurchasableDeckAcquiredStates().containsKey(ExpansionOrDeck)
				&&PurchasableDecksPanel.getPurchasableDeckAcquiredStates().get(ExpansionOrDeck)
				||PurchasableExpansionsPanel.getPurchasableExpansionAcquiredStates().containsKey(ExpansionOrDeck)
				&&PurchasableExpansionsPanel.getPurchasableExpansionAcquiredStates().get(ExpansionOrDeck)) {
			BuyButton.setEnabled(false);
		}
		BuyBackButtons.add(SetPurchasedButton);
		BuyBackButtons.add(BuyButton);
		BuyBackButtons.add(BackButton);
		return BuyBackButtons;
	}


	/**
	 * Updates the total amount of soul gems worth of collection the user has, and will set the value to the JProgressBar existing in the main Framework
	 * @param controlpanel - Father of all frameworks
	 */
	private void updateData(ControlPanel controlpanel) {
		int yourCardsSoulGemCost = 0;
		int yourLegendaryCardsSoulGemCost = 0;
		int yourEpicCardsSoulGemCost = 0;
		int yourRareCardsSoulGemCost = 0;
		int yourCommonCardsSoulGemCost = 0;
		for(Card card : myCollection.values()) {
			switch(card.getType()) {
			case UNIQUELEGENDARY: 
				yourCardsSoulGemCost += 1200*(1-card.getMissing());
				yourLegendaryCardsSoulGemCost += 1200*(1-card.getMissing());
				break;
			case LEGENDARY:
				yourCardsSoulGemCost += 1200*(3-card.getMissing());
				yourLegendaryCardsSoulGemCost += 1200*(3-card.getMissing());
				break;
			case EPIC:
				yourCardsSoulGemCost += 400*(3-card.getMissing());
				yourEpicCardsSoulGemCost += 400*(3-card.getMissing());
				break;
			case RARE:
				yourCardsSoulGemCost += 100*(3-card.getMissing());
				yourRareCardsSoulGemCost += 100*(3-card.getMissing());
				break;
			case COMMON:
				yourCardsSoulGemCost += 50*(3-card.getMissing());
				yourCommonCardsSoulGemCost += 50*(3-card.getMissing());
			}
		}
		/*Updates the FullCollectionProgressBar*/
		ControlPanel.FullCollectionProgress.getProgressBar().setValue(yourCardsSoulGemCost);
		ControlPanel.FullCollectionProgress.getSoulgem().setText(yourCardsSoulGemCost+"/"+ControlPanel.FullCollectionProgress.getProgressBar().getMaximum()+" Souls");
		/*Updates the LegendaryProgressBar*/
		ControlPanel.LegendaryCollectionProgress.getProgressBar().setValue(yourLegendaryCardsSoulGemCost);
		ControlPanel.LegendaryCollectionProgress.getSoulgem().setText(yourLegendaryCardsSoulGemCost+"/"+ControlPanel.LegendaryCollectionProgress.getProgressBar().getMaximum()+" Souls");
		/*Updates the EpicProgressBar*/
		ControlPanel.EpicCollectionProgress.getProgressBar().setValue(yourEpicCardsSoulGemCost);
		ControlPanel.EpicCollectionProgress.getSoulgem().setText(yourEpicCardsSoulGemCost+"/"+ControlPanel.EpicCollectionProgress.getProgressBar().getMaximum()+" Souls");
		/*Updates the RareProgressBar*/
		ControlPanel.RareCollectionProgress.getProgressBar().setValue(yourRareCardsSoulGemCost);
		ControlPanel.RareCollectionProgress.getSoulgem().setText(yourRareCardsSoulGemCost+"/"+ControlPanel.RareCollectionProgress.getProgressBar().getMaximum()+" Souls");
		/*Updates the CommonProgressBar*/
		ControlPanel.CommonCollectionProgress.getProgressBar().setValue(yourCommonCardsSoulGemCost);
		ControlPanel.CommonCollectionProgress.getSoulgem().setText(yourCommonCardsSoulGemCost+"/"+ControlPanel.CommonCollectionProgress.getProgressBar().getMaximum()+" Souls");
	}

	public static CollectionManager getInstance() {
		return COLLECTIONMANAGER;
	}
}
