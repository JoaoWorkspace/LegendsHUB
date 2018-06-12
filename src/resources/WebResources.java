package resources;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class WebResources {

	/**
	 * Opens a website with the given link in your default browser
	 * @author João Mendonça
	 * @param websiteURL - Link URL to the website.
	 */
	public void openWebsite(String websiteURL) {
		try {         
			Desktop.getDesktop().browse(URI.create(websiteURL));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
