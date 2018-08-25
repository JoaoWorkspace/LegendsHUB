package framework;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import objects.CardType;
import resources.ColorSet;
import resources.RescaledImageIcon;

public class CardCollectionProgressPanel extends CustomPanel{
	private final CardProgressBar progressBar = new CardProgressBar();
	private final JLabel soulgem = buildSoulGem();
	private final JLabel label = new JLabel("     Full Collection    ");
	
	/**
	 * This panel shows all the relevant information related to collection progress and shows up different statistics relevant to the user. 
	 * @author João Mendonça
	 */
	public CardCollectionProgressPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT,-5,0));
		setBackground(ColorSet.ControlPanelElementsBackground);
		add(label);
		add(progressBar);
		add(soulgem);
	}
	
	private JLabel buildSoulGem() {
		JLabel built = new JLabel();
		RescaledImageIcon rescaler = new RescaledImageIcon("utility/icons/soulgem.png",30,30);
		built = new JLabel(rescaler.getRESCALEDICON());
		built.setText(progressBar.getValue()+"/"+progressBar.getMaximum()+" Souls");
		built.setOpaque(false);
		return built;
	}

	public void customize(CardType rarity) {
		switch(rarity) {
		case UNIQUELEGENDARY:
		case LEGENDARY:
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
		return progressBar;
	}
	

}
