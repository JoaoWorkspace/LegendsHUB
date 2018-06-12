package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.CraftRoulette;
import objects.Card;
import objects.CardType;
import resources.LineNumberingTextArea;

public class CollectionManager extends JFrame{
	private static ArrayList<Card> myCollection = CraftRoulette.getCardlist();
	private static ArrayList<Card> cardlist = CraftRoulette.getCardlist();
	private static JTable collection;
	private static ControlPanel controlpanel;
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
	}

	public ArrayList<Card> getMyCollection() {
		return myCollection;
	}

	public void IamYourFather(ControlPanel controlPanel) {
		controlpanel=controlPanel;
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
		JTextArea pasteArea = new JTextArea(4,40);
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
		info.setHorizontalAlignment(JLabel.LEFT);
		info.setVerticalAlignment(JLabel.TOP);
		PASTE.add(info);
		PASTE.add(scrollable);
		PASTE.setAlignmentX(0);

		/*Create Button to upload the information given in text area to update my collection*/
		CustomButton UPLOAD = new CustomButton("UPLOAD");
		UPLOAD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myCollection = cardlist;
				int cardsAffected = 0;
				int totalrows = 0;
				/*For each card in the game, searches if there is a corresponding collection entry*/
				for(Card c : myCollection) {
					int row = 0;
					for (String line : pasteArea.getText().split("\\n")) {
						row++;
						String[] firstdivision = line.split(" \\(");
						String content = firstdivision[0];
						if(content.length()>2) {
							int ncards = Integer.parseInt(0 + content.substring(0, 1).replaceAll("\\D+",""));
							String cardname = String.valueOf(content.substring(2));
							/* Set the number of copies missing for that specific card, based on how many copies of it you have */
							if(c.getName().equals(cardname)) {
								for(int i=0;i<ncards;i++) c.reduceMissing();
								cardsAffected++;
								break;
							}
						}
						/* Throw incorrect format exception*/
						else {
							Alerts.IncorrectInput(row);
							myCollection = cardlist;
							return;
						}
					}
					totalrows = row;
				}
				if(cardsAffected!=totalrows) {
					int number = totalrows-cardsAffected;
					Alerts.ThereAreLinesWithWrongInput(number);
					myCollection = cardlist;
				}
				updateData(controlpanel);
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

		createJTable();
		JScrollPane scrollable = new JScrollPane(collection);

		setLayout(new BorderLayout());
		setTitle("Collection Manager");
		add(scrollable);
		JPanel SAVE = new JPanel();
		JButton savebutton = new JButton("SAVE COLLECTION");
		savebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int row = 0;row<collection.getRowCount();row++) {
					String cardname = (String) collection.getValueAt(row, 0); //Returns the card name so it can be searched on the cardlist
					int ncopies = (int)collection.getValueAt(row, 3); //Returns the number of copies of the card you set
					for(Card c : myCollection) {
						if(c.getName().equals(cardname)){
							if(c.getType().equals(CardType.UNIQUELEGENDARY)) {
								if(c.getMissing()>0 && ncopies>0) c.reduceMissing();
							}else {
								c.setMissing(3-ncopies);
							}	
						}
					}	
				}
				updateData(controlpanel);
			}
		});
		SAVE.add(savebutton);
		add(SAVE,BorderLayout.SOUTH);
		setResizable(false);
		pack();		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setState(NORMAL);
		setVisible(true);
	}

	/**
	 * Parent component: {@link #editCollection()}. This method introduces the number of copies this user has in the right-most column of the JTable.
	 * @param c - The card we want to know how many cards are missing
	 * @return - Returns the number of cards fot that particular card
	 */
	private int insertNumberOfCards(Card c){
		int number = 0;
		if (c.getType().equals(CardType.UNIQUELEGENDARY)) number = 1-c.getMissing();
		else number = 3-c.getMissing();
		return number;
	}

	/**
	 * Parent mehtod: {@link #editCollection()}.
	 * Initializes the Table with the current collection information available (either by previous Edit, or by Upload).
	 */
	private void createJTable() {
		String[] columns = {"Card Name","Card Rarity","Set","Copies"};
		Object[][] data = new Object[myCollection.size()][columns.length];
		int row = 0;
		for(Card c : myCollection) {
			data[row][0] = c.getName();    				//Card Name
			data[row][1] = c.getType().getImage();		//Card Type Image
			data[row][2] = c.getSet();					//Card Set
			data[row][3] = insertNumberOfCards(c);		//Number of Card Copies
			row++;
		}
		/*Guarantees that the second column will be read as ImageIcon instead of String*/
		DefaultTableModel model = new DefaultTableModel(data, columns);
		collection = new JTable(model) {
			public Class getColumnClass(int column) {
				if(column==1) {
					return Icon.class;
				}
				return Object.class;
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column==3) return true;
				return false;
			}
		};
		/*Defining the default size of columns, which helps them having different sizes according to the content they wrap*/
		collection.getColumnModel().getColumn(0).setMinWidth(150);
		collection.getColumnModel().getColumn(0).setMaxWidth(150);
		collection.getColumnModel().getColumn(1).setMinWidth(70);
		collection.getColumnModel().getColumn(1).setMaxWidth(70);
		collection.getColumnModel().getColumn(2).setMinWidth(200);
		collection.getColumnModel().getColumn(2).setMaxWidth(200);
		collection.getColumnModel().getColumn(3).setMinWidth(50);
		collection.getColumnModel().getColumn(3).setMaxWidth(50);
		/*Making the last column be editable through a JComboBox*/
		Integer[] choices = {0,1,2,3};
		JComboBox<Integer> chooser = new JComboBox<Integer>(choices);
		DefaultCellEditor cellEditor = new DefaultCellEditor(chooser);
		collection.getColumnModel().getColumn(3).setCellEditor(cellEditor);
		/*Make all Cells center their data*/
		((DefaultTableCellRenderer)collection.getDefaultRenderer(String.class)).setHorizontalAlignment(SwingConstants.CENTER);
		/*Make sure the scrollable size will contain all objects by setting a preferred size*/
		collection.setRowHeight(20);
		collection.setFillsViewportHeight(true);
		collection.getTableHeader().setReorderingAllowed(false);
		collection.setPreferredScrollableViewportSize(new Dimension (470,500));
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
		for(Card card : myCollection) {
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
