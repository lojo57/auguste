package chat_serveur;

import java.net.*;
import java.util.*;


public class Broadcast extends Thread
{
	private Vector<String> mMessageQueue = new Vector<String>();
	private Vector<ClientInfo> mClients = new Vector<ClientInfo>();

	public synchronized void addClient(ClientInfo aClientInfo)
	{
		mClients.add(aClientInfo);
	}

	public synchronized void deleteClient(ClientInfo aClientInfo)
	{
		int clientIndex = mClients.indexOf(aClientInfo);
		if (clientIndex != -1)
			mClients.removeElementAt(clientIndex);
	}
	
	public boolean existLogin(String login)
	{
		boolean res=false;
		for (int i=0; i<mClients.size(); i++)
		{
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			if (login.equals(clientInfo.login))
			{
				res=true;
			}
		}
		return res;
	}
	
	public synchronized void afficherClients()
	{
		for (int i=0; i<mClients.size(); i++)
		{
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			System.out.println(clientInfo.login);
		}
	}

	public synchronized void dispatchMessage(ClientInfo aClientInfo, String aMessage)
	{
		Socket socket = aClientInfo.mSocket;
		aMessage = "<"+aClientInfo.login+"> " + aMessage;
		mMessageQueue.add(aMessage);
		System.out.println(aMessage);
		notify();
	}

	private synchronized String getNextMessageFromQueue()
	throws InterruptedException
	{
		while (mMessageQueue.size()==0)
			wait();
		String message = (String) mMessageQueue.get(0);
		mMessageQueue.removeElementAt(0);
		return message;
	}

	private synchronized void sendMessageToAllClients(String aMessage)
	{
		for (int i=0; i<mClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			clientInfo.emission.sendMessage(aMessage);
		}
	}

	public void run()
	{
		try {
			while (true) {
				String message = getNextMessageFromQueue();
				sendMessageToAllClients(message);
			}
		} catch (InterruptedException ie) {
			// Thread interrupted. Stop its execution
		}
	}
}
