package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import framework.CollectionManager;
import framework.CraftRoulettePanel;
import framework.Framework;
import hub.Hub;
import objects.Card;

public class Alerts extends JOptionPane{
	
	
	public static void NoCardsToShow() {
		showMessageDialog(Framework.getInstance(), "Congratulations! Either you filtered every card type out by mistake or you have no card left to be crafted.\n"
				+ "If it's the latter congratulations for achieving full collection!\nHope this application was useful to you, and hope you'll recommend it to your friends!", 
				"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void IncorrectInput(int row,String mistake) {
		showMessageDialog(CollectionManager.getInstance(), "There's an incorrect input on line "+row
				+"\nText:\""+mistake+"\"\nUploading cancelled.");
	}
	
	public static void RevealPictureInformation(Card card) {
		showMessageDialog(CraftRoulettePanel.getInstance(), 
			"<html><ul>" +
			"<li>Card Rating: "+card.getRating()+"</li>"+
			"<li>Set: "+card.getSet()+"</li>"+
			"<li>You are missing "+Hub.getCollection().get(card.getName()).getMissing()+" copies of this card.</li>"+
			"<li>Last meta presence: "+card.getLastMetaPresence()+".</li>"+
			"</ul></html>","Card Information",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(card.getPathToImage()));
	}
	
	public static void launchException(String errorMessage){
		showMessageDialog(null,errorMessage,"Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void serverAlreadyRunning() {
		showMessageDialog(null,"The server is already up and running.","Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void updatesAreAvailable(HashMap<File,byte[]> serverFiles) {
		int OkCancel = showConfirmDialog(null, "It has been detected you're missing some files!\nWish to update?","Updates",JOptionPane.OK_CANCEL_OPTION);
		if(OkCancel==JOptionPane.OK_OPTION){
			for(File file : serverFiles.keySet()) {
				byte[] write = serverFiles.get(file);
				try {
					System.out.println(file.getPath());
					FileOutputStream fos = new FileOutputStream(file.getPath());
					fos.write(write);
					fos.close();
				} catch (IOException e) {
					Alerts.launchException(e.getMessage());
				}
			}
		}
	}
	
	public static void serverWentOffline() {
		showMessageDialog(null,"Server just went OFFLINE.","Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void serverOffline() {
		showMessageDialog(null,"The Server is currently OFFLINE, try again later.","Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noSuchCard(String cardname) {
		showMessageDialog(null,"There is no such card as "+cardname,"Error",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void thisProcessMightTakeSeveralSeconds() {
		showMessageDialog(null,"Getting files from the server to compare.\nThis process might take several seconds.","Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noUpdatesAvailable() {
		showMessageDialog(null, "Everything is up to date.");
	}
	
	public static void wrongInputOnEvaluationTxt(String wrongLine) {
		showMessageDialog(null, "In the utility/Evaluation.txt file there's an incorrect line, please remove/correct or check for updates to correct the mistake:\n"+wrongLine,"Error in retrieving cards",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noDeckWithThatName(String wrongDeckName) {
		showMessageDialog(null, "The following deck does not exist in the current database, check for updates to correct the mistake:\n"+wrongDeckName,"Error updating purchasable deck acquisition state",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noExpansionWithThatName(String wrongExpansionName) {
		showMessageDialog(null, "The following expansion does not exist in the current database, check for updates to correct the mistake:\n"+wrongExpansionName,"Error updating purchasable expansion acquisition state",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void noImageAvailableForCard(String card) {
		showMessageDialog(null,"The card "+card+" doesn't have any image resource available.","Warning",JOptionPane.WARNING_MESSAGE);
	}
}
