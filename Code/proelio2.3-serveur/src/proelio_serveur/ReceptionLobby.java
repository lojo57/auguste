package proelio_serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceptionLobby extends Thread
{
	private BroadcastLobby broadcastLobby;
	private ClientInfo mClientInfo;
	private BufferedReader mIn;

	public ReceptionLobby(ClientInfo aClientInfo, BroadcastLobby bcLobby) throws IOException
	{
		mClientInfo = aClientInfo;
		broadcastLobby = bcLobby;
		Socket socket = aClientInfo.mSocketLobby;
		mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void run()
	{
		try
		{
			while (!isInterrupted())
			{
				String message = mIn.readLine();
				if (message == null)
				{
					break;
				}
				else if(mClientInfo.login==null)
				{
					broadcastLobby.testLogin(mClientInfo, message);
				}
				else if(message.equals("###BESOINLISTE###"))
				{
					broadcastLobby.besoinListe(mClientInfo);
				}
				else if(message.equals("###CREERPARTIE###"))
				{
					String options = mIn.readLine();
					broadcastLobby.testPartie(mClientInfo, options);
				}
				else if(message.equals("###REJOINDREPARTIE###"))
				{
					String nomPartie = mIn.readLine();
					broadcastLobby.rejoindrePartie(mClientInfo, nomPartie);
				}
			}
		}
		catch (IOException ioex)
		{
			// Problem reading from socket (communication is broken)
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.emissionLobby.interrupt();
		broadcastLobby.deleteClient(mClientInfo);
	}
}
