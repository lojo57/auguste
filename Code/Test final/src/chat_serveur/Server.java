package chat_serveur;

import java.net.*;
import java.io.*;

public class Server
{
	public static final int LISTENING_PORT = 2002;

	public static void main(String[] args)
	{
		// Open server socket for listening
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(LISTENING_PORT);
			System.out.println("Server started on port " + LISTENING_PORT);
		} catch (IOException se) {
			System.err.println("Can not start listening on port " + LISTENING_PORT);
			se.printStackTrace();
			System.exit(-1);
		}

		// Start broadcast thread
		Broadcast broadcast = new Broadcast();
		broadcast.start();

		// Accept and handle client connections
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				/*ClientInfo clientInfo = new ClientInfo();
				BufferedReader mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				clientInfo.login=mIn.readLine();
				clientInfo.mSocket = socket;
				Reception reception1 =
					new Reception(clientInfo, broadcast);
				Emission emission1 =
					new Emission(clientInfo, broadcast);
				clientInfo.reception = reception1;
				clientInfo.emission = emission1;
				reception1.start();
				emission1.start();
				broadcast.addClient(clientInfo);*/
				Connexion connexion = new Connexion(broadcast, socket);
				connexion.start();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
