package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.ButtonUI;

import client.Client;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import resources.RescaledImageIcon;

public class Menu extends JMenuBar{
	/*COMPONENTS*/ 
	private static final JButton UPDATE_MENU = new JButton("Check For Updates");
	private static final ImageIcon UPDATES_IMAGE = new RescaledImageIcon("utility/icons/transfer.png", 50, 50).getRESCALEDICON();
	private static final JButton SERVER_STATE_MENU = new JButton("Server Connection");
	private static final ImageIcon SERVER_STATE_ON = new RescaledImageIcon("utility/icons/connection ON.png", 50, 50).getRESCALEDICON();
	private static final ImageIcon SERVER_STATE_OFF = new RescaledImageIcon("utility/icons/connection OFF.png", 50, 50).getRESCALEDICON();
	private static final JButton SWAP_TO_MENU = new JButton("Swap to CraftRoulette");
	private static final ImageIcon SWAP_TO_CRAFTROULETTE = new RescaledImageIcon("utility/icons/roulette.png", 50, 50).getRESCALEDICON();
	private static final ImageIcon SWAP_TO_METADECKS = new RescaledImageIcon("utility/icons/water deck.png",50,50).getRESCALEDICON();
	/*CONDITIONS*/
	public static volatile boolean READY_TO_ATTEMPT_RECONNECT = false;
	/*INSTANCE*/
	private static final Menu MENU = new Menu();
	
	private Menu() {
		customizeMenus();
		addMenusToMenuBar();
		setServerOff();
	}
	
	private void customizeMenus() {
		UPDATE_MENU.setIcon(UPDATES_IMAGE);
		UPDATE_MENU.setFocusPainted(false);
		UPDATE_MENU.setContentAreaFilled(false);
		UPDATE_MENU.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		UPDATE_MENU.setBorderPainted(false);
		UPDATE_MENU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.getREQUESTER().issueCommand("UPDATE");
			}
		});
		UPDATE_MENU.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
				UPDATE_MENU.setBorderPainted(false);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				UPDATE_MENU.setBorderPainted(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		SERVER_STATE_MENU.setIcon(SERVER_STATE_OFF);
		SERVER_STATE_MENU.setDisabledIcon(SERVER_STATE_ON);
		SERVER_STATE_MENU.setFocusPainted(false);
		SERVER_STATE_MENU.setContentAreaFilled(false);
		SERVER_STATE_MENU.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		SERVER_STATE_MENU.setBorderPainted(false);
		SERVER_STATE_MENU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(SERVER_STATE_MENU.getIcon().equals(SERVER_STATE_OFF)) {
					READY_TO_ATTEMPT_RECONNECT = true;
				}
			}
		});
		SERVER_STATE_MENU.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				SERVER_STATE_MENU.setBorderPainted(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				SERVER_STATE_MENU.setBorderPainted(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		SWAP_TO_MENU.setIcon(SWAP_TO_CRAFTROULETTE);
		SWAP_TO_MENU.setFocusPainted(false);
		SWAP_TO_MENU.setContentAreaFilled(false);
		SWAP_TO_MENU.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		SWAP_TO_MENU.setBorderPainted(false);
		SWAP_TO_MENU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(SWAP_TO_MENU.getIcon().equals(SWAP_TO_CRAFTROULETTE)) {
					SWAP_TO_MENU.setIcon(SWAP_TO_METADECKS);
					SWAP_TO_MENU.setText("Swap to MetaDecks");
					Framework.swap(true);
					ControlPanel.getInstance().setFiltersOn();
				}
				else {
					SWAP_TO_MENU.setIcon(SWAP_TO_CRAFTROULETTE);
					SWAP_TO_MENU.setText("Swap to CraftRoulette");
					Framework.swap(false);
					ControlPanel.getInstance().setFiltersOff();
				}
			}
		});
		SWAP_TO_MENU.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
				SWAP_TO_MENU.setBorderPainted(false);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				SWAP_TO_MENU.setBorderPainted(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
	
	public void setServerOn(){
		READY_TO_ATTEMPT_RECONNECT = false;
		SERVER_STATE_MENU.setEnabled(false);
		UPDATE_MENU.setEnabled(true);
		
	}
	
	public void setServerOff(){
		READY_TO_ATTEMPT_RECONNECT = false;
		SERVER_STATE_MENU.setEnabled(true);
		UPDATE_MENU.setEnabled(false);
	}
	
	private void addMenusToMenuBar(){
		add(SERVER_STATE_MENU);
		add(UPDATE_MENU);
		add(SWAP_TO_MENU);
	}
	
	public static Menu getInstance() {
		return MENU;
	}
}
