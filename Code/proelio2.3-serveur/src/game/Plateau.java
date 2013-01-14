package game;

import java.awt.Color;


/**
 * Finalisation du plateau. 
 * On place sur le plateau existant les légionnaires, les sesterces, le laurier et les tentes.
 * C'est une extension de HexagonalStrata.
 * @author vigon
 *
 */
public class Plateau {

	
//INPUT	FIELDS
	private DataGiven dataGiven;
	private HexagonalStrata hexaStrata;
	
	

	
	public Plateau(DataGiven DG) {		
		dataGiven=DG;		
		hexaStrata=new HexagonalStrata(dataGiven);	
	}

	
	
	
	
	public SetOfVertex initialSituation(){
		
		/**
		 * Here, all the work make is hexagonalStrata is called. 
		 */
		SetOfVertex situation=hexaStrata.getStrata();
		
		
		SetOfVertex extr;
		Color theColor;

		
if (dataGiven.nbLegion==2){
			
			
			extr=situation.extract(dataGiven.tentPosition0);
			extr.changeLegion(dataGiven.setOfLegion.element(0));
			extr.changeObjectSouth("tent");
			
			extr=situation.extract(hexaStrata.symetricRankX(dataGiven.tentPosition0));
			extr.changeLegion(dataGiven.setOfLegion.element(1));
			extr.changeObjectSouth("tent");
					
			
		//TENT CHIEF
			
			extr=situation.extract(dataGiven.tentChiefPosition[0]);
			theColor=dataGiven.setOfLegion.element(0).theColor;
			extr.changeObjectSouth("tentChief");
			extr.changeObjectSouthColor(theColor);
			
			extr=situation.extract(dataGiven.tentChiefPosition[1]);
			theColor=dataGiven.setOfLegion.element(1).theColor;
			extr.changeObjectSouth("tentChief");
			extr.changeObjectSouthColor(theColor);

			}
		
		else if (dataGiven.nbLegion==4){
			
		
		extr=situation.extract(dataGiven.tentPosition0);
		extr.changeLegion(dataGiven.setOfLegion.element(0));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(hexaStrata.symetricRankX(dataGiven.tentPosition0));
		extr.changeLegion(dataGiven.setOfLegion.element(1));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(hexaStrata.symetricRankXY(dataGiven.tentPosition1));
		extr.changeLegion(dataGiven.setOfLegion.element(2));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(dataGiven.tentPosition1);
		extr.changeLegion(dataGiven.setOfLegion.element(3));
		extr.changeObjectSouth("tent");
		
		
		
		//TENT CHIEF
		
		extr=situation.extract(dataGiven.tentChiefPosition[0]);
		theColor=dataGiven.setOfLegion.element(0).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
		
		extr=situation.extract(dataGiven.tentChiefPosition[1]);
		theColor=dataGiven.setOfLegion.element(1).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
	
		extr=situation.extract(dataGiven.tentChiefPosition[2]);
		theColor=dataGiven.setOfLegion.element(2).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
		
		extr=situation.extract(dataGiven.tentChiefPosition[3]);
		theColor=dataGiven.setOfLegion.element(3).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);

		
		}
		
		
		
		else if (dataGiven.nbLegion==6){
			
	
						
		extr=situation.extract(dataGiven.tentPosition0);	
		extr.changeLegion(dataGiven.setOfLegion.element(0));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(hexaStrata.symetricRankX(dataGiven.tentPosition0));
		extr.changeLegion(dataGiven.setOfLegion.element(1));
		extr.changeObjectSouth("tent");

		extr=situation.extract(hexaStrata.symetricRankXY(dataGiven.tentPosition1));
		extr.changeLegion(dataGiven.setOfLegion.element(2));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(dataGiven.tentPosition1);
		extr.changeLegion(dataGiven.setOfLegion.element(3));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(hexaStrata.symetricRankX(dataGiven.tentPosition1));
		extr.changeLegion(dataGiven.setOfLegion.element(4));
		extr.changeObjectSouth("tent");
		
		extr=situation.extract(hexaStrata.symetricRankY(dataGiven.tentPosition1));
		extr.changeLegion(dataGiven.setOfLegion.element(5));
		extr.changeObjectSouth("tent");
		
		
		//TENT CHIEF
		
		extr=situation.extract(dataGiven.tentChiefPosition[0]);
		theColor=dataGiven.setOfLegion.element(0).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
		
		extr=situation.extract(dataGiven.tentChiefPosition[1]);
		theColor=dataGiven.setOfLegion.element(1).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
	
		extr=situation.extract(dataGiven.tentChiefPosition[2]);
		theColor=dataGiven.setOfLegion.element(2).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
		
		extr=situation.extract(dataGiven.tentChiefPosition[3]);
		theColor=dataGiven.setOfLegion.element(3).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
		
		extr=situation.extract(dataGiven.tentChiefPosition[4]);
		theColor=dataGiven.setOfLegion.element(4).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);
		
		extr=situation.extract(dataGiven.tentChiefPosition[5]);
		theColor=dataGiven.setOfLegion.element(5).theColor;
		extr.changeObjectSouth("tentChief");
		extr.changeObjectSouthColor(theColor);

		}
		
		
		
		// To put sesterces		
		extr=situation.extract(hexaStrata.addSymetricRankXandY(dataGiven.sestercePosition));
		extr.changeObjectNorth("sesterce");
		
		// To put the Laurel		
		Vertex ver=situation.element(dataGiven.laurelPosition);
		ver.objectCentral="laurel";
		
		return(situation);		
	}
	

	/** 
	 * From now on, we transfer  the method of HexagonalStrata. Perhaps this can be done directly using "extend"
	 * @param r
	 * @return
	 */
	
	
	public int [] neighbor(int r){		
		return(hexaStrata.neighbor(r));		
	}
	
	
	
	
	public int size(){
		return(hexaStrata.strataSize());
	}
		
	public int [] segment(int i){
		return(hexaStrata.segment(i));
	}

	
	public int nbSegment(){
		return(hexaStrata.nbSegment());
	}
	

	
	public String rank2Word(int r){
		return(hexaStrata.rank2Word(r));
		
	}
	
	
	
	public int word2Rank(String word){
		
		return(hexaStrata.word2Rank(word));
		

	}
	
	

	public XY rank2Pos(int r){
		return(hexaStrata.rank2Pos(r));
	}
	
	
}
