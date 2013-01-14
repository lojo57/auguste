package game;

import proelio_serveur.Broadcast;


public class MainGame extends Thread
{
	public DataGiven dataGiven;
	Plateau plateau;
	SetOfVertex initialSituation;
	Broadcast broadcast;
	
	public MainGame(Broadcast bc, int nbLeg, int baseHexa) 
	{
		
		dataGiven=new DataGiven(bc);
		broadcast = bc;
		
		// Indication des différents paramètres du jeu
		dataGiven.nbLegion=nbLeg;
		dataGiven.baseHexa=baseHexa;
		
		dataGiven.emptyPlateau=false;
		dataGiven.eastPanelVisible=true;
		dataGiven.southPanelVisible=true;
	}
	
	public void run()
	{
		dataGiven.computeData( dataGiven.baseHexa,dataGiven.nbLegion, dataGiven.nbTeam);

		plateau = new Plateau(dataGiven);				
		initialSituation=plateau.initialSituation();

		@SuppressWarnings("unused")
		Game game=new Game(dataGiven,plateau,initialSituation, broadcast);
	}
}
