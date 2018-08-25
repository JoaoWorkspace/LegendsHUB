package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import objects.CardType;
import resources.ColorSet;
import resources.RescaledImageIcon;
import resources.WebResources;

public class ControlPanel extends JPanel{
	
	
	private static final RarityTogglePanel rarityFiltering = RarityTogglePanel.getInstance();
	private static final SetTogglePanel setFiltering = SetTogglePanel.getFirstInstance();
	private static final SetTogglePanel setFiltering2 = SetTogglePanel.getSecondInstance();
	
	private static final CardCollectionManagementPanel collectionManagement1 = CardCollectionManagementPanel.getFirstInstance();
	private static final CardCollectionManagementPanel collectionManagement2 = CardCollectionManagementPanel.getSecondInstance();
	
	public static final CardCollectionProgressPanel FullCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel LegendaryCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel EpicCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel RareCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel CommonCollectionProgress = new CardCollectionProgressPanel();
	
	private static final ControlPanel ControlPanel = new ControlPanel();
	
	private ControlPanel() {
		super(new GridLayout(17,1));
		setBackground(ColorSet.ControlPanelElementsBackground);
		setPreferredSize(Framework.CONTROL_PANEL_DIMENSION);
		
		addRarityFiltering();
		addSetFiltering();
		addCollectionManagement();
		addCollectionProgress();
		addLinks();
		setFiltersOff();
	}
	
	private void addRarityFiltering() {
		CustomLabel topic = new CustomLabel(" Filter Rarity",false);
		add(topic);
		add(rarityFiltering);
	}

	private void addSetFiltering() {
		CustomLabel topic = new CustomLabel(" Filter Set",false);
		add(topic);
		add(setFiltering);
		add(setFiltering2);
	}

	private void addCollectionManagement() {
		CustomLabel topic = new CustomLabel(" Manage your Collection",false);
		add(topic);
		add(collectionManagement1);
		add(collectionManagement2);
	}

	private void addCollectionProgress() {
		CustomLabel topic = new CustomLabel(" Your Collection Progress",false);
		LegendaryCollectionProgress.customize(CardType.LEGENDARY);
		EpicCollectionProgress.customize(CardType.EPIC);
		RareCollectionProgress.customize(CardType.RARE);
		CommonCollectionProgress.customize(CardType.COMMON);
		
		add(topic);
		add(FullCollectionProgress);
		add(LegendaryCollectionProgress);
		add(EpicCollectionProgress);
		add(RareCollectionProgress);
		add(CommonCollectionProgress);
	}
	
	private void addLinks() {
		CustomLabel topic = new CustomLabel(" Links",false);
		add(topic);
		add(getLinkTo("utility/icons/github.png", "JoaoWorkspace","https://github.com/JoaoWorkspace"));
		add(getLinkTo("utility/icons/udt.png", "UniversalDeckTracker/extesy","https://github.com/extesy/DeckTracker"));
	}
	
	/**
	 * Parent method: {@link #addUpLinks()}.
	 * Makes a JPanel to be added to the framework that links to a website.
	 * @param pathToImage - Path to the image (ex: "C:/image.png")
	 * @param linkTitle - Brief Title that represents the Image Link
	 * @param URL - The website that will be open on-click.
	 * @return Returns a JPanel with FlowLayout like the ones that can be observed in the project below "Links"
	 */
	private JPanel getLinkTo(String pathToImage,String linkTitle,String URL) {
		RescaledImageIcon rescaler = new RescaledImageIcon(pathToImage,30,30);
		JLabel Link = new JLabel(rescaler.getRESCALEDICON());
		Link.setText(linkTitle);
		Link.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {	
			}
			@Override
			public void mouseEntered(MouseEvent e) {	
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				WebResources resource = new WebResources();
				resource.openWebsite(URL);
			}
		});
		JPanel linkpanel = new JPanel((new FlowLayout(FlowLayout.LEFT,10,0)));
		linkpanel.setBackground(ColorSet.ControlPanelElementsBackground);
		linkpanel.add(Link);
		return linkpanel;
	}
	
	public void setFiltersOn() {
		rarityFiltering.getToggleCommon().setEnabled(true);
		rarityFiltering.getToggleRare().setEnabled(true);
		rarityFiltering.getToggleEpic().setEnabled(true);
		rarityFiltering.getToggleLegendary().setEnabled(true);
		setFiltering.getSetCore().setEnabled(true);
		setFiltering.getSetHeroesOfSkyrim().setEnabled(true);
		setFiltering.getSetHousesOfMorrowind().setEnabled(true);
		setFiltering.getSetMonthlyRewards().setEnabled(true);
		setFiltering.getSetStarterCards().setEnabled(true);
	}
	
	public void setFiltersOff() {
		rarityFiltering.getToggleCommon().setEnabled(false);
		rarityFiltering.getToggleRare().setEnabled(false);
		rarityFiltering.getToggleEpic().setEnabled(false);
		rarityFiltering.getToggleLegendary().setEnabled(false);
		setFiltering.getSetCore().setEnabled(false);
		setFiltering.getSetHeroesOfSkyrim().setEnabled(false);
		setFiltering.getSetHousesOfMorrowind().setEnabled(false);
		setFiltering.getSetMonthlyRewards().setEnabled(false);
		setFiltering.getSetStarterCards().setEnabled(false);
	}
	
	public static ControlPanel getInstance() {
		return ControlPanel;
	}
}
