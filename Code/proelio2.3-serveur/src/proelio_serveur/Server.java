package proelio_serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	//Ports pour les sockets
	public static final int LISTENING_PORTJEU = 2002;
	public static final int LISTENING_PORTMSG = 2003;
	public static final int LISTENING_PORTLOBBY = 2004;

	public static void main(String[] args)
	{
		//Sockets
		ServerSocket serverSocket = null;
		ServerSocket serverSocketMsg = null;
		ServerSocket serverSocketLobby = null;

		try
		{
			serverSocket = new ServerSocket(LISTENING_PORTJEU);
			System.out.println("Ecoute du serveur sur le port de jeu " + LISTENING_PORTJEU);
		}
		catch (IOException se)
		{
			System.err.println("Echec de l'ecoute du port de jeu " + LISTENING_PORTJEU);
			se.printStackTrace();
			System.exit(-1);
		}
		try
		{
			serverSocketMsg = new ServerSocket(LISTENING_PORTMSG);
			System.out.println("Ecoute du serveur sur le port de chat " + LISTENING_PORTMSG);
		}
		catch (IOException se)
		{
			System.err.println("Echec de l'ecoute du port de chat " + LISTENING_PORTMSG);
			se.printStackTrace();
			System.exit(-1);
		}
		try
		{
			serverSocketLobby = new ServerSocket(LISTENING_PORTLOBBY);
			System.out.println("Ecoute du serveur sur le port du lobby " + LISTENING_PORTLOBBY);
		}
		catch (IOException se)
		{
			System.err.println("Echec de l'ecoute du port du lobby " + LISTENING_PORTLOBBY);
			se.printStackTrace();
			System.exit(-1);
		}
		
		BroadcastLobby bcLobby=new BroadcastLobby();
		bcLobby.start();
		while(true)
		{
			try
			{
				Socket socket = serverSocket.accept();
				Socket socketMsg = serverSocketMsg.accept();
				Socket socketLobby = serverSocketLobby.accept();
				ConnexionLobby connexionLobby = new ConnexionLobby(bcLobby, socketLobby, socket, socketMsg);
				connexionLobby.start();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
