package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import objects.Card;
import objects.CardType;

public class ReadTXT {
	private String pathToCards;
	private String pathToHints;
	
	public ReadTXT(String pathToFile, boolean forCardsNotHints) {
		if(forCardsNotHints) pathToCards=pathToFile;
		else pathToHints=pathToFile;
	}
	
	public ArrayList<Card> getAllCards() throws IOException{
		ArrayList<Card> cardlist = new ArrayList<Card>();
		File f = new File(pathToCards);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			String newCardText = br.readLine();
			String[] divideContentsOfNewCardText = newCardText.split(" : ");
			System.out.println(divideContentsOfNewCardText[0]+" "+divideContentsOfNewCardText[1]+" "+divideContentsOfNewCardText[2]);
			if(divideContentsOfNewCardText[1].equals("UNIQUELEGENDARY")) 
				cardlist.add(new Card(divideContentsOfNewCardText[0],CardType.UNIQUELEGENDARY,Integer.valueOf(divideContentsOfNewCardText[2]),Integer.valueOf(divideContentsOfNewCardText[3]),divideContentsOfNewCardText[4]));
			if(divideContentsOfNewCardText[1].equals("LEGENDARY")) 
					cardlist.add(new Card(divideContentsOfNewCardText[0],CardType.LEGENDARY,Integer.valueOf(divideContentsOfNewCardText[2]),Integer.valueOf(divideContentsOfNewCardText[3]),divideContentsOfNewCardText[4]));
			if(divideContentsOfNewCardText[1].equals("EPIC")) 
				cardlist.add(new Card(divideContentsOfNewCardText[0],CardType.EPIC,Integer.valueOf(divideContentsOfNewCardText[2]),Integer.valueOf(divideContentsOfNewCardText[3]),divideContentsOfNewCardText[4]));
			if(divideContentsOfNewCardText[1].equals("RARE")) 
				cardlist.add(new Card(divideContentsOfNewCardText[0],CardType.RARE,Integer.valueOf(divideContentsOfNewCardText[2]),Integer.valueOf(divideContentsOfNewCardText[3]),divideContentsOfNewCardText[4]));
			if(divideContentsOfNewCardText[1].equals("COMMON")) 
				cardlist.add(new Card(divideContentsOfNewCardText[0],CardType.COMMON,Integer.valueOf(divideContentsOfNewCardText[2]),Integer.valueOf(divideContentsOfNewCardText[3]),divideContentsOfNewCardText[4]));
		}
		br.close();
		return cardlist;
	}
	
	public ArrayList<String> getAllHints() throws IOException{
		ArrayList<String> hintlist = new ArrayList<String>();
		File f = new File(pathToHints);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			hintlist.add(br.readLine());
		}
		return hintlist;
	}
	
	public static void main(String[] args) {
		ReadTXT um = new ReadTXT("utility/ALL_CARD_EVALUATION.txt",true);
		try {
			ArrayList<Card> dois = um.getAllCards();
		} catch (IOException e) {
		}
	}
}
