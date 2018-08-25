package framework;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import resources.ColorSet;
import resources.RescaledImageIcon;
import resources.UseTheLookAndFeelYouWant;
import hub.Hub;

public class CardCollectionManagementPanel extends JPanel{
	/*ICONS*/
	private static final ImageIcon IMPORTBUTTON = new RescaledImageIcon("utility/icons/import.png",25,25).getRESCALEDICON();
	private static final ImageIcon UPLOADBUTTON = new RescaledImageIcon("utility/icons/upload.png",40,25).getRESCALEDICON();
	private static final ImageIcon EDITBUTTON = new RescaledImageIcon("utility/icons/settings.png",25,25).getRESCALEDICON();
	private static final ImageIcon SAVEBUTTON = new RescaledImageIcon("utility/icons/diskette.png",25,25).getRESCALEDICON();
	private static final ImageIcon SELECTALLBUTTON = new RescaledImageIcon("utility/icons/selectAll.png",25,25).getRESCALEDICON();
	private static final ImageIcon DESELECTALLBUTTON = new RescaledImageIcon("utility/icons/deselectAll.png",25,25).getRESCALEDICON();
	private static final ImageIcon PREMADEDECKSBUTTON = new RescaledImageIcon("utility/icons/skyrim.png",25,25).getRESCALEDICON();
	private static final ImageIcon EXPANSIONBUTTON = new RescaledImageIcon("utility/icons/dark brotherhood.png",25,25).getRESCALEDICON();

	/*DIALOG*/
	private static JDialog choose = new JDialog();
	/*INSTANCE*/
	private static final CardCollectionManagementPanel CardCollectionManagementPanel1 = new CardCollectionManagementPanel(true);
	private static final CardCollectionManagementPanel CardCollectionManagementPanel2 = new CardCollectionManagementPanel(false);

	private CardCollectionManagementPanel(boolean first) {
		if(first) {
			addUploadButton();
			addImportButton();
			addEditButton();
			addSaveButton();
		}
		else{
			addPremadeDecksButton();
			addExpansionsButton();
		}
		setBackground(ColorSet.ControlPanelElementsBackground);
	}
	
	private void addUploadButton() {
		JButton collectionUpload = new JButton("Upload");
		collectionUpload.setIcon(UPLOADBUTTON);
		collectionUpload = makePretty(collectionUpload);
		collectionUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Hub.getCollectionmanager().uploadCollection();
			}
		});
		add(collectionUpload);
	}

	private void addImportButton() {
		JButton collectionImport = new JButton("Import"); //Automatically via UniversalDeckTracker
		collectionImport.setIcon(IMPORTBUTTON);
		collectionImport = makePretty(collectionImport);
		collectionImport.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
				CustomFileChooser chooseLocation = new CustomFileChooser();
				int openFile = chooseLocation.showOpenDialog(Framework.getInstance());
				if(openFile==chooseLocation.APPROVE_OPTION) CollectionManager.getInstance().importCollection(chooseLocation.getSelectedFile().getPath());
				UseTheLookAndFeelYouWant.revert();
			}
		});
		add(collectionImport);
	}
	
	private void addEditButton() {
		JButton collectionEdit = new JButton("Edit"); //Manually via JTable
		collectionEdit.setIcon(EDITBUTTON);
		collectionEdit = makePretty(collectionEdit);
		collectionEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Hub.getCollectionmanager().editCollection();
			}
		});
		add(collectionEdit);
	}
	
	private void addSaveButton() {
		JButton collectionSave = new JButton("Save");
		collectionSave.setIcon(SAVEBUTTON);
		collectionSave = makePretty(collectionSave);
		collectionSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
				CustomFileChooser chooseLocation = new CustomFileChooser();
				int saveFile = chooseLocation.showSaveDialog(Framework.getInstance());
				if(saveFile == chooseLocation.APPROVE_OPTION) CollectionManager.getInstance().saveCollection(chooseLocation.getSelectedFile().getPath());
				UseTheLookAndFeelYouWant.revert();
			}
		});
		add(collectionSave);
	}
	
	private void addPremadeDecksButton() {
		JButton premadeDecks = new JButton("Manage Premade Decks");
		premadeDecks.setIcon(PREMADEDECKSBUTTON);
		premadeDecks = makePretty(premadeDecks);
		premadeDecks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CollectionManager.getInstance().managePurchasableDecks();
			}
		});
		add(premadeDecks);
	}

	private void addExpansionsButton() {
		JButton expansions = new JButton("Manage Expansions");
		expansions.setIcon(EXPANSIONBUTTON);
		expansions = makePretty(expansions);
		expansions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CollectionManager.getInstance().managePurchasableExpansions();
			}
		});
		add(expansions);
	}

	private JButton makePretty(JButton button) {
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		return button;
	}

	public static ImageIcon getSaveButton() {
		return SAVEBUTTON;
	}

	public static ImageIcon getSelectAllButton() {
		return SELECTALLBUTTON;
	}

	public static ImageIcon getDeselectAllButton() {
		return DESELECTALLBUTTON;
	}

	public static CardCollectionManagementPanel getFirstInstance() {
		return CardCollectionManagementPanel1;
	}
	
	public static CardCollectionManagementPanel getSecondInstance() {
		return CardCollectionManagementPanel2;
	}
	
}
