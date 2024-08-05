package client;

import framework.CustomLabel;
import framework.Framework;
import framework.Menu;
import hub.Hub;
import resources.Alerts;
import resources.FileLister;
import resources.UseTheLookAndFeelYouWant;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * 
 * @author Jo�o Mendon�a
 *
 */
public class Client extends Thread{
	/*SERVER CONNECTION*/
	private static final String ServerIP = "0.0.0.0";
	private static final int ServerPORT = 2000;
	/*TCP/IP CONNECTION*/
	private static Socket socket;
	private static ObjectOutputStream outputstream;
	private static ObjectInputStream inputstream;
	private static Requester REQUESTER;
	/*LOADING FRAMEWORK*/
	private JDialog LOADING_CONNECTION;
	private JDialog LOADING_UPDATES;
	private JProgressBar LOADING;
	/*INSTANCE*/
	private static final Client CLIENT = new Client();

	public Client() {
	}

	public void startLoadingForConnection() {
		UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
		LOADING_CONNECTION = new JDialog(Framework.getInstance(), "Connecting");
		
		CustomLabel attemptReconnect = new CustomLabel("Attempting to connect to the Server (20 seconds)",true);
		LOADING_CONNECTION.add(attemptReconnect,BorderLayout.NORTH);
		
		LOADING = new JProgressBar(SwingConstants.HORIZONTAL);
		LOADING.setIndeterminate(true);
		LOADING_CONNECTION.add(LOADING);
		
		LOADING_CONNECTION.setSize(400,100);
		LOADING_CONNECTION.setDefaultCloseOperation(LOADING_CONNECTION.DO_NOTHING_ON_CLOSE);
		LOADING_CONNECTION.setLocationRelativeTo(null);
		LOADING_CONNECTION.setResizable(false);
		LOADING_CONNECTION.setVisible(true);
		UseTheLookAndFeelYouWant.revert();
	}
	
	private void startLoadingForUpdates(int numberOfFiles) {
		UseTheLookAndFeelYouWant.useYourSystemLookAndFeel();
		LOADING_UPDATES = new JDialog(Framework.getInstance(), "Loading");
		
		CustomLabel searchingUpdates = new CustomLabel("Searching for Updates",true);
		LOADING_UPDATES.add(searchingUpdates,BorderLayout.NORTH);
		
		LOADING = new JProgressBar(SwingConstants.HORIZONTAL);
		LOADING.setMaximum(numberOfFiles);
		LOADING.setStringPainted(true);
		LOADING_UPDATES.add(LOADING);
		
		LOADING_UPDATES.setSize(400,100);
		LOADING_UPDATES.setDefaultCloseOperation(LOADING_CONNECTION.DO_NOTHING_ON_CLOSE);
		LOADING_UPDATES.setLocationRelativeTo(null);
		LOADING_UPDATES.setResizable(false);
		LOADING_UPDATES.setVisible(true);
		UseTheLookAndFeelYouWant.revert();
	}

	private void connectServer() {
		try {
			socket = new Socket(ServerIP,ServerPORT);
			inputstream = new ObjectInputStream(socket.getInputStream());
			outputstream = new ObjectOutputStream(socket.getOutputStream());
			REQUESTER = new Requester(outputstream);
			Menu.getInstance().setServerOn();
		} catch (IOException e) {
			Menu.getInstance().setServerOff();
			Alerts.serverOffline();
		}finally{ 
			LOADING_CONNECTION.dispose();
		}
	}

	private void checkForUpdatesOnServer(){
		try {
			compareServerFiles(getServerFiles());
		} catch (ClassNotFoundException | IOException e) {
			Alerts.serverWentOffline();
			closeSocket();
		}finally {
			LOADING_UPDATES.dispose();
		}
	}

	private HashMap<File,byte[]> getServerFiles() throws ClassNotFoundException, IOException {
		HashMap<File,byte[]> ServerFilesData = (HashMap<File,byte[]>) inputstream.readObject();
		return ServerFilesData;
	}

	private void compareServerFiles(HashMap<File,byte[]> ServerFiles) {
		startLoadingForUpdates(ServerFiles.size());
		FileLister myUtilityFiles = new FileLister(new File("utility"));
		if(!compareUtilityFolders(ServerFiles,myUtilityFiles)) {
			Alerts.updatesAreAvailable(ServerFiles);
		}
		else {
			REQUESTER.issueCommand("UP-TO-DATE");
			Alerts.noUpdatesAvailable();
		}
	}

	private boolean compareUtilityFolders(HashMap<File,byte[]> ServerFiles, FileLister ClientFiles) {
		if(ClientFiles.size()!=ServerFiles.size()) return false;
		HashMap<File,byte[]> ClientFilesData = ClientFiles.getFileData();
		boolean result = true;
		for(File file : ClientFiles) {
			LOADING.setValue(LOADING.getValue()+1);
			if(ClientFilesData.get(file).length!=ServerFiles.get(file).length) return false;
			for(int i=0;i<ClientFilesData.get(file).length;i++) {
				byte clientbyte = ClientFilesData.get(file)[i];
				byte serverbyte = ServerFiles.get(file)[i];
				if(clientbyte!=serverbyte) {
					System.out.println("File: "+file.getName()+" Client Bytes: "+clientbyte+" Server Bytes: "+serverbyte);
					result = false;
				}
			}
		}
		return result;
	}

	public static void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			Alerts.launchException(e.getMessage());
		}
	}

	public static Socket getClientSocket() {
		return socket;
	}

	public static Requester getREQUESTER() {
		return REQUESTER;
	}

	public static Client getInstance() {
		return CLIENT;
	}

	@Override
	public void run() {
		Hub.getInstance();
		while(true) { //While true to ensure the client can try to reconnect aftet the server goes offline
			while(socket==null || outputstream==null || inputstream==null) {
				if(Menu.READY_TO_ATTEMPT_RECONNECT) {
					startLoadingForConnection();
					connectServer();
				}
			}
			while(socket!=null && !socket.isClosed()) {
				checkForUpdatesOnServer();
			}
			if(socket!=null && socket.isClosed()) {
				Menu.getInstance().setServerOff();
				socket=null;
				outputstream=null;
				inputstream=null;
			}
		}
	}

	public static void main(String[] args) {
		Client.getInstance().start();

	}

}
