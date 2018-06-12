package framework;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import objects.CardType;
import resources.RescaledImageIcon;
import resources.WebResources;

public class ControlPanel extends JPanel{
	
	private static final Dimension DIMENSION = new Dimension(450,640);
	
	private static final JRadioButton toggleLegendary = new JRadioButton("Legendary");
	private static final JRadioButton toggleEpic = new JRadioButton("Epic");
	private static final JRadioButton toggleRare = new JRadioButton("Rare");
	private static final JRadioButton toggleCommon = new JRadioButton("Common");
	private static final JRadioButton setCore = new JRadioButton("Core");
	private static final JRadioButton setHeroesOfSkyrim = new JRadioButton("Heroes of Skyrim");
	private static final JRadioButton setHousesOfMorrowind = new JRadioButton("Houses Of Morrowind");
	private static final JRadioButton setMonthlyRewards = new JRadioButton("Monthly Rewards");
	
	private static final JPanel rarityFiltering = new JPanel();
	private static final JPanel setFiltering = new JPanel();
	private static final JPanel setFiltering2 = new JPanel();
	
	private static final CardCollectionManagementPanel collectionManagement = CardCollectionManagementPanel.getInstance();
	
	public static final CardCollectionProgressPanel FullCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel LegendaryCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel EpicCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel RareCollectionProgress = new CardCollectionProgressPanel();
	public static final CardCollectionProgressPanel CommonCollectionProgress = new CardCollectionProgressPanel();
	
	private static final ControlPanel ControlPanel = new ControlPanel();
	
	private ControlPanel() {
		super(new GridLayout(16,1));
		setPreferredSize(DIMENSION);
		
		addRarityFiltering();
		addSetFiltering();
		addCollectionManagement();
		addCollectionProgress();
		addLinks();
	}
	
	private void addRarityFiltering() {
		CustomLabel topic = new CustomLabel(" Filter Rarity");
		
		rarityFiltering.add(toggleLegendary);
		rarityFiltering.add(toggleEpic);
		rarityFiltering.add(toggleRare);
		rarityFiltering.add(toggleCommon);
		
		toggleLegendary.setSelected(true);
		toggleEpic.setSelected(true);
		toggleRare.setSelected(true);
		toggleCommon.setSelected(true);
		
		add(topic);
		add(rarityFiltering);
	}

	private void addSetFiltering() {
		CustomLabel topic = new CustomLabel(" Filter Set");
		
		setFiltering.add(setCore);
		setFiltering.add(setHeroesOfSkyrim);
		setFiltering.add(setHousesOfMorrowind);
		setFiltering2.add(setMonthlyRewards);
		
		setCore.setSelected(true);
		setHeroesOfSkyrim.setSelected(true);
		setHousesOfMorrowind.setSelected(true);
		setMonthlyRewards.setSelected(true);
		
		add(topic);
		add(setFiltering);
		add(setFiltering2);
	}

	private void addCollectionManagement() {
		CustomLabel topic = new CustomLabel(" Manage your Collection");
		add(topic);
		add(collectionManagement);
	}

	private void addCollectionProgress() {
		CustomLabel topic = new CustomLabel(" Your Collection Progress");
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
		CustomLabel topic = new CustomLabel(" Links");
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
		linkpanel.add(Link);
		return linkpanel;
	}
	
	public static JRadioButton getToggleCommon() {
		return toggleCommon;
	}
	
	public static JRadioButton getToggleRare() {
		return toggleRare;
	}
	
	public static JRadioButton getToggleEpic() {
		return toggleEpic;
	}
	
	public static JRadioButton getToggleLegendary() {
		return toggleLegendary;
	}
	
	public static JRadioButton getSetMonthlyRewards() {
		return setMonthlyRewards;
	}
	
	public static JRadioButton getSetCore() {
		return setCore;
	}
	
	public static JRadioButton getSetHeroesOfSkyrim() {
		return setHeroesOfSkyrim;
	}
	
	public static JRadioButton getSetHousesOfMorrowind() {
		return setHousesOfMorrowind;
	}
	
	public static Dimension getDimension() {
		return DIMENSION;
	}
	
	public static ControlPanel getInstance() {
		return ControlPanel;
	}
}
