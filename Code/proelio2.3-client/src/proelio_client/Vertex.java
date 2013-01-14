package proelio_client;

import java.awt.Color;
import java.util.ArrayList;

public class Vertex 
{

	  /**
	   * rank of the vertex.
	   * Ex: 0 is the rank of the vertex the more at the left. 
	   * To make appears the position : set "dataGiven.printPosition" to true
	   */
	  public int rank;
	  /**
	   * all the information about the lion of the pion which perhaps is on this vertex
	   * informations are: name, theColor, team ...
	   */
	  public Legion legion;
	  /**
	   * integer position : two integer.
	   *  To make appears the position : set "dataGiven.printPosition" to true
	   */
	  public XY pos;		  
	  /**
	   * The 3 letters word who help people to indicate a vertex on the hard-plateau
	   */
	  public String word;
	  /**
	   * the vertex is free ?
	   */
	  public boolean free;
	  /**
	   * the legionary has an armor ?
	   */
	  public boolean armor;	  
	  /**
	   * is there a collision here ?
	   */
	  public boolean collision;	  
	  /**
	   * does the legionary is killed ?
	   */
	  public boolean killed;  
	  /**
	   * object put on a center of the vertex. It can be : the laurel image, of the bang image which indicate a colision.  
	   */
	  public String objectCentral;
	  /**
	   * object put on the south. It can be : tent (departure position of legionaries), tentChief (departure position and purpose of the laurel), 
	   */
	  public String objectSouth;
	  /**
	   * Color of the tents. 
	   */
	  public Color objectSouthColor;
	  /**
	   * object put on the south. It can be : a sesterce 
	   */
	  public String objectNorth;
	  /**
	   * During a displacement : it indicates the rank of the arrival vertex 
	   */
	  public int travelTo;
	  /**
	   * During a tenaille : it indicates the other legionaries which help to make the tenaille
	   */	  
	  public ArrayList<Integer> tenailleTo;
	  /**
	   * During a tenaille : it indicates the ranks of the legionaries which he kill. 
	   */		  
	  public ArrayList<Integer> winFightAgainst;
	  
	  public boolean selected;
	  public boolean isHighlighted;
	  
	  /**
	   * Constructors
	   */
	  public Vertex()
	  { 
		  legion= DataGiven.emptyLegion;
		  rank=0;	
		  free=true;
		  armor=false;
		  killed=false;
		  collision=false;
		  word="not";
		  pos=new XY(0,0);
		  objectNorth="none";
		  objectSouth="none";
		  objectCentral="none";
		  objectSouthColor=Color.white;		  
		  travelTo=-1;
		  tenailleTo=new ArrayList<Integer>();
		  winFightAgainst=new ArrayList<Integer>();		  	          
	  }	  
}