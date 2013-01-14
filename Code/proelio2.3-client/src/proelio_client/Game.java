package proelio_client;

import java.io.IOException;
import java.util.regex.Pattern;

public class Game {

	
	//INPUT
	/**
	 * all the data
	 */
	private DataGiven dataGiven;
	/**
	 * the empty plateau and its relation 
	 */
	private Plateau plateau;
	/**
	 * The window for printing
	 */
	private Window window;
	/**
	 * To call algorithm 
	 */
	private Analyse analyse;
	/**
	 * Connexion au serveur
	 */
	public Connexion connexion;
	/**
	 * Numéro de la légion que le joueurs contrôle
	 */
	public String numLegion;

	//VARIABLE
	/**
	 * The description of the whole situation : position of legionaries, laurel, sesterce etc... It will change along the game
	 */
	private SetOfVertex situation;
	/**
	 * Here is written who play where, who had already played...
	 */
	public SetOfLegion setOfLegion;

	public boolean perdu=false;


	/**
	 * All the game takes place in this class
	 * @param dG all the data
	 * @param pla the empty plateau and its relation are given in this parameter
	 * @param wind the window for printing
	 * @param situ all the legionaries, and other object are described by this SetOfVertex Object. 
	 */
	public Game (DataGiven dG, Plateau pla, Window wind,SetOfVertex situ,Connexion connec,String numLeg){

		dataGiven=dG;
		plateau=pla;
		window=wind;
		analyse=new Analyse(pla);
		connexion=connec;
		situation=situ;
		numLegion=numLeg;

		setOfLegion=dataGiven.setOfLegion;
		
		Boolean jeu = true; //variable déterminant si le jeu est en court.

		window.numLegion=Integer.parseInt(numLegion);
		int nbPerdu=0;
		while(jeu)
		{
			if(perdu)
			{
				window.write("\n\n\nVous n'avez plus de pions. Vous pouvez regarder la fin de la partie");
			}
			else
			{
				window.write("\n\n\nEntrez une action (/depart arrivée) ou utilisez la souris.");
			}
			window.centralPanel.setOfVertex=situation;
			window.container.repaint();
			//window.drawCentral(situation);
			// following function will ask to all the players the action they want : to move a legionary or to move the Laurel. 
			// Cette fonction s'arrête dès que tous les joueurs on joué leurs coups.
			if(perdu)
			{
				window.freezeClic=true;
			}
			else
			{
				AskAction askAction = new AskAction();
				askAction.start();
			}

			// Number of player who want to play laurels. 
			int nbLaurel=0;
			// The number of the last legion who play laurels. 
			int whoPlayLaurel=-1;
			
			//Reception des indicateur permettant de savoir qui a joué.
			int nbI = 0; // nommbre d'indicateur reçu
			int joueur;
			//String login;
			while(nbI<dataGiven.nbLegion-nbPerdu)
			{
				try
				{
					joueur = Integer.parseInt(connexion.in.readLine());
					setOfLegion.element(joueur).alreadyPlayed=true;
					if (joueur==Integer.parseInt(numLegion))
					{
						window.write("Vous avez joué.");
					}
					else
					{
						window.write(dataGiven.setOfLegion.element(joueur).player+" a joué.");
					}
					nbI++;
					window.eastPanel.repaint();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.exit(-1);
				}
			}

			//Reception des coups des autres joueurs
			String reception = "";
			String slash="/";
			Pattern pattern=Pattern.compile(slash);
			try
			{
				reception=connexion.in.readLine();//Reception des coups des autres joueurs via la socket
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			//Les coups sont recus via un String comme suit : depRankJ1/arrRankJ1/depRankJ2/arrRankJ2...
			//Ici, on sépare les coups des différents joueurs et on les stocks dans un tableau msgTab.
			String msgTab[]=pattern.split(reception);
			
			//On parcours le tableau et on affecte les cases de départ et d'arrivée au légions correspondantes
			for(int i=0;i<dataGiven.nbLegion;i++)
			{
				if(msgTab[2*i].equals("-2") && msgTab[2*i+1].equals("-2"))
				{
					setOfLegion.element(i).typeAction="perdu";
				}
				else if(!msgTab[2*i].equals("-1") && !msgTab[2*i+1].equals("-1"))
				{
					int rDep=Integer.parseInt(msgTab[2*i]);
					int rArr=Integer.parseInt(msgTab[2*i+1]);
					if(situation.element(rDep).objectCentral.equals("laurel"))
					{
						setOfLegion.element(i).typeAction="moveLaurel";
						window.write(dataGiven.setOfLegion.element(i).player+" veut jouer le laurier.");
					}
					else if( rDep!=-1 && rArr!=-1)
					{
						setOfLegion.element(i).typeAction="moveLegionary";
						window.write(dataGiven.setOfLegion.element(i).player+" a bougé un legionnaire de "+plateau.rank2Word(rDep)+" à "+plateau.rank2Word(rArr)+".");
					}
					setOfLegion.element(i).departure=rDep;
					setOfLegion.element(i).arrival=rArr;
				}
			}
			// PREMIERE PARTIE DU DEPLACEMENT:
			// On ajoute les flèches de déplacement, mais sans les montrer.
			for (int i=0;i<dataGiven.nbLegion;i++)
			{				
				if (setOfLegion.element(i).typeAction.equals("moveLegionary"))
				{
					int rDep=setOfLegion.element(i).departure;
					int rArr=setOfLegion.element(i).arrival;
					situation.element(rDep).travelTo=rArr;
				}
				// check the number of laurel's action
				else if(setOfLegion.element(i).typeAction.equals("moveLaurel"))
				{
					nbLaurel++;
					whoPlayLaurel=i;
				}
			}

			// On affiche maintenant les flèches de déplacement
			window.centralPanel.setOfVertex=situation;
			window.container.repaint();
//			window.drawCentral(situation);
			
			Tool.attend();
			// on attend car le graphisme prend du temps, il faut faire une pause apr?s chaque affichage.  


			// On efface les flèches
			situation.cleanTravel();
			window.centralPanel.setOfVertex=situation;
			window.container.repaint();
//			window.drawCentral(situation);
			Tool.attend();


			// SECOND PART OF DISPLACEMENT: 
			// change positions of legionaries
			// make collisions
			// add armor

			for (int i=0;i<dataGiven.nbLegion;i++){	
				Legion leg=setOfLegion.element(i);



				if (leg.typeAction.equals("moveLegionary")){

					int rDep=leg.departure;
					int rArr=leg.arrival;


					// COLLISION CASE
					if (!situation.element(rArr).free || situation.element(rArr).collision){


						situation.element(rArr).collision=true;
						situation.element(rArr).objectCentral="bang";
						situation.element(rArr).free=true;
						situation.element(rArr).legion=DataGiven.emptyLegion;

						situation.element(rArr).objectNorth="none";

						situation.element(rDep).legion=DataGiven.emptyLegion;		
						situation.element(rDep).free=true;

						situation.nbCollision++;



					}
					// NO COLLISION
					else{

						situation.element(rArr).legion=leg;		
						situation.element(rArr).free=false;

						situation.element(rDep).legion=DataGiven.emptyLegion;
						situation.element(rDep).free=true;

						//A legionary keep its armor when he moves
						if (situation.element(rDep).armor) {
							situation.element(rArr).armor=true;
							situation.element(rDep).armor=false;														
						}
						// A legionary win an armor when he find a sesterce
						if (situation.element(rArr).objectNorth.equals("sesterce")) {
							situation.element(rArr).armor=true;
							situation.element(rArr).objectNorth="none";														
						}

					}
				}
			}// end: second part of displacement




			if (situation.nbCollision>0)
			{
				window.centralPanel.setOfVertex=situation;
				window.container.repaint();
//				window.drawCentral(situation);

				//ONE STOP HERE:
				Tool.attend();
				situation.cleanCollision();
			}

			window.centralPanel.setOfVertex=situation;
			window.container.repaint();
//			window.drawCentral(situation);
			Tool.attend();


			//TENAILLE
			analyse.tenaille(situation);			
			if (situation.nbTenaille>0){
				window.centralPanel.setOfVertex=situation;
				window.container.repaint();
//				window.drawCentral(situation);

				//ONE STOP HERE:
				Tool.attend();

				situation.cleanKilled();
				situation.cleanTenaille();
			}




			//FIGHT			
			analyse.fight(situation);
			if (situation.nbFight>0){
				window.centralPanel.setOfVertex=situation;
				window.container.repaint();
				//window.drawCentral(situation);

				//ONE STOP HERE:
				Tool.attend();

				situation.cleanKilled();
				situation.cleanFight();
			}

			try
			{
				String etatDefaiteJoueurs=connexion.in.readLine();
				nbPerdu=0;
				for(int i=0; i<etatDefaiteJoueurs.length();i++)
				{
					if(etatDefaiteJoueurs.charAt(i)=='2')
					{
						window.write(dataGiven.setOfLegion.element(i).player+" a perdu !");
						nbPerdu++;
					}
					else if(etatDefaiteJoueurs.charAt(i)=='1')
					{
						nbPerdu++;
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				String etat=connexion.in.readLine();
				if(!etat.equals("continue"))
				{
					if(etat.equals("perdu"))
					{
						window.write("Fin du jeu ! Vous n'avez pas pu mener vos légionnaires à la victoire. (Vous pouvez retourner au lobby.)");
						jeu=false;
					}
					else if(etat.equals("gagne"))
					{
						window.write("Vous avez gagné !! Felicitations, vous êtes un grand stratège ! (Vous pouvez retourner au lobby.)");
						jeu=false;
					}
					window.container.repaint();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
			
			if(jeu)
			{
				// DISPLACEMENTS OF LEGIONAIRES ARE FINISHED.
				// Now we move Laurels if necessary
				// LAUREL 
				if (nbLaurel>1)
				{
					window.write("Trop de personnes veulent jouer le laurier.");
					window.secondClic=false;
				}
				else if (nbLaurel==1)
				{
					window.centralPanel.setOfVertex=situation;
					window.container.repaint();
					//window.drawCentral(situation);	
	
					int laurelPosition=setOfLegion.element(whoPlayLaurel).departure;
	
					//ONE STOP HERE:
					if(whoPlayLaurel!=Integer.parseInt(numLegion)) 
					{
						window.write(dataGiven.setOfLegion.element(whoPlayLaurel).player+" joue le laurier, patientez.");
					}
					int arrivalOfLaurel=askLaurel(laurelPosition,whoPlayLaurel);
	
					situation.element(laurelPosition).objectCentral="none";
					situation.element(laurelPosition).free=true;
	
					situation.element(arrivalOfLaurel).objectCentral="laurel";
					situation.element(arrivalOfLaurel).free=false;
					
					dataGiven.laurelPosition=arrivalOfLaurel;
					
					window.centralPanel.setOfVertex=situation;
					window.container.repaint();
					
					try 
					{
						String msgFin=connexion.in.readLine();
						String fin[] = pattern.split(msgFin);
						if (!fin[0].equals("-1"))
						{
							jeu=false;
							if(fin[0].equals(fin[1]))
							{
								window.write(fin[1]+" a terminé la partie en ramenant le laurier à son campement.");
							}
							else
							{
								window.write(fin[1]+" a terminé la partie en ramenant le laurier dans le campement de "+fin[0]+".");
							}
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				setOfLegion.cleanAction();	//we clean "setOfLegion" of all the actions : movement of Legionaries and Laurel.
			
				window.freezeClic=false;
			}
		}// end of while(true)
	}






	/**
	 * This long function will ask to players what they want to do : displacement of a legionary (and where) of displacement of the laurel (and where). 
	 * These information are stocked in setOfLegion (because there is one action by legion).  
	 * @param setOfLegion
	 */
	public class AskAction extends Thread
	{
		public void run()
		{
			/**
			 * the rank of a legionary which will move 
			 * OR
			 * the rank of a legionary which  will play the laurel 
			 */
			int depRank;
			/**
			 * the rank of the vertex where the legionary will go. 
			 */
			int arrRank;
	
			
			/**
			 * This infinite loop will end when players enter the word "end". 
			 */
			boolean ok=true;
			while (ok){
				
				
				
	
				int k;
				// in the departure word, we wait a 3 letters word, which designate a vertex. 
				String departureWord=new String();
				// in the arrical word we wait a 3 letters word for a vertex, of we wait the word "laurel". 
				String arrivalWord=new String();
	
				String sentence = window.sentence;
				// we convert the input sentence to Upper Case for simplicity. 
	
				while(sentence.equals(""))
				{
						sentence = window.sentence.toUpperCase();
				}
				
				
				
				if (sentence.length()>6){
					departureWord=sentence.substring(0,3);
					arrivalWord=sentence.substring(4,sentence.length());
				}
	
				departureWord.toUpperCase();
				arrivalWord.toUpperCase();
				
				depRank=plateau.word2Rank(departureWord);
				arrRank=plateau.word2Rank(arrivalWord);
				//long list of possibilities to analyze the input sentence. 
				
				//ITEM : end of the infinite loop
				if (sentence.equals("SKIP"))
				{
					connexion.out.println("-1/-1");
					connexion.out.flush();
				}
	
	
				//ITEM : the sentence is made with two 3-letters words, which correspond to vertices. 			
				else if (depRank!= -1 && arrRank!=-1){
	
					Vertex depVertex=situation.element(depRank);
					Vertex arrVertex=situation.element(arrRank);
	
					if (depVertex.armor && arrVertex.objectNorth=="sesterce"){
	
						window.write("Un legionnaire en armure ne peut pas aller sur une case avec un sesterce.");
					}
					else if (!arrVertex.free){
						window.write("La case d'arrivée n'est pas libre." );					 
					}
					else if (depVertex.legion.name=="none"){
						window.write("La case de départ est vide." );					 
					}
					else if (!Tool.contain(analyse.edgeGroup(depRank, situation),arrRank)) {
						window.write("Vous ne pouvez pas aller si loin." );
					}
					else{
						String name=depVertex.legion.name;
						k=setOfLegion.findLegionOfName(name);
						String kk=k+"";
						if(kk.equals(numLegion))
						{
							connexion.out.println(depRank+"/"+arrRank);
							connexion.out.flush();
							
							Legion leg=setOfLegion.element(k);
							leg.departure=depRank;
							leg.arrival=arrRank;
							leg.alreadyPlayed=true;
							leg.typeAction="moveLegionary";
	
							setOfLegion.setElement(k, leg);
							//window.drawEast(setOfLegion);
							ok=false;
							window.freezeClic=true;
							
						}
						else
						{
							window.write("Ce n'est pas votre légion !");
						}
					}
	
	
				}
	
				//ITEM : a sentence to move laurels.
				else if (sentence.toUpperCase().equals("LAUREL"))
				{
					int [] voisin=plateau.neighbor(dataGiven.laurelPosition);
					int laurelPosition=-1;
					String color="";
					color=dataGiven.setOfLegion.element(Integer.parseInt(numLegion)).name;
					for (int elem:voisin)
					{
						if (situation.element(elem).legion.name.equals(color))
						{
							laurelPosition=dataGiven.laurelPosition;
						}
					}
	
	
					if (laurelPosition==-1) window.write("Vous etes trop loin du laurier pour le jouer." );
					else
					{
						Legion leg=setOfLegion.element(Integer.parseInt(numLegion));
						
						leg.departure=laurelPosition;
						leg.arrival=-1;
						leg.typeAction="moveLaurel";	
						leg.alreadyPlayed=true;
						
						connexion.out.println(leg.departure+"/"+leg.arrival);
						connexion.out.flush();
	
						//setOfLegion.setElement(Integer.parseInt(numLegion), leg);
						//window.drawEast(setOfLegion);
						ok=false;
					}
	
				}
	
				//LAST ITEM : a sentence corresponding to nothing
				else{ 
					window.write("Entrée incorrecte !");
				}
				//end of the long list of possibilities
	
				window.sentence="";
				sentence="";
				window.mouseArrivalWord="";
				window.mouseDepartureWord="";
	
			}//end of while
	
		}// End of askDisplacement()
	}








/**
 * This function ask where a player want to play Laurel. 
 * This function is run only if there is only one player who ask to play laurel
 * @param laurelPosition  the current rank of the Laurel
 * @return the rank of the vertex where the laurel goes
 */
	public int askLaurel(int laurelPosition, int whoPlayLaurel){
		int arrRank=-1;

		window.sentence="";
		window.freezeClic=false;
		boolean ok=true;
		if(numLegion.equals(""+whoPlayLaurel))
		{
			while (ok)
			{
				window.isPlayingLaurel=true;
				String sentence="";
				window.sentence="";
				window.write("Où voulez vous jouer le laurier ?");
				window.write("Entrez /NOWHERE si vous ne voulez/pouvez pas le jouer.");
				window.drawLaurelPossibilities();
				String arrivalWord=new String();
				
				while(sentence.equals(""))
				{
						sentence = window.sentence.toUpperCase();
				}
				
				if (sentence.length()==3){
					arrivalWord=sentence;
				}
				arrRank=plateau.word2Rank(arrivalWord);
				// list of possibilities
				//ITEM : the player change his mind, and do not want to play the laurel. 
				if (sentence.equals("NOWHERE")){
					window.isPlayingLaurel=false;
					window.secondClic=false;
					sentence="";
					window.sentence="";
					ok=false;
					arrRank=laurelPosition;
				}
	
	
				//ITEM	: the player writes a 3 letter word corresponding to a vertex		
				else if ( arrRank!=-1){
	
					//Vertex depVertex=situation.element(depRank);
					Vertex arrVertex=situation.element(arrRank);
	
					if ( arrVertex.objectNorth=="sesterce"){
	
						window.write("Le laurier ne peut pas aller sur une case où il y a un sesterce.");
					}
					else if (!arrVertex.free){
						window.write("La case d'arrivée n'est pas libre." );					 
					}
	
					else if (!Tool.contain(plateau.neighbor(laurelPosition),arrRank)) {
						window.write("Le laurier ne peut pas aller aussi loin." );
					}
					else{		
						window.isPlayingLaurel=false;
						window.secondClic=false;
						sentence="";
						window.sentence="";
						ok=false;					
					}
	
	
				}
	
				//LAST ITEM : the sentence is no valid
				else
				{ 
					window.write("Entrée incorrecte !");
				}
				//end of the long list of possibilities
	
			}//end of while
			connexion.out.println(arrRank+"");
			connexion.out.flush();
		}//end of if
		else
		{
			window.freezeClic=true;
		}
		try
		{
			arrRank=Integer.parseInt(connexion.in.readLine());
		}
		catch (NumberFormatException e)
		{
			// non
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// non
			e.printStackTrace();
		}
		window.freezeClic=true;
		window.eraseLaurelPossibilities();
		return(arrRank);
		
	}// End of askLaurel()
}
