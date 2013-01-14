package proelio_serveur;

import java.net.Socket;

public class ClientInfo
{
	//parametre de la socket de lobby
	public Socket mSocketLobby = null;
	public ReceptionLobby receptionLobby = null;
	public EmissionLobby emissionLobby = null;
	
	//parametre de la socket de jeu
	public Socket mSocket = null;
	public Reception reception = null;
	public Emission emission = null;
	
	//parametre de la socket de chat
	public Socket mSocketMsg = null;
	public ReceptionMsg receptionMsg = null;
	public EmissionMsg emissionMsg = null;
	
	//variables du joueur
	public String numJoueur = null;
	public String login = null;
	public boolean perdu = false;
}
