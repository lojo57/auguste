package game;

import proelio_serveur.Broadcast;

public class Game {


	//INTPUT
	
	private Broadcast broadcast;
	/**
	 * all the data
	 */
	private DataGiven dataGiven;
	/**
	 * the empty plateau and its relation 
	 */
	public Plateau plateau;
	/**
	 * To call algorithm 
	 */
	private Analyse analyse;

	private boolean[] aPerdu;

	//VARIABLE
	/**
	 * The description of the whole situation : position of legionaries, laurel, sesterce etc... It will change along the game
	 */
	private SetOfVertex situation;
	/**
	 * Here is written who play where, who had already played...
	 */
	private SetOfLegion setOfLegion;



	/**
	 * All the game takes place in this class
	 * @param dG all the data
	 * @param pla the empty plateau and its relation are given in this parameter
	 * @param situ all the legionaries, and other object are described by this SetOfVertex Object. 
	 */
	public Game (DataGiven dG, Plateau pla, SetOfVertex situ, Broadcast bc)
	{
		broadcast = bc;
		bc.initGame(this);
		
		dataGiven=dG;
		plateau=pla;
		analyse=new Analyse(pla);

		situation=situ;
		
		aPerdu=new boolean[dataGiven.nbLegion];

		setOfLegion=dataGiven.setOfLegion;
	}

		// some general function are written in the class U
		// ex : u.attend() will write "taper entrée"  and wait for "entrée". 
		

	public void cycleGame(String coups[])
	{			
			// Number of player who want to play laurels. 
			int nbLaurel=0;
			// The number of the last legion who play laurels. 
			int whoPlayLaurel=-1;

			for(int i = 0; i < dataGiven.nbLegion; i++)
			{
				if(Integer.parseInt(coups[2*i])!=-1 && Integer.parseInt(coups[2*i])!=-2)
				{
					Legion leg=setOfLegion.element(i);
					leg.departure=Integer.parseInt(coups[2*i]);
					leg.arrival=Integer.parseInt(coups[2*i+1]);
					leg.alreadyPlayed=true;
					setOfLegion.setElement(i, leg);
					
					if(situation.element(leg.departure).objectCentral.equals("laurel") && coups[2*i+1].equals("-1"))
					{
						leg.typeAction="moveLaurel";
					}
					else
					{
						leg.typeAction = "moveLegionary";
					}
				}
			}



			// FIRST PART OF DISPLACEMENT: 
			//add arrows for displacement of Legionaries
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

			broadcast.envoyerCoups(nbLaurel, whoPlayLaurel);

			// printing theses arrows
			Tool.attend();
			// on attend car le graphisme prend du temps, il faut faire une pause apr?s chaque affichage.  


			// we clean the arrows
			situation.cleanTravel();




			// SECOND PART OF DISPLACEMENT: 
			//change positions of legionaries
			// make collisions
			// add armor

			for (int i=0;i<dataGiven.nbLegion;i++)
			{	
				Legion leg=setOfLegion.element(i);


				if (leg.typeAction.equals("moveLegionary"))
				{
					int rDep=leg.departure;
					int rArr=leg.arrival;


					// COLISION CASE
					if (!situation.element(rArr).free || situation.element(rArr).collision)
					{
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
					else
					{
						situation.element(rArr).legion=leg;		
						situation.element(rArr).free=false;

						situation.element(rDep).legion=DataGiven.emptyLegion;
						situation.element(rDep).free=true;

						//A legionary keep its armor when he moves
						if (situation.element(rDep).armor)
						{
							situation.element(rArr).armor=true;
							situation.element(rDep).armor=false;														
						}
						// A legionary win an armor when he find a sesterce
						if (situation.element(rArr).objectNorth.equals("sesterce"))
						{
							situation.element(rArr).armor=true;
							situation.element(rArr).objectNorth="none";														
						}

					}
				}
			}// end: second part of displacement




			if (situation.nbCollision>0)
			{
				//ONE STOP HERE:
				situation.cleanCollision();
			}

			// DISPLACEMENTS OF LEGIONAIRES ARE FINISHED.
			// Now we move Laurels if necessary

			//TENAILLE
			analyse.tenaille(situation);			
			if (situation.nbTenaille>0)
			{
				//ONE STOP HERE:
				situation.cleanKilled();
				situation.cleanTenaille();
			}




			//FIGHT			
			analyse.fight(situation);
			if (situation.nbFight>0)
			{
				//ONE STOP HERE:
				situation.cleanKilled();
				situation.cleanFight();
			}




			//			we clean "setOfLegion" of all the actions : movement of Legionaries and Laurel. 

			if(nbLaurel!=1)
			{
				setOfLegion.cleanAction();
			}
			
			dataGiven.nbPions=situation.getNumberOfPawn(DataGiven.emptyLegion, dataGiven.nbLegion);
			for (int i=0; i<dataGiven.nbPions.length; i++)
			{
				aPerdu[i]=(dataGiven.nbPions[i]==0);
			}
			broadcast.traitementDefaite(aPerdu);
	}
	
	public void cycleLaurel(int laurelPos, int whoPlayLaurel)
	{
			int laurelPosition=setOfLegion.element(whoPlayLaurel).departure;
			int arrivalOfLaurel=laurelPos;

			situation.element(laurelPosition).objectCentral="none";
			situation.element(laurelPosition).free=true;

			situation.element(arrivalOfLaurel).objectCentral="laurel";
			situation.element(arrivalOfLaurel).free=false;
			
			dataGiven.laurelPosition=arrivalOfLaurel;
			
			setOfLegion.cleanAction();
			int victoire=victoireLaurel();
			broadcast.envoyerVictoireLaurel(victoire, whoPlayLaurel);	
	}
	
	public int victoireLaurel()
	{
		for(int i=0; i<dataGiven.nbLegion; i++)
		{
			int[] tCP=dataGiven.tentChiefPosition[i];
			for(int j=0; j<tCP.length; j++)
			{
				if(dataGiven.laurelPosition==tCP[j])
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public void setPlayer(String[] logins)
	{
		for(int i=0; i<logins.length; i++)
		{
			dataGiven.setOfLegion.element(i).player=logins[i];
			dataGiven.setOfLegion.element(i).numLegion=i;
		}
	}
}
