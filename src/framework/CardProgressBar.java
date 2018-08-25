package framework;

import java.awt.Color;

import javax.swing.JProgressBar;

import hub.Hub;
import objects.Card;
import objects.CardType;
import resources.ColorSet;

public class CardProgressBar extends JProgressBar{
	private static final int TotalCardsSoulGemCost = getTotalCardsCost();
	private static final int TotalLegendariesSoulGemCost = getTotalLegendariesCost();
	private static final int TotalEpicsSoulGemCost = getTotalEpicsCost();
	private static final int TotalRaresSoulGemCost = getTotalRaresCost();
	private static final int TotalCommonsSoulGemCost = getTotalCommonsCost();

	public CardProgressBar() {
		super(0,0,TotalCardsSoulGemCost);
		setBackground(Color.WHITE);
		setForeground(ColorSet.CraftRouletteTheme);
		setStringPainted(true);
	}

	private static int getTotalCardsCost(){
		int totalGemCost = 0;
		for(Card card : Hub.getCardlist().values()) {
			switch(card.getType()) {
			case UNIQUELEGENDARY: 
				totalGemCost += 1200;
				break;
			case LEGENDARY:
				totalGemCost += 3600;
				break;
			case EPIC:
				totalGemCost +=1200;
				break;
			case RARE:
				totalGemCost += 300;
				break;
			case COMMON:
				totalGemCost += 150;
				break;
			}
		}
		return totalGemCost;
	}

	private static int getTotalLegendariesCost() {
		int totalGemCost = 0;
		for(Card card : Hub.getCardlist().values()) {
			switch(card.getType()) {
			case UNIQUELEGENDARY: 
				totalGemCost += 1200;
				break;
			case LEGENDARY:
				totalGemCost += 3600;
				break;
			}
		}
		return totalGemCost;
	}

	private static int getTotalEpicsCost() {
		int totalGemCost = 0;
		for(Card card : Hub.getCardlist().values()) {
			switch(card.getType()) {
			case EPIC: 
				totalGemCost += 1200;
				break;
			}
		}
		return totalGemCost;
	}

	private static int getTotalRaresCost() {
		int totalGemCost = 0;
		for(Card card : Hub.getCardlist().values()) {
			switch(card.getType()) {
			case RARE: 
				totalGemCost += 300;
				break;
			}
		}
		return totalGemCost;
	}

	private static int getTotalCommonsCost() {
		int totalGemCost = 0;
		for(Card card : Hub.getCardlist().values()) {
			switch(card.getType()) {
			case COMMON: 
				totalGemCost += 150;
				break;
			}
		}
		return totalGemCost;
	}

	public void becomeLegendary() {
		setMaximum(TotalLegendariesSoulGemCost);
		setForeground(ColorSet.ProgressPanelLEGENDARY);
	}
	
	public void becomeEpic() {
		setMaximum(TotalEpicsSoulGemCost);
		setForeground(ColorSet.ProgressPanelEPIC);
	}
	
	public void becomeRare() {
		setMaximum(TotalRaresSoulGemCost);
		setForeground(ColorSet.ProgressPanelRARE);
		
	}
	
	public void becomeCommon() {
		setMaximum(TotalCommonsSoulGemCost);
		setForeground(ColorSet.ProgressPanelCOMMON);
	}
	
}
