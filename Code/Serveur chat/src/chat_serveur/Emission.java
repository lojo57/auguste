package chat_serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class Emission extends Thread
{
	private Vector<String> mMessageQueue = new Vector<String>();

	private Broadcast broadcast;
	private ClientInfo mClientInfo;
	private PrintWriter mOut;

	public Emission(ClientInfo aClientInfo, Broadcast aBroadcast)
	throws IOException
	{
		mClientInfo = aClientInfo;
		broadcast = aBroadcast;
		Socket socket = aClientInfo.mSocket;
		mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
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
		try {
			while (!isInterrupted()) {
				String message = getNextMessageFromQueue();
				sendMessageToClient(message);
			}
		} catch (Exception e) {
			// Commuication problem
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.reception.interrupt();
		broadcast.deleteClient(mClientInfo);
	}
}