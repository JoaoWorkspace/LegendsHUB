package framework;

import javax.swing.JOptionPane;

import main.CraftRoulette;
import objects.Card;

public class Alerts extends JOptionPane{
	
	
	public static void NoCardsToShow() {
		showMessageDialog(Framework.getInstance(), "Congratulations! Either you filtered every card type out by mistake or you have no card left to be crafted.\n"
				+ "If it's the latter congratulations for achieving full collection!\nHope this application was useful to you, and hope you'll recommend it to your friends!", 
				"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void IncorrectInput(int row) {
		showMessageDialog(CollectionManager.getInstance(), "There's an incorrect input on line "+row+"\n Uploading cancelled.");
	}
	
	public static void ThereAreLinesWithWrongInput(int number) {
		showMessageDialog(CollectionManager.getInstance(), number+" line(s) have wrong input detected!\nUploading cancelled.\n"
				+ "Check if there's no: Card names repeated or Typos","Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	public static void RevealPictureInformation(Card card) {
		showMessageDialog(CardPanel.getInstance(), 
			"<html><ul>" +
			"<li>Card Rating: "+card.getRating()+"</li>"+
			"<li>Set: "+card.getSet()+"</li>"+
			"<li>You are missing "+card.getMissing()+" copies of this card.</li>"+
			"</ul></html>");
	}
}
