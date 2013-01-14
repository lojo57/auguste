package proelio_client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Stroke;



/**
 * All the data are writed and compute here. 
 * @author vigon
 *
 */
public class DataGiven
{
	// Main parameters (here is the default values)
	/**
	 * The number of vertices in the base of the hexagon
	 */
	public int baseHexa=9;// 5,7,or 9
	/**
	 * the number of Legion
	 */
	public int nbLegion=4;//2 or  4 or 6
	/**
	 * The number of team
	 */
	public int nbTeam=2;// 2 or 3	
	
	
	/**
	 * The size of the window
	 */
	public Dimension dimCentralPanel=new Dimension(400,400);	
	public Dimension dimWindow=new Dimension(1000,1000);;





	//controlers
	/**
	 * set to "true" to write ranks of vertices instead of 3 letters words
	 */
	public boolean printRank=false;
	/**
	 * set to "true" to write positions of vertices instead of 3 letters words
	 */
	public boolean printPos=false;
	/**
	 * set to "true" make the empty plateau of the physical game. This will print explicitly the tent (departure position of legionaries). 
	 */
	public boolean emptyPlateau=true;
	
	public boolean southPanelVisible=true;
	public boolean eastPanelVisible=true;
	







	/**
	 * 3 following fields give the size and the shape of hexagon :
	 *           
	 *             O     
	 *            OOO    
	 *           AOOOD   
	 *           OOOOO
	 *           BOOOO
	 *            OOO
	 *             C
	 *             
	 *           unit.y = A<->C
	 *           unit.x = A<->D
	 *           longSideHexa= A<->B
	 *           shortSideHexa= B<->C 	
	 *           
	 *           The parameter unit is very important. All other dimension will be proportional to it. 
	 * 
	 */


	public XY unit;
	public int longSideHexa;
	public int shortSideHexa;


	/**
	 * panels dimensions
	 */
	public Dimension dimSouthPanel;
	public Dimension dimNorthPanel;
	public Dimension dimEastPanel;

	/**
	 * dimension of the graphical objects
	 */
	public Dimension dimCircle;
	public Dimension dimSquare;
	public Dimension dimCircleArmor;
	public Dimension dimSquareArmor;	
	public Dimension dimDague;
	public Dimension dimArrow;
	public Dimension dimLaurel;
	public Dimension dimBang;
	public Dimension dimSesterce;
	public Dimension dimCircleTenaille;



/**
 * The array of 3 letters words. 
 */
	public  String [] listOfWord;

	/**
	 * Will indicate the colors and shape of each legion
	 */
	public SetOfLegion setOfLegion;


	/**
	 * Tents are the departure position of legionaries. 
	 * BE CAREFULL : Actually, it is not position, but the rank which will be writed in this list and also in the next. 
	 */
	public int [] tentPosition0;
	public int [] tentPosition1;
	/**
	 * Tents chief are departure position but also the vertices where we have to lead the laurel to win
	 */
	public int [][] tentChiefPosition;
	/**
	 * A sesterce allow to win an armor 
	 */
	public int [] sestercePosition;	
	/**
	 * The Laurel is put in the center of the plateau
	 */
	public int laurelPosition;
	
	/** emptyLegion represent a non existing legion (it is the "emptyset")*/
	public static final Legion emptyLegion=new Legion();



	/**
	 * The strocke is the style of line. 
	 */
	public Stroke strokeNormal=new BasicStroke(3.0f,                     // Line width
			BasicStroke.CAP_ROUND,    // End-cap style
			BasicStroke.JOIN_ROUND
			); // Vertex join style
	/**
	 *  Dash (pointillŽs) are used for killed legionaries. 
	 */
	public Stroke strokeDash = new BasicStroke(3.0f,   // Width
			BasicStroke.CAP_ROUND,    // End cap
			BasicStroke.JOIN_ROUND,    // Join style
			10.0f,                     // Miter limit
			new float[] {5.0f,10.0f}, // Dash pattern
			0.0f);   // Dash phase

	/**
	 * Font of the 3 letter words
	 */
	public Font font = new Font("Andale Mono", Font.PLAIN, 16);


	
	
public void computeUnit(Dimension dimWin,int baseH){
	
	// compute dimensions 
	dimSouthPanel=new Dimension(dimWindow.width,30);
	dimEastPanel=new Dimension(60,dimWindow.height);
	unit=new XY(dimCentralPanel.width/2/baseHexa,dimCentralPanel.height/(2*baseHexa+1));	
	
	
}
	




	public void computeData(XY uni,int baseH,int nbLegio, int nbTea)
	{
		int i;
		unit=uni;
		baseHexa=baseH;
		nbLegion=nbLegio;
		nbTeam=nbTea;

		longSideHexa=unit.x*4/7;
		shortSideHexa=unit.y-longSideHexa;

		dimCircle=new Dimension(unit.x*2/3,unit.y*2/3);
		dimSquare=new Dimension(unit.x*3/5,unit.y/2);

		dimCircleArmor=new Dimension(dimCircle.width,dimCircle.height*4/3);
		dimSquareArmor=new Dimension(dimSquare.width,dimSquare.height*4/3);

		dimArrow=new Dimension(unit.x/5,unit.x/8);
		dimDague=new Dimension(unit.x*4/5,unit.x/8);

		dimLaurel=new Dimension(unit.x-10,longSideHexa);
		dimBang=new Dimension(unit.x*3/2,longSideHexa*3/2);
		dimSesterce=new Dimension(unit.x/4,unit.y/4);
		dimCircleTenaille=new Dimension(unit.x/4,unit.y/4);

		if (baseHexa==5){

			int [] tP0={/*0,1,*/2/*,4,7,8,12*/};
			tentPosition0=tP0;
			int [] tP1={14,9,23,18,13,22,27};
			tentPosition1=tP1;	
			
			int [][] tCP = {{0},
							{60},
							{46},
							{14},
							{50},
							{10}};
			tentChiefPosition=tCP;

			int [] sP={21,26};
			sestercePosition=sP;


			laurelPosition=1;//30
		}
		else if (baseHexa==7)
		{
			int [] tP0={/*0,1,2,3,4,5,7,8,12,17,18,*/24};
			tentPosition0=tP0;
			int [] tP1={/*14,20,27,40,53,26,33,46,32,39,45,*/52};
			tentPosition1=tP1;	

			int [][] tCP = {{0,1,2},
							{124,125,126},
							{86,99,106},
							{20,27,40},
							{92,105,111},
							{15,21,34}};
			tentChiefPosition=tCP;

			int [] sP={62,50,44,57};
			sestercePosition=sP;

			laurelPosition=63;
		}
		else if (baseHexa==9)
		{
			int [] tP0={0,1,2,3,4,5,7,8,11,12,13,17,18,24,31,32,40};
			tentPosition0=tP0;
			int [] tP1={44,35,27,61,52,43,34,78,69,60,51,86,77,68,59,85,76};
			tentPosition1=tP1;	

			int [][] tCP = {{0,1,2},
							{214,215,216},
							{155,172,181},
							{35,44,61},
							{163,180,188},
							{28,36,53}};
			tentChiefPosition=tCP;

			int [] sP={104,105,106,107,10,30,56,82};
			sestercePosition=sP;

			laurelPosition=48;//108
		}
		else if (baseHexa==11)
		{
			int [] tP0={0,1,3,2,5,6,4,7,11,8,12,17,9,13,18,24,31,40,32};
			tentPosition0=tP0;
			int [] tP1={65,86,107,54,44,128,75,96,117,64,85,106,35,53,74,95,84,105,116};
			tentPosition1=tP1;	

			int [][] tCP = {{0,1,2,3,5},
							{325,327,328,329,330},
							{223,244,265,276,286},
							{44,54,65,86,107},
							{233,254,275,285,294},
							{36,45,55,76,97}};	
			tentChiefPosition=tCP;

			int [] sP={15,38,69,133,160,161,162,164};
			sestercePosition=sP;

			laurelPosition=165;
		}

		
		/**Legion are true legions */
		if (nbTeam==2 && nbLegion==2 ){

			setOfLegion=new SetOfLegion(2);

			setOfLegion.element(0).team="circle";
			setOfLegion.element(0).name="red";
			setOfLegion.element(0).theColor=Color.red;
			setOfLegion.element(0).theBackgroundColor= new Color(0xFF6666);

			setOfLegion.element(1).team="square";
			setOfLegion.element(1).name="blue";
			setOfLegion.element(1).theColor=Color.blue;
			setOfLegion.element(1).theBackgroundColor= new Color(0x6666FF);


		}
		else if (nbTeam==2 && nbLegion==4 ){

			setOfLegion=new SetOfLegion(4);

			setOfLegion.element(0).team="circle";
			setOfLegion.element(0).name="red";
			setOfLegion.element(0).theColor=Color.red;
			setOfLegion.element(0).theBackgroundColor= new Color(0xFF6666);


			setOfLegion.element(1).team="circle";
			setOfLegion.element(1).name="yellow";
			setOfLegion.element(1).theColor=Color.yellow;
			setOfLegion.element(1).theBackgroundColor= new Color(0xFFFF66);

			
			setOfLegion.element(2).team="square";
			setOfLegion.element(2).name="blue";
			setOfLegion.element(2).theColor=Color.blue;
			setOfLegion.element(2).theBackgroundColor= new Color(0x6666FF);


			setOfLegion.element(3).team="square";
			setOfLegion.element(3).name="green";
			setOfLegion.element(3).theColor=Color.green;
			setOfLegion.element(3).theBackgroundColor= new Color(0x66FF66);


		}

		else if (nbTeam==2 && nbLegion==6 ){

			setOfLegion=new SetOfLegion(6);

			setOfLegion.element(0).team="circle";
			setOfLegion.element(0).name="red";
			setOfLegion.element(0).theColor=Color.red;
			setOfLegion.element(0).theBackgroundColor= new Color(0xFF6666);

			
			setOfLegion.element(1).team="circle";
			setOfLegion.element(1).name="orange";
			setOfLegion.element(1).theColor=new Color(0xFF8C00);
			setOfLegion.element(1).theBackgroundColor= new Color(0xFFD044);

			
			setOfLegion.element(2).team="circle";
			setOfLegion.element(2).name="yellow";
			setOfLegion.element(2).theColor=Color.yellow;
			setOfLegion.element(2).theBackgroundColor= new Color(0xFFFF99);
			
			
			setOfLegion.element(3).team="square";
			setOfLegion.element(3).name="blue";
			setOfLegion.element(3).theColor=Color.blue;
			setOfLegion.element(3).theBackgroundColor= new Color(0x6666FF);

			
			setOfLegion.element(4).team="square";
			setOfLegion.element(4).name="green";
			setOfLegion.element(4).theColor=Color.green;
			setOfLegion.element(4).theBackgroundColor= new Color(0x99FF99);

			
			setOfLegion.element(5).team="square";
			setOfLegion.element(5).name="cyan";
			setOfLegion.element(5).theColor=new Color(0x00CED1);
			setOfLegion.element(5).theBackgroundColor= new Color(0xAFEEEE);


		}

		String word="ACE, ADA, ADO, AGA, AGE, ARG, AGI, AID, AYE, AIR, HAC, AIT, ALE, ALU, AME, AMI, ANA, ANE, ANS, API, ARA, ARC, ARE, ARS, ART, ASA, ASE, HOX, AVE, AXA, AXE, AYS, BAC, BAH, BAI, BAL, BAN, BAR, BAS, BAT, BAU, BEA, BEC, BEH, BEL, BEN, BER, BEY, BIC, BIO, BIP, BIS, BIT, BLE, BOA, BOB, BOF, BOG, BOL, BON, BOP, BOT, BOX, BOY, BRU, BUE, BUG, BUN, BUS, BUT, BUF, BYE, CAB, CAF, CAL, CAP, CAR, CAS, CEP, CES, CET, CHU, CIF, CIL, CIS, CLF, COB, COI, COL, CON, COQ, COR, COU, CRE, CRI, CRU, CUT, DAB, DAL, DAM, DAN, DAO, DEB, DER, DAI, DEY, DIA, DIN, DIS, DIT, DIX, DOC, DOL, DOM, DON, DOP, DOS, DOT, DRU, DRY, DUB, DUC, DUE, DUO, DUR, DUT, EAU, ECO, ECU, EGO, ELU, EMU, EON, EPI, ERE, ERG, ERS, EST, ETA, ETE, EUG, EUH, EUS, EUT, EUL, EUX, EWE, EXO, FAC, FAF, FAN, FAQ, FAR, FAT, FAX, FEE, FER, FEU, FEZ, FIA, FIC, FIE, FIM, FIL, FIN, FIS, FIT, FLA, FOB, FOC, FOG, FOI, FOL, FOR, FOU, FOX, FUI, FUN, FUR, FUT, GAG, GAL, GAN, GAP, GAZ, GEL, GEO, GEX, GIN, GIS, GIT, GLU, GOI, GON, GOS, GOY, GRE, GRI, GUI, GUR, GUS, GYM, HAI, HAN, HEM, HEP, HEU, HIA, HIC, HIE, HIK, HIP, HIT, HOP, HOT, HOU, HUA, HUB, HUE, HUR, HUI, HUM, HUN, IBN, IBO, ICI, IDE, IFS, ILE, ILS, ION, IPE, IRA, IRE, ISO, IVE, IXA, IXE, JAB, JAM, JAN, JAR, JAS, JET, JEU, JOB, JUS, KAN, KAS, KEA, KHI, KID, KIF, KIL, KIP, KIR, KIT, KOB, KOP, KOT, KRU, KSI, KWA, KYU, LAC, LAD, LAI, LAO, LAS, LEF, LEK, LEM, LES, LET, LEU, LEV, LEZ, LIA, LIE, LIM, LIN, LIT, LOB, LOF, LOG, LOI, LOS, LOT, LUE, LUI, LUT, LUX, LYS, MAC, MAI, MAL, MAN, MAO, MAS, MAT, MAX, MEC, MEL, MEO, MER, MET, MIE, MIL, MIN, MIR, MIS, MIT, MIX, MMM, MOA, MOB, MOI, MOL, MON, MOR, MOS, MOT, MOU, MOX, MUA, MUE, MUC, MUG, MUR, MUT, NAY, NOU, NEF, NEM, NEO, NIS, NET, NEY, NEZ, NIA, NIB, NID, NIE, NIP, NIF, NIT, NON, NOS, NUA, NUC, NUI, NUL, OBA, OBI, ODE, OFF, OHE, OHM, OIE, OIL, OKA, OLA, OLE, ONC, ONT, OPE, ORE, ORS, OSA, OSE, OSI, OST, OTA, OTE, HOG, OUD, OUF, OUH, OUI, OUT, OVE, OVI, OXO, OYE, PAF, PAL, PAN, PAP, PAR, PAS, PAT, PEC, PEP, PET, PEU, PFF, PHI, PHO, PIC, PIE, PIF, PIN, PIU, PLI, PLU, POP, POT, POU, PRE, PRO, PSI, PUT, PSY, PUA, PUB, PUE, PUF, PUR, PUY, QAT, QIN, QUE, QUI, RAB, RAC, RAD, RAI, RAN, RAM, RAP, RAS, RAT, RAY, REA, REB, REG, REM, RHE, RHO, RIA, RIF, RIO, RIZ, ROB, ROC, ROI, ROM, ROS, ROT, ROF, RUA, RUE, RUC, RUZ, RYE, SAC, SAI, SAL, SAR, SAS, SAX, SEC, SEL, SEN, SEP, SES, SET, SIC, SIL, SIR, SIS, SIX, SKA, SKI, SOC, SOI, SOL, SOM, SON, SOT, SOU, SPA, SPI, SUA, SUC, SUD, SUE, SUB, SUR, SUF, SUS, SUT, SCU, TAC, TAF, TAG, TAN, TAO, TAR, TAM, TAS, TAU, TEC, TEK, TEL, TEP, TER, TES, TEF, TET, TEX, THE, TIC, TIF, TIN, TIP, TIR, TOC, TOI, TOM, TON, TOP, TOT, TRI, TUA, TUB, TUE, TUL, TUF, TUT, UNE, UNI, UNS, URE, USA, USE, USO, UTE, VAL, VAN, VAR, VAS, VAU, VER, VET, VIA, VIE, VIF, VIL, VIN, VIS, VIT, VIC, VOL, VOS, VUE, WAD, WAP, WAX, WEB, WOK, WON, WUS, YAK, YAM, YEN, YET, YIN, YOD, YOP, ZEC, ZAH, ZEF, ZEK, ZEN, ZIG, ZIP, ZOB, ZOE, ZOO, ZOU, ZUP, ZUT";
		int nbWord=word.length();
		listOfWord=new String[nbWord/5];
		for (i=0;i<nbWord/5;i++)
		{
			listOfWord[i]=word.substring(i*5,i*5+3);
		}
	}//end of method computeData
}//end class