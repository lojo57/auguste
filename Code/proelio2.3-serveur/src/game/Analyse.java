package game;

import java.util.ArrayList;

/**
 * This class contains the more algorithmic method.
 * Starting From SetOfVertex Situation, it compute fight, tenaille, and  edges of Connex component (= a continuous group of legionnary of the same color).
 * 
 * @author vigon
 *
 */
public class Analyse {

	
// INPUT
	
	private Plateau plateau;
	
	
//OUTPUT	
	public int [] deathByFight;
	public int [] deathByTenaille;

	

	public Analyse(Plateau PAR){
		plateau=PAR;		
	}



	

	/**
	 * The method modify extract, by marking vertices where legionaries are killed by fight, and by marking vertices which win the fight. 
	 * @param extract  This will be a line of Vertices to analyze	 
	 * @return the number of fight in the input line
	 */
	
	private int fightOneLine(SetOfVertex extract)
	{
		
		int nbFight=0;
		int i,j;
		int forceA,forceB;
		String teamA,teamB;
		
		for (i=0;i<extract.size();i++)
		{
			teamA=extract.element(i).legion.team;

			if (i+1<extract.size() && !teamA.equals("none") && !extract.element(i+1).legion.team.equals("none"))
			{
				forceA=0;
				forceB=0;

				if(i+1<extract.size() && ! extract.element(i+1).legion.team.equals(teamA))
				{
					 teamB = extract.element(i+1).legion.team;
					// to compute the force of team A
					j=i-1;
					while (j>=0 &&  extract.element(j).legion.team.equals(teamA) ) j--;
					forceA=i-j; 
					if (extract.element(i).armor){forceA++;}

					//to compute the force of team B
					j=i+2;
					while (j<extract.size() && extract.element(j).legion.team.equals(teamB) ) j++;
					forceB= j-i-1;
					if (extract.element(i+1).armor){forceB++;}
				}

				if (forceA>forceB && i+1<extract.size()){
					extract.element(i+1).killed=true; 
					extract.element(i).winFightAgainst.add(extract.element(i+1).rank);
					nbFight++;
					
				}
				else if (forceA<forceB){
					extract.element(i).killed=true;
					extract.element(i+1).winFightAgainst.add(extract.element(i).rank);
					nbFight++;
				}

			}
			
			
		}//end for
		
		return(nbFight);

	}//end fightOneLne

			

	/**
	 * This method gather all the fights make along all the line. Finally it modify the field nbFight of the "situation"
	 * @param situation  The "SetOfVertex situation" to analyze
	 */
	public void fight(SetOfVertex situation)	{
		
		
					
		for (int a=0;a<plateau.nbSegment();a++)
		{
			SetOfVertex extr=situation.extract(plateau.segment(a));			
			situation.nbFight=situation.nbFight+fightOneLine(extr);						
		}
	}

//	private int  tenailleOneLine(SetOfVertex extract)
//	{
//		int nbTenaille=0;
//		
//		int n=extract.size();
//		int i,j,k;
//		String a,b;
//		
//		for (i=0;i<n;i++)
//		{
//			a=extract.element(i).team;
//			if (a!="none" && i+1<n &&  !extract.element(i+1).free && extract.element(i+1).team!=a)
//			{
//				b=extract.element(i+1).team;  
//				j=i+2;
//				while (j<n && extract.element(j).team ==b) 	j++;
//				
//				if (j<n && !extract.element(j).free && extract.element(j).team!=b)//the last verification is not usefull
//				{					
//					for (k=i+1;k<=j-1;k++)   extract.element(k).killed=true; 
//					extract.element(i).tenailleTo.add(extract.element(j).rank);
//					nbTenaille++;
//				}
//				else if (j<n && extract.element(j).free && j+1<n && !extract.element(j+1).free && extract.element(j+1).team!=b )
//				{
//					for (k=i+1;k<=j-1;k++)  extract.element(k).killed=true;
//					extract.element(i).tenailleTo.add(extract.element(j+1).rank);
//					nbTenaille++;
//				}
//								
//			}
//			else if (a!="none" && i+1<n && extract.element(i+1).free && i+2<n && !extract.element(i+2).free && extract.element(i+2).team!=a)
//			{
//				b=extract.element(i+2).team;
//				j=i+3;
//				while (j<n && extract.element(j).team==b) 	j++;
//				
//				if (j<n && !extract.element(j).free && extract.element(j).team!=b)
//				{
//					for (k=i+2;k<=j-1;k++)  extract.element(k).killed=true;
//					extract.element(i).tenailleTo.add(extract.element(j).rank);
//					nbTenaille++;
//				}
//				else if (j<n && extract.element(j).free && j+1<n && !extract.element(j+1).free && extract.element(j+1).team!=b )
//				{
//					for (k=i+2;k<=j-1;k++)  extract.element(k).killed=true;
//					extract.element(i).tenailleTo.add(extract.element(j+1).rank);
//					nbTenaille++;
//
//				}
//				
//			}
//			
//		}
//		
//		return(nbTenaille);
//	}	
	
	
	
	

	/**
	 * The method modify "extract", by marking vertices where legionaries are killed by tenaille, and by marking vertices which win produce this tenaille. 
	 * @param extract  This will be a line of Vertices to analyze	 
	 * @return the number of tenaille in the input line
	 */
	private int  tenailleOneLine(SetOfVertex extract)
	{
		int nbTenaille=0;
		
		int n=extract.size();
		int i,j,k;
		String a,b;
		
		for (i=0;i<n;i++)
		{
			a=extract.element(i).legion.team;
			if (a!="none" && i+1<n &&  extract.element(i+1).legion.team !="none" && extract.element(i+1).legion.team!=a)
			{
				b=extract.element(i+1).legion.team;  
				j=i+2;
				while (j<n && extract.element(j).legion.team ==b) 	j++;
				
				if (j<n && extract.element(j).legion.team!="none" && extract.element(j).legion.team!=b)//the last verification is not usefull
				{					
					for (k=i+1;k<=j-1;k++)   extract.element(k).killed=true; 
					extract.element(i).tenailleTo.add(extract.element(j).rank);
					nbTenaille++;
				}
				else if (j<n && extract.element(j).legion.team=="none" && j+1<n && extract.element(j+1).legion.team!="none" && extract.element(j+1).legion.team!=b )
				{
					for (k=i+1;k<=j-1;k++)  extract.element(k).killed=true;
					extract.element(i).tenailleTo.add(extract.element(j+1).rank);
					nbTenaille++;
				}
								
			}
			else if (a!="none" && i+1<n && extract.element(i+1).legion.team=="none" && i+2<n && extract.element(i+2).legion.team!="none" && extract.element(i+2).legion.team!=a)
			{
				b=extract.element(i+2).legion.team;
				j=i+3;
				while (j<n && extract.element(j).legion.team==b) 	j++;
				
				if (j<n && extract.element(j).legion.team!="none" && extract.element(j).legion.team!=b)
				{
					for (k=i+2;k<=j-1;k++)  extract.element(k).killed=true;
					extract.element(i).tenailleTo.add(extract.element(j).rank);
					nbTenaille++;
				}
				else if (j<n && extract.element(j).legion.team=="none" && j+1<n && extract.element(j+1).legion.team!="none" && extract.element(j+1).legion.team!=b )
				{
					for (k=i+2;k<=j-1;k++)  extract.element(k).killed=true;
					extract.element(i).tenailleTo.add(extract.element(j+1).rank);
					nbTenaille++;

				}
				
			}
			
		}
		
		return(nbTenaille);
	}
	
	
	
	/**
	 * This method gather all the tenaille make along all the lines. Finally it modify the field nbTenaille of the "situation"
	 * @param situation  The "SetOfVertex situation" to analyze
	 */
	public void tenaille(SetOfVertex situation)
	{		
		for (int a=0;a<plateau.nbSegment();a++)
		{
			SetOfVertex extr=situation.extract(plateau.segment(a));			
			situation.nbTenaille=situation.nbTenaille+ tenailleOneLine(extr);
		}
		
		

	}



	public int[] edgeGroup(int theR,SetOfVertex situation)
	{

		
		int markA,markB,i,a,r;
		ArrayList<Integer> edge=new ArrayList<Integer>();
		ArrayList<Integer> group=new ArrayList<Integer>();
		int s= situation.size();
		String legionName=situation.element(theR).legion.name;
		boolean [] rightLegionName = new boolean[s];
		boolean [] free = new boolean[s];



		for (r=0;r<s;r++)
		{
			rightLegionName[r]=  situation.element(r).legion.name.equals(legionName);
			free[r]=situation.element(r).free;						
		}

		group.add(theR);
		rightLegionName[theR]=false; 

		markA=-1;
		markB=0;

		while (markB>markA)
		{

			for (i=markA+1;i<=markB;i++)
			{

				//ON CHERCHE LES VOISINS DE MæME COULEUR:


				int [] neighbor=plateau.neighbor(group.get(i));
				
				
				ArrayList<Integer> neighborKeeped=new ArrayList<Integer>();

				for (int elem:neighbor){					
					if (rightLegionName[elem]){neighborKeeped.add(elem);}
					rightLegionName[elem]=false;
					if (free[elem]){edge.add(elem);}
					free[elem]=false;
				}
				group.addAll(neighborKeeped);				
			}
			markA=markB;
			markB=group.size()-1;
		}

		int [] outputFinal=new int[edge.size()];
		for (a=0;a<edge.size();a++){outputFinal[a]=edge.get(a);}
		return(outputFinal);
	}


	
	
//	
//	public int[] collision ( ArrayList<Integer> depa , ArrayList<Integer> arri){
//		
//		int i,j;
//		ArrayList <Integer> output=new ArrayList<Integer>();
//		ArrayList <Integer> arrival=new ArrayList<Integer>();
//		ArrayList <Integer> departure=new ArrayList<Integer>();
//		
//		//copy of the two arrayList to no destroy them
//		for (i=0;i<depa.size();i++){
//			departure.add(depa.get(i));
//			arrival.add(arri.get(i));
//			
//		}
//		
//		
//		
//		while (arrival.size()>0){
//			
//			int rA=arrival.get(0);
//			int rD=departure.get(0);
//		    departure.remove(0);
//		    arrival.remove(0);
//
//		    
//			j=arrival.indexOf(rA);
//			if (j!=-1){
//				output.add(rD);
//			}
//			
//			
//			while (j!=-1){
//				output.add(departure.get(j));
//				arrival.remove(j);
//				departure.remove(j);
//				j=arrival.indexOf(rA);
//				
//			}
//		}
//		
//		int [] finalOutput=new int[output.size()];
//		for (i=0;i<output.size();i++){finalOutput[i]=output.get(i); }
//			
//		
//		return(finalOutput);
//		
//	}
//




}
