package main;

import java.io.IOException;
import java.util.ArrayList;
import framework.CollectionManager;
import framework.Framework;
import objects.Card;
import resources.ReadTXT;

public class CraftRoulette {
	/*FRAMEWORK*/
	private static Framework framework;
	/*RESOURCES*/
	private static ArrayList<Card> cardlist;
	private static ArrayList<String> hintlist;
	private static CollectionManager collectionmanager;
//	private static Card randomcard;
	/*SINGLETON*/
	private static final CraftRoulette CRAFTROULETTE= new CraftRoulette();
	
	/**
	 * <p>Public constructor of the CraftRoulette application. Makes use of the following methods:
	 * <ul>
	 * <li> {@link #fetchCards()}
	 * <li> {@link #fetchHints()}
	 * <li> {@link #openFramework()}
	 * </ul>
	 * @author João Mendonça
	 * 
	 */
	public CraftRoulette() {
		fetchCards();
		fetchHints();
		openFramework();
	}
	
	/**
	 * <p>Parent method: {@link #CraftRoulette()}.
	 * <p>Reads a text file and pulls all the cards in the game, fully evaluated and defined.
	 * <p>Creates an instance of CollectionManager with the cardlist to aid collection management.
	 * <p>Will throw an IOException FileNotFound if the text file isn't where it's supposed to be.
	 */
	private void fetchCards() {
		ReadTXT whereToFetchFrom = new ReadTXT("utility/ALL_CARD_EVALUATION.txt",true);
		try {
			cardlist = whereToFetchFrom.getAllCards();
			collectionmanager = CollectionManager.getInstance();
		} catch (IOException e) {
		}
	}
	
	/**
	 * <p>Parent method: {@link #CraftRoulette()}.
	 * <p>Reads a text file and pulls all the hints for this app.
	 * <p>Will throw an IOException FileNotFound if the text file isn't where it's supposed to be.
	 */
	private void fetchHints() {
		ReadTXT whereToFetchFrom = new ReadTXT("utility/HINTS.txt",false);
		try {
			hintlist = whereToFetchFrom.getAllHints();
		} catch (IOException e) {
			System.out.println("something");
		}
	}
	
	/**
	 * <p>Parent method: {@link #CraftRoulette()}.
	 * <p>Initializes the framework needed to interact with the application.
	 */
	private void openFramework() {
		framework = Framework.getInstance();
	}
	
	public static ArrayList<Card> getCardlist() {
		return cardlist;
	}
	
	public static CollectionManager getCollectionmanager() {
		return collectionmanager;
	}
	
	public static ArrayList<String> getHintlist() {
		return hintlist;
	}
	
	public static Framework getFramework() {
		return framework;
	}
	
	public static CraftRoulette getInstance() {
		return CRAFTROULETTE;
	}
	public static void main(String[] args) {
		CraftRoulette.getInstance();
		
	}
}
