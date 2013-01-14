package proelio_client;

import java.awt.Color;
import java.util.ArrayList;


/**
 * It is a set of vertex. It is implemented by an array of vertex (similarly to setOfLegion).
 *  In the whole setOfVertex (called situation or initialSituation), the index is also the rank. 
 *  But in a subSet (an extraction of Situation), the index and the rank of the vertex are different
 * 
 * @author vigon
 *
 */
public class SetOfVertex {


	/**
	 * The array of vertex
	 */
	private Vertex [] content;
	/**
	 * The number of vertex
	 */
	private int size;
	/**
	 * The number of Fights during a turn
	 */
	public int nbFight;
	/**
	 * The number of collisions during a turn
	 */
	public int nbCollision;
	/**
	 * The number of tenaille during a turn
	 */
	public int nbTenaille;


//Constructors: 	
	public SetOfVertex(){
		size=0;
	}
	
	
	
	public SetOfVertex(int i){
		int r;

		size=i;
		nbFight=0;
		nbCollision=0;
		nbTenaille=0;

		content= new Vertex[i];
		for (r=0;r<i;r++){
			content[r]=new Vertex();
			content[r].rank=r;			
		}
	}





	
/**
 * @return the number of vertex
 */
	public int size(){

		return(size);
	}

/**
 * @param i  the index of the vertex to get
 * @return the vertex at index i. 
 */
	public Vertex element(int i){
		return(content[i]);
	}


	

	
/**
 * 
 * @param i the index of the vertex to get
 * @param vertex get the vertex with index i
 */
	public  void  setContent(int i,Vertex vertex){
		this.content[i]=vertex;
	}




	 	
/**
 * Be careful: Extraction is not a copy : To change the extracted setOfVertex would change the original one. 
 * @param list The list of index to extract.  
 * @return The extracted vertex
 */
	
	public  SetOfVertex extract(int [] list){

		int r,s;
		s=list.length;
		SetOfVertex output = new SetOfVertex(s);

		for (r=0;r<s;r++){
			output.setContent(r,content[list[r]] );
		}

		return(output);
	}
	

	/**
	 * It change all the color and the team of the setOfVertex (in practice, this setOfVertex is only an extraction of the whole "initialSituation").
	 * @param col  The color to change
	 * @param team The team to change
	 */		
		public void changeLegion(Legion legion){
	
			int r;
			Vertex vertex;
	
			for (r=0;r<this.size;r++){
				vertex=this.element(r);
				vertex.legion=legion;
				vertex.free=false;	
				this.setContent(r, vertex);						
			}
		}
	
	
/**
 *  It change all the object and the team of the setOfVertex (in practice, this setOfVertex is only an extraction of the whole "initialSituation").
 * @param obj
 */		
		public  void changeObjectSouth(String obj){
			for (int r=0;r<size;r++){				
				content[r].objectSouth=obj;									
			}
		}
		
	
		// Other method work similarly
		
		public  void changeObjectNorth(String obj){
			for (int r=0;r<size;r++){				
				content[r].objectNorth=obj;									
			}
		}

		
		
		public  void changeObjectSouthColor(Color theColor){
			for (int r=0;r<size;r++){				
				content[r].objectSouthColor=theColor;									
			}
		}
		
				
		public  void cleanKilled(){
	
			for (int r=0;r<size;r++){
				
				if (content[r].killed){
				content[r].armor=false;
				content[r].killed=false;
				content[r].legion=DataGiven.emptyLegion;	
				content[r].free=true;
				}
				
			}
		}
		
		
		

		public  void cleanTenaille(){
			
			nbTenaille=0;
	
			for (int r=0;r<size;r++){
				
				if (content[r].tenailleTo.size()>0){
					content[r].tenailleTo=new ArrayList<Integer>();
				}
				
			}
		}
		
		
		

		public  void cleanFight(){
			
			nbFight=0;
	
			for (int r=0;r<size;r++){
				
				if (content[r].winFightAgainst.size()>0){
					content[r].winFightAgainst=new ArrayList<Integer>();
				}
				
			}
		}
		

		public  void cleanTravel(){
	
			for (int r=0;r<size;r++){
				
				
					content[r].travelTo=-1;
				
				
			}
		}
	
	

		public  void cleanCollision(){
			
			nbCollision=0;
			
			for (int r=0;r<size;r++){				
				if (content[r].collision){
						content[r].collision=false;
					content[r].objectCentral="none";
				}								
			}
		}
	


}
