package proelio_client;

import java.io.*;
import java.net.*;

public class ReceptionMsg extends Thread
{
	private BufferedReader mIn;
	private Connexion connexion;

	public ReceptionMsg(Socket aSocket, Connexion aConnexion) throws IOException
	{
		Socket socket = aSocket;
		connexion = aConnexion;
		mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void run()
	{
		try
		{
			while (!isInterrupted())
			{
				String msg = mIn.readLine();
				if (msg == null)
				{
					break;
				}
				connexion.window.write(msg);
			}
		}
		catch (IOException ioex)
		{
			// Problem reading from socket (communication is broken)
		}
	}
}
