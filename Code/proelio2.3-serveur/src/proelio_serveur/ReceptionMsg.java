package proelio_serveur;

import java.io.*;
import java.net.*;

public class ReceptionMsg extends Thread
{
	private Broadcast broadcast;
	private ClientInfo mClientInfo;
	private BufferedReader mIn;

	public ReceptionMsg(ClientInfo aClientInfo, Broadcast aBroadcast) throws IOException
	{
		mClientInfo = aClientInfo;
		broadcast = aBroadcast;
		Socket socketMsg = aClientInfo.mSocketMsg;
		mIn = new BufferedReader(new InputStreamReader(socketMsg.getInputStream()));
	}

	public void run()
	{
		try
		{
			while (!isInterrupted())
			{
				String message = mIn.readLine();
				if (message == null)
					break;
				broadcast.dispatchMessage(mClientInfo, message);
			}
		}
		catch (IOException ioex)
		{
			// Problem reading from socket (communication is broken)
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.emissionMsg.interrupt();
		broadcast.deleteClient(mClientInfo);
	}
}
