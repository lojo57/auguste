package proelio_serveur;

import game.Game;
import java.util.*;
import java.util.regex.Pattern;

public class Broadcast extends Thread
{
	private Vector<String> mMessageQueue = new Vector<String>();
//	private Vector<ClientInfo> mClients = new Vector<ClientInfo>();
	public ClientInfo Clients[];
	private String coups[];
	int nbLegion;
	int nbTeam;
	Game game;
	public boolean laurel = false;
	int whoPlayLaurel;
	int[][] Equipe2={{0,2,4},{1,3,5}};
	int[][] Equipe3={{0,1},{2,3},{4,5}};
	int[][] Equipe6={{0,1,2,3,4,5}};
	
	public Broadcast(int nbLeg, int nbTeam)
	{
		nbLegion = nbLeg;
		this.nbTeam=nbTeam;
		coups = new String[nbLeg*2];
		Clients = new ClientInfo[nbLeg];
	}
	
	public void initGame(Game g)
	{
		game = g;
	}

	public synchronized void addClient(ClientInfo aClientInfo)
	{
		int i;
		for(i=0; i<Clients.length; i++)
		{
			if(Clients[i]==null)
			{
				Clients[i]=aClientInfo;
				envoyerPseudo();
				break;
			}
		}
		if(i==Clients.length-1)
		{
			String logins[] = new String[Clients.length];
			for(int j=0; j<logins.length; j++)
			{
				logins[j]=Clients[j].login;
			}
			game.setPlayer(logins);
		}
	}

	private synchronized void envoyerPseudo()
	{
		String message="";
		for (int i=0; i<Clients.length; i++)
		{
			String separation = i==Clients.length-1 ? "" : ";";
			if(Clients[i]!=null)
				message=message+Clients[i].login+separation;
			else
				message=message+" "+separation;
		}
		for (int i=0; i<Clients.length;i++)
		{
			if(Clients[i]!=null)
				Clients[i].emission.sendCoups(message);
		}
	}

	public synchronized void deleteClient(ClientInfo aClientInfo)
	{
		for(int i=0; i<Clients.length; i++)
		{
			if (Clients[i]==aClientInfo)
			{
				Clients[i]=null;
			}
		}
	}
	
	public synchronized void dispatchMessage(ClientInfo aClientInfo, String aMessage)
	{
		aMessage = "<"+aClientInfo.login+"> " + aMessage;
		mMessageQueue.add(aMessage);
		notify();
	}

	public synchronized void coupJouee(ClientInfo aClientInfo, String coup)
	{
		String slash = "/";
		Pattern pattern1 = Pattern.compile(slash);
		String msgTab[] = pattern1.split(coup);
		coups[2*Integer.parseInt(aClientInfo.numJoueur)]=msgTab[0];
		coups[2*Integer.parseInt(aClientInfo.numJoueur)+1]=msgTab[1];
		for (int i=0; i<Clients.length; i++)
		{
			ClientInfo clientInfo = Clients[i];
			if(clientInfo!=null)
			{
				clientInfo.emission.sendCoups(aClientInfo.numJoueur);
			}
		}
		
		boolean complet = true;
		for(int i=0; i< coups.length; i=i+2)
		{
			if (coups[i]==null)
			{
				complet=false;
			}
		}
		if (complet==true)
		{
			game.cycleGame(coups);
			for(int j=0; j<Clients.length; j++)
			{
				if(!Clients[j].perdu)
				{
					coups[2*j]=null;
					coups[2*j+1]=null;
				}
			}
		}
	}
	
	public synchronized void laurelJouee(String coup)
	{
		int laurelPos=Integer.parseInt(coup);
		laurel=false;
		envoyerLaurel(coup);
		game.cycleLaurel(laurelPos, whoPlayLaurel);
	}
	

	public synchronized void envoyerCoups(int nbLaurel, int wPL)
	{
		if (nbLaurel == 1)
		{
			laurel=true;
			whoPlayLaurel = wPL;
		}
		String message = coups[0]+"";
		for (int i = 1; i < coups.length; i++)
		{
			message+="/"+coups[i];
		}
		for (int i=0; i<Clients.length; i++)
		{
			if(Clients[i]!=null)
				Clients[i].emission.sendCoups(message);
		}
	}
	
	public synchronized void envoyerLaurel(String laurelPos)
	{
		for (int i=0; i<Clients.length; i++)
		{
			if(Clients[i]!=null)
				Clients[i].emission.sendCoups(laurelPos);
		}
	}
	
	public synchronized void envoyerVictoireLaurel(int nbGagnant, int whoPlayLaurel)
	{
		String s = nbGagnant==-1 ? nbGagnant+"" : Clients[nbGagnant].login;
		for (int i=0; i<Clients.length; i++)
		{
			if(Clients[i]!=null)
				Clients[i].emission.sendCoups(s+"/"+Clients[whoPlayLaurel].login);
		}
	}

	public synchronized void traitementDefaite(boolean[] aPerdu)
	{
		String res="";
		for(int i=0; i<Clients.length; i++)
		{
			if(aPerdu[i])
			{
				if(Clients[i].perdu)
				{
					res+=1;
				}
				else
				{
					res+=2;
					Clients[i].perdu=true;
					coups[2*i]="-2";
					coups[2*i+1]="-2";
				}
			}
			else
			{
				res+=0;
			}
		}
		for (int i=0; i<Clients.length; i++)
		{
			if(Clients[i]!=null)
				Clients[i].emission.sendCoups(res);
		}
		traitementFin();
	}
	
	private synchronized void traitementFin()
	{
		int restantEquipe[]=new int[nbTeam];
		for(int i=0; i<restantEquipe.length; i++)
		{
			restantEquipe[i]=nbLegion/nbTeam;
		}
		for(int i=0; i<Clients.length; i++)
		{
			if(Clients[i].perdu)
			{
				restantEquipe[i*nbTeam/nbLegion]--;
			}
		}
		int nbGagnant=0;
		int indiceGagnant=-1;
		for(int i=0; i<restantEquipe.length; i++)
		{
			if(restantEquipe[i]>0)
			{
				nbGagnant++;
				indiceGagnant=i;
			}
		}
		String etat="continue";
		for (int i=0; i<Clients.length; i++)
		{
			if(Clients[i]!=null)
			{
				if(nbGagnant==1)
				{
				
					if(i*nbTeam/nbLegion==indiceGagnant)
					{
							etat="gagne";
					}
					else
					{
							etat="perdu";
					}
				}
				Clients[i].emission.sendCoups(etat);
			}
		}
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
		for (int i=0; i<Clients.length; i++)
		{
			if(Clients[i]!=null)
				Clients[i].emissionMsg.sendMessage(aMessage);
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
}
