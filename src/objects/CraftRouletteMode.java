package objects;

import java.util.ArrayList;

import framework.RarityTogglePanel;
import framework.SetTogglePanel;

/**
 * The CraftRouletteMode is an enumerator of the different modes of how to approach the randomizer - the Roulette.
 * How it will be randomized/filtered will be decided depending on the {@link #CraftRouletteMode()}.
 * <p><b>HELP</b> - Selects a random card from the best possible cards to craft(Or THE best).
 * <p><b>NORMAL</b> - Normal functioning, selects a random card from ALL the possible cards, each with each own weight. Filters out all the cards whose rating is below the best.
 * <p><b>CHAOS</b> - ALL the cards have the same weight, there's no good or bad, take the risk and craft whatever shows up!
 * @author João Mendonça
 */
public enum CraftRouletteMode {
	HELPFUL,NORMAL,CHAOS;
}
