package proelio_client;

import java.awt.Color;

public class Legion{	 
	/**
	 * An integer representing the legion 
	 */
	public int rank;
	/**
	 * The name associated
	 */
	public String name;
	/**
	 * The shape of the team
	 */
	public String team;	
	/**
	 * The departure-rank chosen for one displacement
	 */
	public int departure;
	/**
	 * The arrival-rank chosen for one displacement
	 */
	public int arrival;
	/**
	 * The name of the player who lead this legion 
	 * This field is not used yet
	 */	
	public String player;
	/**
	 * The type of action. It can be "moveLegionary" of "moveLaurel"
	 */
	public String typeAction;
	/**
	 * is set to "true" when this legion had already made is choice of action
	 */
	public boolean alreadyPlayed;
	
	public Color theColor;
	public Color theBackgroundColor;
	
	
	//public boolean havePlayedLaurel;

//	the constructor produce a non-existing legion
	public Legion(){
		rank=-1;
		theColor=Color.white;
		name="none";
		team="none";
		departure=-1;
		arrival=-1;
		player="";
		alreadyPlayed=false;
		typeAction="none";

	}

}