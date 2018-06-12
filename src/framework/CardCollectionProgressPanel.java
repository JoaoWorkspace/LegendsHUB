package framework;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import javafx.scene.control.ProgressBar;
import main.CraftRoulette;
import objects.Card;
import objects.CardType;
import resources.RescaledImageIcon;
import sun.security.provider.certpath.BuildStep;

public class CardCollectionProgressPanel extends JPanel{
	private final CardProgressBar progressBar = new CardProgressBar();
	private final JLabel soulgem = buildSoulGem();
	private final JLabel label = new JLabel("     Full Collection    ");
	
	/**
	 * This panel shows all the relevant information related to collection progress and shows up different statistics relevant to the user. 
	 * @author João Mendonça
	 */
	public CardCollectionProgressPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT,-5,0));
		add(label);
		add(progressBar);
		add(soulgem);
	}
	
	private JLabel buildSoulGem() {
		JLabel built = new JLabel();
		RescaledImageIcon rescaler = new RescaledImageIcon("utility/icons/soulgem.png",30,30);
		built = new JLabel(rescaler.getRESCALEDICON());
		built.setText(progressBar.getValue()+"/"+progressBar.getMaximum()+" Souls");
		return built;
	}

	public void customize(CardType rarity) {
		System.out.println("Customizing "+rarity);
		switch(rarity) {
		case UNIQUELEGENDARY:
		case LEGENDARY:
			System.out.println("MADE INTO LEGENDARY");
			label.setText("     Legendaries      ");
			progressBar.becomeLegendary();
			soulgem.setText(progressBar.getValue()+"/"+progressBar.getMaximum()+" Souls");
			break;
		case EPIC:
			label.setText("     Epics                   ");
			progressBar.becomeEpic();
			soulgem.setText(progressBar.getValue()+"/"+progressBar.getMaximum()+" Souls");
			break;
		case RARE:
			label.setText("     Rares                  ");
			progressBar.becomeRare();
			soulgem.setText(progressBar.getValue()+"/"+progressBar.getMaximum()+" Souls");
			break;
		case COMMON:
			label.setText("     Commons          ");
			progressBar.becomeCommon();
			soulgem.setText(progressBar.getValue()+"/"+progressBar.getMaximum()+" Souls");
			break;
		}
	}
	
	public JLabel getSoulgem() {
		return soulgem;
	}
	
	public CardProgressBar getProgressBar() {
		System.out.println("Progress Bar"+label.getText()+"\nCurrent Value: "+progressBar.getValue()+" | Maximum Value: "+progressBar.getMaximum());
		return progressBar;
	}
	

}
