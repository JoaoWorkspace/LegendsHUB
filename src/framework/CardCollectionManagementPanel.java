package framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.CraftRoulette;

public class CardCollectionManagementPanel extends JPanel{
	private static final CardCollectionManagementPanel CardCollectionManagementPanel = new CardCollectionManagementPanel();
	
	private CardCollectionManagementPanel() {
		addUploadButton();
		addEditButton();
		
		
	}
	
	private void addUploadButton() {
		JButton collectionUpload = new JButton("Upload Collection"); //Via UniversalDeckTracker
		collectionUpload.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				CraftRoulette.getCollectionmanager().uploadCollection();
			}
		});
		add(collectionUpload);
	}
	
	private void addEditButton() {
		JButton collectionEdit = new JButton("Edit your Collection"); //Manually through JGrid
		collectionEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CraftRoulette.getCollectionmanager().editCollection();
			}
		});
		add(collectionEdit);
	}
	

	public static CardCollectionManagementPanel getInstance() {
		return CardCollectionManagementPanel;
	}
}
