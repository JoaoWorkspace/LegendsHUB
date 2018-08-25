package resources;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UseTheLookAndFeelYouWant {
	private static LookAndFeel PERMANENT_LOOK_AND_FEEL;

	public static void useYourSystemLookAndFeel() {
		try {
			PERMANENT_LOOK_AND_FEEL = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException error) {}
	}

	public static void useCrossPlatformLookAndFeel() {
		try {
			PERMANENT_LOOK_AND_FEEL = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException error) {}
	}

	public static void revert() {
		try {
			UIManager.setLookAndFeel(PERMANENT_LOOK_AND_FEEL);
		} catch (UnsupportedLookAndFeelException e) {}
	}
}
