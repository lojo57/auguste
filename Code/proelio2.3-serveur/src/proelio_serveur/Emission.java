package proelio_serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class Emission extends Thread
{
	private Vector<String> mMessageQueue = new Vector<String>();

	private Broadcast broadcast;
	private ClientInfo mClientInfo;
	private PrintWriter mOut;

	public Emission(ClientInfo aClientInfo, Broadcast aBroadcast) throws IOException
	{
		mClientInfo = aClientInfo;
		broadcast = aBroadcast;
		Socket socket = aClientInfo.mSocket;
		mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	private synchronized String getNextMessageFromQueue() throws InterruptedException
	{
		while (mMessageQueue.size()==0)
			wait();
		String message = (String) mMessageQueue.get(0);
		mMessageQueue.removeElementAt(0);
		return message;
	}

	public void sendCoups(String coups)
	{
		mOut.println(coups);
		mOut.flush();
	}

	public void run()
	{
		try
		{
			while (!isInterrupted())
			{
				String message = getNextMessageFromQueue();
				sendCoups(message);
			}
		}
		catch (Exception e)
		{
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.reception.interrupt();
		broadcast.deleteClient(mClientInfo);
	}
}