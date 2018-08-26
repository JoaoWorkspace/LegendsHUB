package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import framework.Framework;
import resources.Alerts;

public class Requester{
	private ObjectOutputStream outputstream;
	
	public Requester(ObjectOutputStream ClientOutputstream) {
		outputstream=ClientOutputstream;
	}
	
	public void issueCommand(String command) {
		try {
			outputstream.writeObject(command);
			if(command.equals("UPDATE")) Alerts.thisProcessMightTakeSeveralSeconds();
		} catch (IOException e) {
			Alerts.serverWentOffline();
		}
	}
}
