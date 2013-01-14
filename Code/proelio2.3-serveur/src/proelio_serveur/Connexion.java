package proelio_serveur;

import java.io.IOException;

public class Connexion extends Thread
{
	private Broadcast broadcast;
	private ClientInfo clientInfo;
	private int num;
	
	public Connexion (Broadcast aBroadcast, ClientInfo mClientInfo, int i) throws IOException
	{
		broadcast = aBroadcast;
		num = i;
		clientInfo=mClientInfo;
	}
	
	public void run()
	{
		try
		{
			Reception reception = new Reception(clientInfo, broadcast);
			Emission emission = new Emission(clientInfo, broadcast);
			ReceptionMsg receptionMsg = new ReceptionMsg(clientInfo, broadcast);
			EmissionMsg emissionMsg = new EmissionMsg(clientInfo, broadcast);
			clientInfo.reception = reception;
			clientInfo.emission = emission;
			clientInfo.receptionMsg = receptionMsg;
			clientInfo.emissionMsg = emissionMsg;
			clientInfo.numJoueur = num+"";
			clientInfo.perdu=false;
			reception.start();
			emission.start();
			receptionMsg.start();
			emissionMsg.start();
			broadcast.addClient(clientInfo);
		}
		catch (Exception e)
		{
			try
			{
				broadcast.deleteClient(clientInfo);
			}
			catch(Exception e2)
			{
				System.out.println(e2.getMessage());
			}
		}
		
	}
}
