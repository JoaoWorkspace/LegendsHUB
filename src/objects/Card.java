package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import framework.MetaDecksPanel;
import hub.Hub;
import resources.Alerts;

public class Card {
	private String name;
	private CardType type;
	private int missing;
	private int rating;
	private String set;
	private String pathToImage;
	private int soulGemValue;
	
	public Card(String name, CardType type, String set) {
		this.name=name;
		this.type=type;
		if(type.equals(CardType.UNIQUELEGENDARY)) missing = 1;
		else missing = 3;
		this.set=set;
		if(type.equals(CardType.UNIQUELEGENDARY)) pathToImage = "utility/cards/Legendary/"+name+".png";
		else pathToImage = "utility/cards/"+type+"/"+name+".png";
		checkSoulGemValue();
	}
	
	public String getName() {
		return name;
	}
	
	public CardType getType() {
		return type;
	}
	
	public Card getCopy() {
		return new Card(name,type,set);
	}
	
	public int getMissing() {
		return missing;
	}
	
	public int getCopies() {
		if(type.equals(CardType.UNIQUELEGENDARY)) return 1-missing;
		return 3-missing;
	}
	
	public String getSet() {
		return set;
	}
	
	public int getSoulGemValue() {
		return soulGemValue;
	}
	
	public void reduceMissing() {
		if(missing>0)missing--;
	}
	
	
	public void resetMissing() {
		if(type.equals(CardType.UNIQUELEGENDARY)) missing = 1;
		else missing = 3;
	}
	
	public String getPathToImage() {
		return pathToImage;
	}
	
	public BigInteger getCraftValue() {
		BigInteger craftValue = BigInteger.valueOf(type.getBaseValue());
		craftValue = craftValue.multiply(BigInteger.valueOf(rating)).multiply(BigInteger.valueOf(missing));
		return craftValue;
	}
	
	/**
	 * This is how each and every card is rated:
	 * <p> Is meta currently and was before? Rating = 5
	 * <p> Is meta now but never was before? Rating = 4
	 * <p> Is not meta now but was in the last 3 seasons? Rating = 3
	 * <p> Is not meta now but was in the past? Rating = 2
	 * <p> Was never meta? Rating = 1
	 */
	public void checkRating() {
		boolean hasCurrentMetaPresence = false;
		boolean hasPreviousMetaPresence = false;
		boolean hasPresenceInLastThreeSeasons = false;
		/*Is part of the current season*/
		String currentSeason = Hub.getCurrentSeason().split(" ")[0];
		String currentYear = Hub.getCurrentSeason().split(" ")[1];
		String currentMeta = currentYear+"/"+currentSeason;
		/*List Last 3 Seasons*/
		ArrayList<String> Last3Seasons = new ArrayList<String>();
		for(String season : MetaDecksPanel.getSeasons().keySet()) {
			String fixed = season.replace(" ", "/");
			Last3Seasons.add(fixed);
		}
		/*Discover what's the current season's position*/
		int currentSeasonPosition = 0;
		for(int i=0;i<Last3Seasons.size();i++) {
			if(Last3Seasons.get(i).equals(currentMeta)) currentSeasonPosition=i;
		}
		/*Verify each Season for Meta Decks to find out where this card belongs to*/
		for(String pathToDeck : MetaDecksPanel.getMetaDecks().keySet()) {
			/*Check if this card is part of current meta*/
			if(pathToDeck.contains(currentMeta)) {
				if(MetaDecksPanel.getMetaDecks().get(pathToDeck).getCARDS().containsKey(name)) hasCurrentMetaPresence=true;
			}
			/*Check if the card is part of old meta*/
			else {
				if(MetaDecksPanel.getMetaDecks().get(pathToDeck).getCARDS().containsKey(name)) {
					/*Check if the card was part of the last 3 seasons' meta*/
					if(pathToDeck.contains(Last3Seasons.get(currentSeasonPosition-1))
						||pathToDeck.contains(Last3Seasons.get(currentSeasonPosition-2))
						||pathToDeck.contains(Last3Seasons.get(currentSeasonPosition-3)))hasPresenceInLastThreeSeasons=true;
					hasPreviousMetaPresence=true;
				}
			}
		}
		if(hasCurrentMetaPresence&&hasPreviousMetaPresence)rating=5;
		else if(hasCurrentMetaPresence&&!hasPreviousMetaPresence)rating=4;
		else if(!hasCurrentMetaPresence&&hasPresenceInLastThreeSeasons)rating=3 ;
		else if(!hasCurrentMetaPresence&&hasPreviousMetaPresence&&!hasPresenceInLastThreeSeasons)rating=2;
		else if(!hasCurrentMetaPresence&&!hasPreviousMetaPresence)rating = 1;
	}
	
	public int getRating() {
		return rating;
	}
	
	public String getLastMetaPresence() {
		String lastYear = "";
		String lastMonth = "";
		LinkedHashMap<String,Integer> MonthInteger = new LinkedHashMap<String,Integer>();
		for(int i=0;i<MetaDecksPanel.getMonthOrder().length;i++) MonthInteger.put(MetaDecksPanel.getMonthOrder()[i], i);
		
		for(Deck deck : MetaDecksPanel.getMetaDecks().values()) {
			if(deck.getCARDS().containsKey(name)) {
				/*Let's check if the Year is later than the one being currently used*/
				if(lastYear.equals("")||lastMonth.equals("")) {
					lastYear=deck.getYear();
					lastMonth=deck.getSeason();
				}
				else if(Integer.valueOf(deck.getYear())>Integer.valueOf(lastYear)) {
					lastYear = deck.getYear();
					lastMonth = deck.getSeason();
				}
				else if(Integer.valueOf(deck.getYear())>=Integer.valueOf(lastYear)&&MonthInteger.get(deck.getSeason())>MonthInteger.get(lastMonth)) {
					lastYear = deck.getYear();
					lastMonth=deck.getSeason();
				}
			}
		}
		if(lastYear.equals("")||lastMonth.equals("")) return "Never";
		else return lastMonth+", "+lastYear;
	}
	
	
	public void checkSoulGemValue() {
		switch(type) {
		case UNIQUELEGENDARY: 
		case LEGENDARY:
			soulGemValue = 1200;
			break;
		case EPIC:
			soulGemValue = 400;
			break;
		case RARE:
			soulGemValue = 100;
			break;
		case COMMON:
			soulGemValue = 50;
			break;
		}
	}
	
	public void checkImagePath() {
		ImageIcon testImage = new ImageIcon(pathToImage);
		if(testImage.equals(null)) Alerts.noImageAvailableForCard(name);
	}
}
