package objects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

public class Card {
	private String name;
	private CardType type;
	private int rating;
	private int missing;
	private String set;
	private String pathToImage;

	
	public Card(String name, CardType type, int rating, int missing, String set) {
		this.name=name;
		this.type=type;
		this.rating=rating;
		this.missing=missing;
		this.set=set;
		if(type.equals(CardType.UNIQUELEGENDARY)) pathToImage = "utility/cards/Legendary/"+name+".png";
		else pathToImage = "utility/cards/"+type+"/"+name+".png";
	}
	
	public String getName() {
		return name;
	}
	
	public CardType getType() {
		return type;
	}
	
	public int getRating() {
		return rating;
	}
	
	public int getMissing() {
		return missing;
	}
	
	public String getSet() {
		return set;
	}
	
	public void reduceMissing() {
		if(missing>0)missing--;
	}
	
	public void setMissing(int missing) {
		this.missing = missing;
	}
	
	public String getPathToImage() {
		return pathToImage;
	}
	
	public BigInteger getCraftValue() {
		BigInteger craftValue = BigInteger.valueOf(type.getBaseValue());
		craftValue = craftValue.multiply(BigInteger.valueOf(rating)).multiply(BigInteger.valueOf(missing));
		return craftValue;
	}
}
