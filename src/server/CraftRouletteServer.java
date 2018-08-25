package server;

import java.io.*;
import java.net.*;
import java.util.*;

import resources.Alerts;

public class CraftRouletteServer {
	private static ServerSocket SOCKET;
	/*OBJECTS*/
	private static final ServerFramework SERVERFRAMEWORK = ServerFramework.getInstance();
	private static final ArrayList<Socket> CLIENTS = new ArrayList<Socket>();
	/*INSTANCE*/
	private static final CraftRouletteServer SERVER = new CraftRouletteServer();
	
	

	private CraftRouletteServer() {
		try {
			initializeServer();
			handleConnections();
		} catch (IOException e) {
			 SERVERFRAMEWORK.dispose();
			Alerts.serverAlreadyRunning();
		
		}
	}

	private void initializeServer() throws IOException {
		SOCKET = new ServerSocket(2000);
	}

	private void handleConnections() throws IOException {
		new ClientAccepter().start();
	}
	
	public static void disconnectClient(Socket socket) {
		CLIENTS.remove(socket);
	}
	
	public static ArrayList<Socket> getClients() {
		return CLIENTS;
	}
	
	public static ServerSocket getServerSocket() {
		return SOCKET;
	}

	public static CraftRouletteServer getInstance() {
		return SERVER;
	}

	public static void main(String[] args) {
		CraftRouletteServer.getInstance();
	}
}
