package resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import framework.PurchasableDecksPanel;
import framework.PurchasableExpansionsPanel;
import hub.Hub;
import objects.Card;
import objects.CardType;

public class ReadTXT {
	private String pathToFile;

	public ReadTXT(String pathToFile) {
		this.pathToFile=pathToFile;
	}

	public LinkedHashMap<String,Card> getAllCards() throws IOException{
		LinkedHashMap<String,Card> cardlist = new LinkedHashMap<String,Card>();
		File f = new File(pathToFile);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			String newCardText = br.readLine();
			String[] divideContentsOfNewCardText = newCardText.split(" : ");
			if(checkForCardNameMistake(divideContentsOfNewCardText[0])||checkForRarityMistake(divideContentsOfNewCardText[1])||checkForExpansionMistake(divideContentsOfNewCardText[2])) 
				Alerts.wrongInputOnEvaluationTxt(newCardText);
			else cardlist.put(divideContentsOfNewCardText[0],new Card(divideContentsOfNewCardText[0],CardType.valueOf(divideContentsOfNewCardText[1]),divideContentsOfNewCardText[2]));
		}
		br.close();
		return cardlist;
	}

	public ArrayList<String> getAllHints() throws IOException{
		ArrayList<String> hintlist = new ArrayList<String>();
		File f = new File(pathToFile);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			hintlist.add(br.readLine());
		}
		return hintlist;
	}

	public LinkedHashMap<String,Integer> getDeckContents() throws IOException{
		LinkedHashMap<String,Integer> deck = new LinkedHashMap<String,Integer>();
		File f = new File(pathToFile);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			String line = br.readLine();
			String cardname = line.split(" : ")[0];
			Integer cardcopies = Integer.valueOf(line.split(" : ")[1]);
			deck.put(cardname, cardcopies);
		}
		return deck;
	}

	public LinkedHashMap<String,Card> readCollectionCards() throws IOException{
		LinkedHashMap<String,Card> collection = new LinkedHashMap<String,Card>();
		for(String key : Hub.getCollection().keySet()) collection.put(key, Hub.getCollection().get(key).getCopy());
		File f = new File(pathToFile);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			String newCardText = br.readLine();
			String[] divideContentsOfNewCardText = newCardText.split(" : ");
			String cardname = divideContentsOfNewCardText[0];
			if(isPurchasableDeck(cardname)) PurchasableDecksPanel.updateAcquiredState(cardname, divideContentsOfNewCardText[1].equals("1"));
			else if(isPurchasableExpansion(cardname)) PurchasableExpansionsPanel.updateAcquiredState(cardname, divideContentsOfNewCardText[1].equals("1"));
			else {
				int ncopies = Integer.valueOf(divideContentsOfNewCardText[1]);
				if(collection.containsKey(cardname)) {
					for(int i=0;i<ncopies;i++) collection.get(cardname).reduceMissing();
				}
				else {
					Alerts.noSuchCard(cardname);
				}
			}
		}
		br.close();
		return collection;
	}

	public void saveCollectionCards() throws IOException{
		String fileSuffix = ".collection";
		String fileName = pathToFile;
		if(pathToFile.endsWith(".collection")) fileName = pathToFile.replaceAll(".collection", "");
		String filepath = fileName+fileSuffix;
		System.out.println(filepath);
		File saveFile = new File(filepath);
		FileWriter fw = new FileWriter(saveFile);
		BufferedWriter bw = new BufferedWriter(fw);
		/*Purchasable Deck Data*/
		for(String deck : PurchasableDecksPanel.getPurchasableDeck().keySet()) {
			if(PurchasableDecksPanel.getPurchasableDeckAcquiredStates().get(deck)) bw.write(deck+" : 1");
			else bw.write(deck+" : 0");
			bw.newLine();
		}
		/*Purchasable Expansion Data*/
		for(String expansion : PurchasableExpansionsPanel.getPurchasableExpansion().keySet()) {
			if(PurchasableExpansionsPanel.getPurchasableExpansionAcquiredStates().get(expansion)) bw.write(expansion+" : 1");
			else bw.write(expansion+" : 0");
			bw.newLine();
		}
		/*Card Data*/
		for(Card card : Hub.getCollection().values()) {
			bw.write(card.getName()+" : "+String.valueOf(card.getCopies()));
			bw.newLine();
		}
		bw.close();
	}

	private boolean checkForCardNameMistake(String cardname){
		if(cardname.startsWith(" ")||cardname.endsWith(" ")||cardname.isEmpty()) return true;
		if(!Pattern.matches("[a-zA-Z\\s',-]+", cardname)) return true;
		return false;
	}

	private boolean checkForRarityMistake(String rarity) {
		if(rarity.equals("LEGENDARY")
				||rarity.equals("UNIQUELEGENDARY")
				||rarity.equals("EPIC")
				||rarity.equals("RARE")
				||rarity.equals("COMMON")) return false;
		return true;
	}

	private boolean checkForExpansionMistake(String expansion) {
		if(expansion.equals("Starter Cards")
				||expansion.equals("Monthly Rewards")
				||expansion.equals("The Fall of the Dark Brotherhood")
				||expansion.equals("Madhouse")
				||expansion.equals("Return to Clockwork City")
				||expansion.equals("Forgotten Hero")
				||expansion.equals("Core")
				||expansion.equals("Heroes of Skyrim")
				||expansion.equals("Houses of Morrowind")) return false;
		return true;
	}

	private boolean isPurchasableDeck(String deckname) {
		return PurchasableDecksPanel.getPurchasableDeckAcquiredStates().containsKey(deckname);
	}

	private boolean isPurchasableExpansion(String expansion) {
		return PurchasableExpansionsPanel.getPurchasableExpansionAcquiredStates().containsKey(expansion);
	}
}
