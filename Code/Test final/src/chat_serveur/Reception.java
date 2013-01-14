package chat_serveur;

import java.io.*;
import java.net.*;

public class Reception extends Thread
{
	private Broadcast broadcast;
	private ClientInfo mClientInfo;
	private BufferedReader mIn;

	public Reception(ClientInfo aClientInfo, Broadcast aBroadcast)
	throws IOException
	{
		mClientInfo = aClientInfo;
		broadcast = aBroadcast;
		Socket socket = aClientInfo.mSocket;
		mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void run()
	{
		try {
			while (!isInterrupted()) {
				String message = mIn.readLine();
				if (message == null)
					break;
				broadcast.dispatchMessage(mClientInfo, message);
			}
		} catch (IOException ioex) {
			// Problem reading from socket (communication is broken)
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.emission.interrupt();
		broadcast.deleteClient(mClientInfo);
	}
}
