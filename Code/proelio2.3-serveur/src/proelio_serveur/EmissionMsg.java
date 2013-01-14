package proelio_serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class EmissionMsg extends Thread
{
	private Vector<String> mMessageQueue = new Vector<String>();

	private Broadcast broadcast;
	private ClientInfo mClientInfo;
	private PrintWriter mOut;

	public EmissionMsg(ClientInfo aClientInfo, Broadcast aBroadcast) throws IOException
	{
		mClientInfo = aClientInfo;
		broadcast = aBroadcast;
		Socket socketMsg = aClientInfo.mSocketMsg;
		mOut = new PrintWriter(new OutputStreamWriter(socketMsg.getOutputStream()));
	}

	public synchronized void sendMessage(String aMessage)
	{
		mMessageQueue.add(aMessage);
		notify();
	}

	private synchronized String getNextMessageFromQueue() throws InterruptedException
	{
		while (mMessageQueue.size()==0)
			wait();
		String message = (String) mMessageQueue.get(0);
		mMessageQueue.removeElementAt(0);
		return message;
	}

	private void sendMessageToClient(String aMessage)
	{
		mOut.println(aMessage);
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
		mClientInfo.receptionMsg.interrupt();
		broadcast.deleteClient(mClientInfo);
	}
}