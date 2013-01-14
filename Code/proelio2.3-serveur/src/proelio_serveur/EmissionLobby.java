package proelio_serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class EmissionLobby extends Thread
{
	private Vector<String> mMessageQueue = new Vector<String>();

	private BroadcastLobby broadcastLobby;
	private ClientInfo mClientInfo;
	private PrintWriter mOut;

	public EmissionLobby(ClientInfo aClientInfo, BroadcastLobby bcLobby) throws IOException
	{
		mClientInfo = aClientInfo;
		broadcastLobby = bcLobby;
		Socket socket = aClientInfo.mSocketLobby;
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
	
	public synchronized void sendMessage(String aMessage)
	{
		mMessageQueue.add(aMessage);
		notify();
	}

	public void sendMessageToClient(String message)
	{
		mOut.println(message);
		mOut.flush();
	}

	public void run()
	{
		try
		{
			while (!isInterrupted())
			{
				String message = getNextMessageFromQueue();
				sendMessageToClient(message);
			}
		}
		catch (Exception e)
		{
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.receptionLobby.interrupt();
		broadcastLobby.deleteClient(mClientInfo);
	}
}