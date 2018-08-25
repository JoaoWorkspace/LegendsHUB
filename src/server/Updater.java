package server;

import java.io.*;
import java.net.*;
import java.util.*;

import resources.Alerts;
import resources.FileLister;

public class Updater extends Thread{
	private Socket thisClientsSocket;
	private ObjectOutputStream outputstream;
	private ObjectInputStream inputstream;
	private static final ArrayList<File> FILELIST_UTILITY = new FileLister(new File("utility"));

	/**
	 * The Updater is a thread responsible for handling Client commands, nominately "UPDATE" and "CLOSE".
	 * If there are files missing for the client, the content of utility folder gets updated to match the server, 
	 * the same doesn't apply if the client adds files that the server doesn't have, for security and integrity issues.
	 * @author João Mendonça
	 * @throws IOException If an I/O error occurs while writing/reading stream header
	 */
	public Updater(Socket socket) throws IOException {
		thisClientsSocket = socket;
		outputstream = new ObjectOutputStream(socket.getOutputStream());
		inputstream = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * Closes only if Client disconnects.
	 */
	private void waitForCommand() {
		try {
			String command = (String)inputstream.readObject();
			if(command.equals("UPDATE")) {
				ServerFramework.getInstance().addLog("<font color=green><b>"+thisClientsSocket.getPort()+"</b></font>: Issued an update command.");
				checkIfClientNeedsUpdate();
			}
			if(command.equals("UP-TO-DATE")) ServerFramework.getInstance().addLog("<font color=green><b>"+thisClientsSocket.getPort()+"</b></font>: Already up-to-date.");
			if(command.equals("UPDATED")) ServerFramework.getInstance().addLog("<font color=green><b>"+thisClientsSocket.getPort()+"</b></font>: Updated it's files.");
		} catch (ClassNotFoundException | IOException e) {
			closeSocket();
		}
	}

	private void closeSocket() {
		CraftRouletteServer.disconnectClient(thisClientsSocket);
		try {thisClientsSocket.close();} 
		catch (IOException e1) {Alerts.launchException(e1.getMessage());}
		finally {ServerFramework.getInstance().addLog("<font color=green><b>"+thisClientsSocket.getPort()+"</b></font>: Disconnected.");}
	}

	private void checkIfClientNeedsUpdate() {
		try {
			outputstream.writeObject(new FileLister(new File("utility")).getFileData());
			ServerFramework.getInstance().addLog("<font color=green><b>"+thisClientsSocket.getPort()+"</b></font>: Checking for updates.");
		} catch (IOException e) {
			Alerts.launchException(e.getMessage());
			System.out.println("COULD NOT SEND THE FILE-LIST TO USER: Socket has been closed.");
		}
	}


	@Override
	public void run() {
		while(!thisClientsSocket.isClosed()) {
			waitForCommand();
		}
	}
}
