package framework;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import hub.Hub;
import objects.Card;
import objects.CardType;
import objects.CraftRouletteMode;
import resources.Alerts;
import resources.RescaledImageIcon;

public class CraftRoulettePanel extends CustomPanel{
	private static final JLabel CardIcon = new JLabel();
	private static final CustomButton ShowMeWhatToCraft = new CustomButton("Show me what to craft!",false);
	private static final HashMap<String, Card> cardlist = Hub.getCardlist();
	private static Card randomcard;
	/*INSTANCE*/
	private static final CraftRoulettePanel INSTANCE = new CraftRoulettePanel();

	private CraftRoulettePanel() {
		RescaledImageIcon rescaler = new RescaledImageIcon("utility/images/start.png",350,580);
		CardIcon.setIcon(rescaler.getRESCALEDICON());
		add(CardIcon);
		ShowMeWhatToCraft.setPreferredSize(new Dimension(330,40));
		add(ShowMeWhatToCraft);
		setPreferredSize(Framework.CARD_PANEL_DIMENSION);
		setButtonAction();
	}
	
	private void setButtonAction() {
		CardIcon.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(randomcard!=null) Alerts.RevealPictureInformation(Hub.getCardlist().get(randomcard.getName()));
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		ShowMeWhatToCraft.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Card> filterAppliedCards = applyFiltering();
				selectRandomCard(filterAppliedCards);
			}
		});
	}
	
	/**
	 * <p>Parent method: {@link #setButtonAction()}.
	 * <p>Applies rarity filter, the card is only counted if the filter for it's rarity is enabled.
	 * <p>Applies set filter, the card is only counted if the filter for it's set is enabled.
	 * The new cardlist is then used as argument for {@link #selectRandomCard(ArrayList)}.
	 * @return A new cardlist (ArrayList) after the filters were applied
	 */
	private ArrayList<Card> applyFiltering() {
		ArrayList<Card> cardlistfiltertype = new ArrayList<Card>();
		ArrayList<Card> cardsfilterset = new ArrayList<Card>();
		ArrayList<Card> newcardlist = new ArrayList<Card>();
		for(Card card : cardlist.values()) {
			if((card.getType().equals(CardType.LEGENDARY) || card.getType().equals(CardType.UNIQUELEGENDARY))&& RarityTogglePanel.getToggleLegendary().isSelected()
					|| card.getType().equals(CardType.EPIC)&&RarityTogglePanel.getToggleEpic().isSelected()
					|| card.getType().equals(CardType.RARE)&& RarityTogglePanel.getToggleRare().isSelected()
					|| card.getType().equals(CardType.COMMON)&&RarityTogglePanel.getToggleCommon().isSelected()) cardlistfiltertype.add(card);
		}
		for(Card card : cardlistfiltertype) {
			if(card.getSet().equals("Core")&&SetTogglePanel.getSetCore().isSelected()
					||card.getSet().equals("Heroes of Skyrim")&&SetTogglePanel.getSetHeroesOfSkyrim().isSelected()
					||card.getSet().equals("Houses of Morrowind")&&SetTogglePanel.getSetHousesOfMorrowind().isSelected()
					||card.getSet().equals("Monthly Rewards")&&SetTogglePanel.getSetMonthlyRewards().isSelected()
					||card.getSet().equals("Starter Cards")&&SetTogglePanel.getSetStarterCards().isSelected()
					||card.getSet().equals("The Fall of the Dark Brotherhood")
					||card.getSet().equals("Return to Clockwork City")
					||card.getSet().equals("Forgotten Hero")
					||card.getSet().equals("Madhouse"))
					cardsfilterset.add(card);
		}
		if(CraftRouletteModePanel.getMODE().equals(CraftRouletteMode.HELPFUL)) {
			BigInteger BestValue = getMaximumValueOf(cardsfilterset);
			for(Card card : cardsfilterset) {
				if(card.getCraftValue().compareTo(BestValue)==0) newcardlist.add(card);
			}
		}
		else if(CraftRouletteModePanel.getMODE().equals(CraftRouletteMode.NORMAL)) {
			for(Card card : cardsfilterset) {
				if(!(card.getSet().equals("The Fall of the Dark Brotherhood")
						||card.getSet().equals("Return to Clockwork City")||card.getSet().equals("Forgotten Hero")
						||card.getSet().equals("Madhouse")||card.getSet().equals("Isle of Madness"))) newcardlist.add(card);
			}
		}
		else newcardlist = cardsfilterset;
		return newcardlist;
	}
	
	/**
	 * <p>Parent method: {@link #setButtonAction()}.
	 * <p>Based on the filtered list received from {@link #applyFiltering()} it randomizes through all weights and returns the random card.
	 */
	public void selectRandomCard(ArrayList<Card> cardlist) {
		boolean CARD_HAS_BEEN_CHOSEN = false;
		BigInteger total = BigInteger.valueOf(0);
		for(Card card : cardlist) {
			if(CraftRouletteModePanel.getMODE().equals(CraftRouletteMode.CHAOS)) total = total.add(BigInteger.ONE);
			else total = total.add(card.getCraftValue());
		}
		System.out.println("TOTAL: "+total);
		if(total.compareTo(BigInteger.ZERO)==0) Alerts.NoCardsToShow();
		else {
			Random random = new Random();
			BigInteger randomnumber = new BigInteger(total.bitLength(), random);
			/*I would prefer to be able to randomize a BigInteger and an upperlimit, but so far it goes over the upperlimit and I gotta ignore when it is so!*/
			while(randomnumber.compareTo(total)>=0) randomnumber = new BigInteger(total.bitLength(),random);
			/* It will run through all the cards, summing up their total value. Now the randomnumber is between 0 and the total sum of all cards, 
		  	this guarantees every card has a balanced chance!*/
			int pointer = 0;
			BigInteger sum = BigInteger.valueOf(0);
			while(!CARD_HAS_BEEN_CHOSEN) {
				if(CraftRouletteModePanel.getMODE().equals(CraftRouletteMode.CHAOS)) sum = sum.add(BigInteger.ONE);
				else sum = sum.add(cardlist.get(pointer).getCraftValue());
				if(randomnumber.compareTo(sum)<0) {
					randomcard = cardlist.get(pointer);
					CARD_HAS_BEEN_CHOSEN = true;
				}
				pointer++;
			}
			System.out.println(randomcard.getName() +" - RATING: "+randomcard.getRating()+" | Craft Value: "+randomcard.getCraftValue());
			/* Show on Screen the image of the card randomly selected*/
			try {
				BufferedImage myPicture = ImageIO.read(new File(randomcard.getPathToImage()));
				CardIcon.setIcon(new ImageIcon(myPicture));
			} catch (IOException e1) {}
		}
	}
	
	private BigInteger getMaximumValueOf(ArrayList<Card> cardlist) {
		BigInteger maximumValue = BigInteger.valueOf(0);
		for(Card card : cardlist) {
			if(card.getCraftValue().compareTo(maximumValue)>0) maximumValue = card.getCraftValue();
		}
		return maximumValue;
	}
	
	public static CraftRoulettePanel getInstance() {
		return INSTANCE;
	}
}
