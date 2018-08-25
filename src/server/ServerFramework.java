package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import resources.Alerts;

/**
 * 
 * @author João Mendonça
 *
 */
public class ServerFramework extends JFrame{
	private static final Dimension DIMENSION = new Dimension(1000,1000);
	private static final JEditorPane ClientMonitor = new JEditorPane("text/html","<html><head></head><body></body></html>");
	private static final JScrollPane JS = new JScrollPane(ClientMonitor);
	private static final ServerFramework SERVERFRAMEWORK = new ServerFramework();
	
	
	private ServerFramework() {
		setTitle("TESLCraftRoulette Server");
		setPreferredSize(DIMENSION);
		createFramework();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void createFramework() {
		add(JS);
	}
	
	public void addLog(String newLine) {
		HTMLEditorKit editor = (HTMLEditorKit) ClientMonitor.getEditorKit();
		StringReader reader = new StringReader(newLine);
		  try {
			editor.read(reader, ClientMonitor.getDocument(), ClientMonitor.getDocument().getLength());
		} catch (IOException | BadLocationException e) {
			Alerts.launchException(e.getMessage());
		}
	}
	
	public static ServerFramework getInstance() {
		return SERVERFRAMEWORK;
	}
}
