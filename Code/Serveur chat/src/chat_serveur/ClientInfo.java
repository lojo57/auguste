package chat_serveur;

import java.net.Socket;

public class ClientInfo
{
	public String login = null;
	public Socket mSocket = null;
	public Reception reception = null;
	public Emission emission = null;
}
