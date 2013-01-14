package proelio_serveur;

import java.net.Socket;

public class ConnexionLobby extends Thread
{
	private BroadcastLobby broadcastLobby;
	private Socket socketLobby;
	private Socket socket;
	private Socket socketMsg;
	private ClientInfo clientInfo = new ClientInfo();
	
	public ConnexionLobby(BroadcastLobby bcLobby, Socket sLobby, Socket s, Socket sMsg)
	{
		broadcastLobby = bcLobby;
		socketLobby = sLobby;
		socket = s;
		socketMsg = sMsg;
	}

	public void run()
	{
		try
		{
			clientInfo.mSocket = socket;
			
			clientInfo.mSocketMsg = socketMsg;
			
			clientInfo.mSocketLobby = socketLobby;
			ReceptionLobby receptionLobby = new ReceptionLobby(clientInfo, broadcastLobby);
			EmissionLobby emissionLobby = new EmissionLobby(clientInfo, broadcastLobby);
			clientInfo.receptionLobby = receptionLobby;
			clientInfo.emissionLobby = emissionLobby;
			receptionLobby.start();
			emissionLobby.start();
			broadcastLobby.addClient(clientInfo);
		}
		catch (Exception e)
		{
			try
			{
				broadcastLobby.deleteClient(clientInfo);
			}
			catch(Exception e2)
			{
				System.out.println(e2.getMessage());
			}
		}
		
	}
}
