package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import resources.Alerts;

public class ClientAccepter extends Thread{
	private static final ServerSocket socket = CraftRouletteServer.getServerSocket();

	private void acceptClient() throws IOException {
		Socket newClientConnected = socket.accept();
		CraftRouletteServer.getClients().add(newClientConnected);
		ServerFramework.getInstance().addLog("<font color=green><b>"+newClientConnected.getPort()+"</b></font>: Logged on. Currently "+CraftRouletteServer.getClients().size()+" clients active.");
		new Updater(newClientConnected).start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				acceptClient();
			} catch (IOException e) {Alerts.launchException(e.getMessage());}
		}
	}
}
