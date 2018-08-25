package hub;

import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import framework.CollectionManager;
import framework.Framework;
import objects.Card;
import resources.Alerts;
import resources.ReadTXT;

public class Hub {
	/*RESOURCES*/
	private static LinkedHashMap<String,Card> cardlist = new LinkedHashMap<String,Card>();
	private static LinkedHashMap<String,Card> collection = new LinkedHashMap<String,Card>();
	private static ArrayList<String> hintlist = new ArrayList<String>();
	private static String currentseason;
	private static CollectionManager collectionmanager;
	/*FRAMEWORK*/
	private static Framework framework;
	/*INSTANCE*/
	private static final Hub HUB= new Hub();
	/**
	 * <p>Public constructor of the CraftRoulette application. Makes use of the following methods:
	 * <ul>
	 * <li> {@link #fetchCards()}
	 * <li> {@link #fetchCollection()}
	 * <li> {@link #fetchHints()}
	 * <li> {@link #startInstances()}
	 * <li> {@link #openFramework()}
	 * </ul>
	 * @author João Mendonça
	 * 
	 */
	private Hub() {
		checkCurrentSeason();
		fetchCards();
		fetchCollection();
		fetchHints();
		startInstances();
		openFramework();
	}
	
	/**
	 * <p>Parent method: {@link #Hub()}.
	 * <p>Obtains the current Month and Year, which represent the present Season. This is very relevant for rating cards, as being part of the meta in the current season
	 * places a card in higher rating than a card which was part of the meta only in previous seasons.
	 */
	private static void checkCurrentSeason() {
		Month month = Month.of(Calendar.getInstance().get(Calendar.MONTH)+1);
		String currentMonth = month.getDisplayName(TextStyle.FULL,Locale.ENGLISH);
		currentseason = currentMonth+" "+Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * <p>Parent method: {@link #Hub()}.
	 * <p>Reads a text file and pulls all the cards in the game.
	 * <p>Will throw an IOException FileNotFound if the text file isn't where it's supposed to be.
	 */
	private static void fetchCards() {
		ReadTXT TXTreader = new ReadTXT("utility/cards/Evaluation.txt");
		try {
			cardlist = TXTreader.getAllCards();
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
	}
	
	/**
	 * <p>Parent method: {@link #Hub()}.
	 * <p>Reads a text file and pulls all the cards in the game into the collection, to start it anew.
	 * <p>Will throw an IOException FileNotFound if the text file isn't where it's supposed to be.
	 */
	private static void fetchCollection() {
		ReadTXT TXTreader = new ReadTXT("utility/cards/Evaluation.txt");
		try {
			collection = TXTreader.getAllCards();
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
	}
	
	/**
	 * <p>Parent method: {@link #Hub()}.
	 * <p>Reads a text file and pulls all the hints for this app.
	 * <p>Will throw an IOException FileNotFound if the text file isn't where it's supposed to be.
	 */
	private static void fetchHints() {
		ReadTXT TXTreader = new ReadTXT("utility/HINTS.txt");
		try {
			hintlist = TXTreader.getAllHints();
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
	}
	
	/**
	 * <p> Parent method: {@link #Hub()} .
	 * <p>Creates an instance of CollectionManager with the cardlist to aid collection management.
	 */
	private static void startInstances() {
		collectionmanager = CollectionManager.getInstance();
	}
	
	/**
	 * <p>Parent method: {@link #Hub()}.
	 * <p>Initializes the framework needed to interact with the application.
	 */
	private static void openFramework() {
		framework = Framework.getInstance();
	}
	
	public static LinkedHashMap<String,Card> getCardlist() {
		return cardlist;
	}
	
	public static LinkedHashMap<String, Card> getCollection() {
		return collection;
	}
	
	public static CollectionManager getCollectionmanager() {
		return collectionmanager;
	}
	
	public static ArrayList<String> getHintlist() {
		return hintlist;
	}
	
	public static String getCurrentSeason() {
		return currentseason;
	}
	
	public static Framework getFramework() {
		return framework;
	}
	
	/**
	 * The server updates all the client's data through this method.
	 * @param cardlist - New cardlist with new Values (can be the same)
	 * @param - hintlist - New hintlist with new hints (can be the same)
	 */
	public void updateData(LinkedHashMap<String,Card> cardlist, ArrayList<String> hintlist) {
		Hub.cardlist = cardlist;
		Hub.hintlist = hintlist;
	}
	
	public static Hub getInstance() {
		return HUB;
	}
}
