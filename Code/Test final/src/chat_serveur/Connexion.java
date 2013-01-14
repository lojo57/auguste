package chat_serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Connexion extends Thread
{
	private Broadcast broadcast;
	private Socket socket;
	private ClientInfo clientInfo = new ClientInfo();
	private String login;
	
	public Connexion (Broadcast aBroadcast, Socket aSocket)
	throws IOException
	{
		broadcast = aBroadcast;
		socket = aSocket;
	}
	
	public void run()
	{
		try
		{
			BufferedReader mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			login=mIn.readLine();
			boolean exist=broadcast.existLogin(login);
			if(exist==false)
			{
				mOut.println("ok");
				mOut.flush();
				clientInfo.login=login;
				clientInfo.mSocket = socket;
				Reception reception1 = new Reception(clientInfo, broadcast);
				Emission emission1 = new Emission(clientInfo, broadcast);
				clientInfo.reception = reception1;
				clientInfo.emission = emission1;
				reception1.start();
				emission1.start();
				broadcast.addClient(clientInfo);
			}
			else
			{
				mOut.println("!ok");
				mOut.flush();
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
