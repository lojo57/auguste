package proelio_serveur;

import game.MainGame;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;

public class BroadcastLobby extends Thread
{
	//liste des messages qui n'ont pas encore été envoyés
	private Vector<String> mMessageQueue = new Vector<String>();
	
	//listes des joueurs dans le lobby
	private Vector<ClientInfo> mClients = new Vector<ClientInfo>();
	
	//liste des parties
	private Vector<PartieInfo> mParties = new Vector<PartieInfo>();
	
	//sert à découper une chaine de caractères via le caractère ';'
	private String slash=";";
	private Pattern pattern = Pattern.compile(slash);

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
	
	public synchronized boolean existLogin(String login)
	{
		boolean res=false;
		for (int i=0; i<mClients.size(); i++)
		{
			if (login.equals(mClients.get(i).login))
			{
				res=true;
			}
		}
		return res;
	}

	public synchronized void testLogin(ClientInfo mClientInfo, String login)
	{
		if (!existLogin(login))
		{
			mClientInfo.login=login;
			mClientInfo.emissionLobby.sendMessage("###OK###");
		}
		else
		{
			mClientInfo.emissionLobby.sendMessage("###DEJAUTILISE###");
		}
	}

	private synchronized boolean existPartie(String nomPartie)
	{

		boolean res=false;
		for (int i=0; i<mParties.size(); i++)
		{
			if (nomPartie.equals(mParties.get(i).nomPartie))
			{
				res=true;
			}
		}
		return res;
	}

	public synchronized void testPartie(ClientInfo mClientInfo, String options)
	{
		
		String[] option = pattern.split(options);
		if(!existPartie(option[0]))
		{
			creerPartie(mClientInfo, option[0], Integer.parseInt(option[1]), Integer.parseInt(option[2]));
			mClientInfo.emissionLobby.sendMessage("###OK###");
		}
		else
		{
			mClientInfo.emissionLobby.sendMessage("###DEJAUTILISE###");
		}
	}

	public synchronized void creerPartie(ClientInfo mClientInfo, String nomPartie, int nbJoueurs, int baseHexa)
	{
		PartieInfo pi= new PartieInfo();
		pi.broadcast = new Broadcast(nbJoueurs, 2);
		pi.broadcast.start();
		pi.mainGame = new MainGame(pi.broadcast, nbJoueurs, baseHexa);
		pi.mainGame.start();
		pi.createur=mClientInfo.login;
		pi.nomPartie=nomPartie;
		pi.nbJoueurs=0;
		pi.nbJoueursMax=nbJoueurs;
		pi.baseHexa=baseHexa;
		mParties.add(pi);
	}

	public synchronized void rejoindrePartie(ClientInfo mClientInfo, String nomPartie)
	{
		try
		{
			boolean res=false;
			for(int i=0; i<mParties.size(); i++)
			{
				PartieInfo pi = mParties.get(i);
				if(pi.nomPartie.equals(nomPartie))
				{
					if(pi.nbJoueurs<pi.nbJoueursMax)
					{
						
						try
						{
							Connexion connexion = new Connexion(pi.broadcast, mClientInfo, pi.nbJoueurs);
							connexion.start();
							
							mClientInfo.emissionLobby.sendMessage("###OK###");
							mClientInfo.emissionLobby.sendMessage(pi.nbJoueursMax+";"+pi.baseHexa);
							pi.nbJoueurs++;
						}
						catch (IOException e)
						{
							System.out.println(e.getMessage());
						}
					}
					else
					{
						mClientInfo.emissionLobby.sendMessage("###PLEIN###");
					}
					res=true;
					break;
				}
			}
			if(!res)
			{
				mClientInfo.emissionLobby.sendMessage("###PLUS###");
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			mClientInfo.emissionLobby.sendMessage("###ERREURSERVEUR###");
			System.out.println(e.getMessage());
		}
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
		aMessage = "<"+aClientInfo.login+"> " + aMessage;
		mMessageQueue.add(aMessage);
		notify();
	}
	
	private synchronized String getNextMessageFromQueue() throws InterruptedException
	{
		while (mMessageQueue.size()==0)
		{
			wait();
		}
		String message = (String) mMessageQueue.get(0);
		mMessageQueue.removeElementAt(0);
		return message;
	}

	private synchronized void sendMessageToAllClients(String aMessage)
	{
		for (int i=0; i<mClients.size(); i++)
		{
			ClientInfo clientInfo = (ClientInfo) mClients.get(i);
			clientInfo.emissionMsg.sendMessage(aMessage);
		}
	}

	public void run()
	{
		try
		{
			while (true)
			{
				String message = getNextMessageFromQueue();
				sendMessageToAllClients(message);
			}
		}
		catch (InterruptedException ie)
		{
		}
	}

	public synchronized void besoinListe(ClientInfo mClientInfo)
	{
		for(int i=0; i<mParties.size(); i++)
		{
			PartieInfo pi=mParties.get(i);
			if(pi.nbJoueurs<pi.nbJoueursMax)
			{
				String message = pi.nomPartie+";"+pi.nbJoueurs+"/"+pi.nbJoueursMax+";"+pi.createur;
				mClientInfo.emissionLobby.sendMessage(message);
			}
		}
		mClientInfo.emissionLobby.sendMessage("###FINLISTE###");
	}
}
