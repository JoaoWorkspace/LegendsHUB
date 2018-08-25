package objects;

import java.io.IOException;
import java.util.LinkedHashMap;

import hub.Hub;
import resources.Alerts;
import resources.ReadTXT;

public class Deck {
	private String year;
	private String season;
	private String tier;
	private String name;
	private String classe;
	private LinkedHashMap<String,Integer> CARDS;
	private LinkedHashMap<String,Integer> MissingCards = new LinkedHashMap<String,Integer>();
	
	public Deck(String pathToDeck) {
		try {
			CARDS = new ReadTXT(pathToDeck).getDeckContents();
		} catch (IOException e) {Alerts.launchException(e.getMessage());}
		String[] pathFolders = pathToDeck.replaceAll("\\\\", "/").split("/");
		classe = pathFolders[6].split(" ")[1];
		name = pathFolders[6].split("\\.")[0];
		tier = pathFolders[5];
		season = pathFolders[4];
		year = pathFolders[3];
	}
	
	public LinkedHashMap<String, Integer> getCARDS() {
		return CARDS;
	}
	
	public String getName() {
		return name;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getSeason() {
		return season;
	}
	
	public String getTier() {
		return tier;
	}
	
	public String getClasse() {
		return classe;
	}
	
	public LinkedHashMap<String,Integer> getMissingCards(){
		return MissingCards;
	}
	
	public int getMissingSoulGems(){
		MissingCards.clear();
		int missing = 0;
		for(String cardname : CARDS.keySet()) {
			if(Hub.getCardlist().containsKey(cardname)) {
				int DeckCollectionDifference = CARDS.get(cardname)-Hub.getCollection().get(cardname).getCopies();
				if(DeckCollectionDifference>0) {
					missing+= DeckCollectionDifference*Hub.getCardlist().get(cardname).getSoulGemValue();
					MissingCards.put(cardname, DeckCollectionDifference);
				}
			}
		}
		return missing;
	}
}