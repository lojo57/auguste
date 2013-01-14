package proelio_client;

import java.util.ArrayList;


/**
 * This class produce an empty plateau will all the relation necessary: 
 * the neighborhood 
 * the catalog of lines (= segment). 
 * the bijection rank <-> position
 * the bijection word <-> rank
 * @author vigon
 *
 */

public class HexagonalStrata {

	
// there will be geter/seter (=accesseur) to all the following fields
	//INPUT	
	private DataGiven dataGiven;


	//OUTPUT
	/**
	 * to a rank "r", it sends  the position XY[r] of the corresponding vertex. 
	 */
	private XY[] rank2Pos;
	/**
	 * ot a position (i,j) it sends the rank pos2Rank[i][j]
	 */
	private int[][] pos2Rank;
	/**
	 * the set of all the vertex which will be our plateau
	 */
	private SetOfVertex strata;
	/**
	 *  setOfSegment[a][] is the list of vertices corresponding to the a-th possible line.
	 */
	private int [][] setOfSegment; 
	/**
	 * to a rank "r", it sends a 3-letter word rank2Word[r]
	 */
	private String [] rank2Word;
	/**
	 * the number of vertices of the strata
	 */
	private int strataSize;



	// Constructor
	public HexagonalStrata(DataGiven dG){


		int sizeSegment,m,sizeCat;
		dataGiven=dG;


		// Construction of the bijection rank <-> pos
		/**
		 *  We start by create a boolean Matrix. Each "true" corresponding to a valid position. See comments of createMatric	
		 */
		boolean[][] matrix=createMatrix(dataGiven.baseHexa);
		/**
		 * This list will be change into the array "rank2Pos". 
		 */
		ArrayList<XY> rank2PosCrash= new ArrayList<XY>();
		
		int i,j,r;
		
		// initialisation of pos2Rank : -1 correspond to a non-existing position 
		pos2Rank = new int[matrix.length][matrix[0].length];
		for (i=0;i<matrix.length;i++) {
			for (j=0;j<matrix[0].length;j++){
				pos2Rank[i][j]=-1;				
			}
		}


		r=0;
		for (i=0;i<matrix.length;i++) {
			for (j=0;j<matrix[0].length;j++){
				if (matrix[i][j]) {
					rank2PosCrash.add(new XY(i,j));
					pos2Rank[i][j]=r;
					r++;

				}

			}
		}

		strataSize=rank2PosCrash.size();
		rank2Pos=new XY[strataSize];

		for (r=0;r<strataSize;r++){
			rank2Pos[r]=rank2PosCrash.get(r);
		}




		// Now we add words, and rank to each vertex
		String [] alpha = dataGiven.listOfWord;				    		
		//String [] word=randomListWord(strataSize,alpha);		
		rank2Word=new String[strataSize];
		strata=new SetOfVertex(strataSize); 
		
		for (r=0;r<strataSize;r++){				
			Vertex vertex=strata.element(r);
			vertex.rank=r;
			rank2Word[r]=alpha[r];
			vertex.pos=rank2Pos[r];
			
			if (dataGiven.printRank){vertex.word=(""+vertex.rank);}
			else if (dataGiven.printPos) {vertex.word=(""+vertex.pos.x+","+vertex.pos.y  );}
			else{	
				vertex.word=(alpha[r]);
			}					
		}
		



		// Creation of the SetOfSegment
		int [][][] catalogue;
		catalogue=catalogueDroite(matrix);

		sizeCat=catalogue.length;
		setOfSegment=new int[sizeCat][];



		for (r=0;r<sizeCat;r++){
			sizeSegment=catalogue[r].length;					
			setOfSegment[r]= new int[sizeSegment];
			for (m=0;m<sizeSegment;m++){
				i=catalogue[r][m][0];
				j=catalogue[r][m][1];				
				setOfSegment[r][m]=pos2Rank[i][j]; 				

			}
		}
	}//end of the constructor of HexagonalStrata


	//
	//

	//	// ACCESSEUR	
	//


	public SetOfVertex getStrata(){
		return(strata);
	}



	public int strataSize(){
		return(strataSize);
	}

	public int [] segment(int i){
		return(setOfSegment[i]);
	}

	public int nbSegment(){
		return(setOfSegment.length);
	}


	public String rank2Word(int r){
		return(rank2Word[r]);
	}
	

	public int word2Rank(String word){
		int r=0;		
		while(r<rank2Word.length && ! word.equals(rank2Word[r].toUpperCase())  ) {r++;}			
		if (r<rank2Word.length) return(r);
		else return(-1);
	}


	public XY rank2Pos(int r){
		XY xy;

		if (r<rank2Pos.length){
			xy=rank2Pos[r];		
			return(xy);	
		}
		else
		{
			xy=new XY(-1,-1);
			return(xy);
		}
	}
		
	
	public XY [] rank2Pos(int [] listRank){
		
		XY [] output= new XY[listRank.length];
		
		for (int i=0;i<listRank.length;i++){
			output[i]=rank2Pos(listRank[i]);			
			
		}
		
		return(output);
		
	}
	
			
public ArrayList<XY> rank2Pos(ArrayList <Integer> listRank){
		
		ArrayList<XY> output= new ArrayList<XY>();
		
		for (int i=0;i<listRank.size();i++){
			output.add(rank2Pos(listRank.get(i)));			
			
		}
		
		return(output);		
	}

	
	private int pos2Rank(XY ij){
		int r;
		int i=ij.x;
		int j=ij.y;

		if (i>=0 && j>=0  && i< pos2Rank.length && j < pos2Rank[0].length && pos2Rank[i][j]!=-1   ){

			r=pos2Rank[i][j];
			return(r);

		}
		else {return(-1); }

	}	
	
	
//	private int [] pos2Rank(XY [] listPos){		
//		int [] output=new int [listPos.length];		
//		for (int k=0;k<listPos.length;k++){		
//			output[k]=pos2Rank(listPos[k]);			
//		}		
//		return(output);
//	}

	private ArrayList<Integer>  pos2Rank(ArrayList<XY> listPos){		
		ArrayList<Integer> output=new ArrayList<Integer>();				
		for (int k=0;k<listPos.size();k++){		
			output.add(pos2Rank(listPos.get(k)));			
		}		
		return(output);
	}
	
	
	
	
	
	private int pos2Rank(int i,int j){
		int r;
		

		if (i>=0 && j>=0  && i< pos2Rank.length && j < pos2Rank[0].length && pos2Rank[i][j]!=-1   ){

			r=pos2Rank[i][j];
			return(r);

		}
		else {return(-1); }

	}
	

	
	/**
	 * @param r a rank
	 * @return the array of ranks of neighbor of the vertex "r"
	 */
	
	public int [] neighbor( int r)
	{

		int i,j,s,rr,k;
		ArrayList<Integer> output=new ArrayList<Integer>();
		int [] outputFinal;

		i=rank2Pos[r].x;
		j=rank2Pos[r].y;


		rr=pos2Rank(i+1,j+1); 
		if (rr!=-1)	{
			output.add(rr);			
		}

		rr=pos2Rank(i+1,j-1); 
		if (rr!=-1)	{
			output.add(rr);			
		}

		rr=pos2Rank(i-1,j+1); 
		if (rr!=-1)	{
			output.add(rr);			
		}

		rr=pos2Rank(i-1,j-1); 
		if (rr!=-1)	{
			output.add(rr);			
		}

		rr=pos2Rank(i+2,j); 
		if (rr!=-1)	{
			output.add(rr);			
		}

		rr=pos2Rank(i-2,j); 
		if (rr!=-1)	{
			output.add(rr);			
		}

		s=output.size();				
		outputFinal=new int[s];	

		for (k=0;k<s;k++){
			outputFinal[k]=output.get(k);
		}

		return(outputFinal);

	}


	

	// to help to build datas



//	private static int[] randomPermutation(int i){
//
//		int [] tab = new int[i];
//
//		for (int j=0;j<i;j++){
//			tab[j]=j;
//		}
//
//		for (int k = tab.length - 1; k > 0; k--) {
//			int w = (int)Math.floor(Math.random() * (k+1));
//			int temp = tab[w];
//			tab[w] = tab[k];
//			tab[k] = temp;
//		}
//
//		return(tab);
//
//	}
//	private static String[] randomListWord(int siz, String [] alpha){
//
//		int [] randomIndex=randomPermutation(alpha.length);
//
//
//		String [] alphaPermuted=new String[siz]; 
//
//		for (int k=0;k<siz;k++){
//			alphaPermuted[k]=alpha[randomIndex[k]];		   
//		}
//		return(alphaPermuted);
//
//	}

	

	
	
/**
 * Create a 2 dimensional boolean Matrix which represent the hexagonal plateau.
 * 
 *  Here is the matrix when baseHexa=3. We just write the "true" by "T" and "false" by "spaces"
 *  
 *          T T T
 *         T T T T
 *        T T T T T
 *         T T T T 
 *          T T T
 * 
 * As you can see, this matrix is made by an alternation of "true" and "false", and then to arise 4 angles with "false"
 */

	private boolean[][] createMatrix(int baseHexa){

		
		int xPlat = baseHexa+(baseHexa-1)+2*baseHexa;
		int yPlat = 2*baseHexa-1+2;

		boolean [][] plateau=new boolean[xPlat][yPlat]; 

		int i, j;


		for (i=0;i<xPlat;i++){plateau[i][yPlat-1]=false;}
		for (i=0;i<xPlat;i++){plateau[i][0]=false;}
		for (j=0;j<yPlat;j++){plateau[0][j]=false;}
		for (j=0;j<yPlat;j++){plateau[xPlat-1][j]=false;}



// To alternate true and false

		for (i = 1; i < xPlat-1; i++)
			for (j = 1; j < yPlat-1; j++) {
				{
					if ((i + j) % 2 == 1) {
						plateau[i][j] = false;
					} else {
						plateau[i][j] = true;
					}
				}
			}



//To erase angles
		
		ArrayList<XY> erase =new ArrayList<XY>();
				
		for (i=0;i<=baseHexa;i++)
		{
			for (j=0;j<=baseHexa-i;j++)
			{
				erase.add(new XY(i,j));
			}
		}
					
		erase.addAll(addSymetricPosXandY(erase));
	
		for (XY m:erase) {
			i=m.x;
			j=m.y; 
			plateau[i][j]=false;
		}

		return(plateau);

	}


	
	


	private static int [][][] catalogueDroite(boolean plateau[][])
	{
		int xPlat=plateau.length;
		int yPlat=plateau[0].length;
		int baseHexa=(yPlat-1)/2;
		int nbDiag=2*baseHexa-1;
		int nbHoriz=yPlat-2;
		int [][][] catalogue=new int[2*nbDiag+nbHoriz][2][2];//Strange
		int i,j,k,m,a;


		a=0;//position courante dans "catalogue"

		//Diagonales "\" : quand i agumente, j augmente.
		for (i=-baseHexa+1;i<xPlat-baseHexa;i++)
		{
			if (i%2==0)
			{
				k=0;//position courante dans "liste"
				int[][] liste=new int[yPlat][2];
				for (m=0;m<yPlat;m++)
				{
					if (i+m>0 && i+m<xPlat)
					{
						if ( plateau[i+m][m])
						{
							liste[k][0]=i+m;
							liste[k][1]=m;
							k++;

						}
					}
				}

				catalogue[a]=new int[k][2];
				System.arraycopy(liste, 0, catalogue[a], 0, k);
				a++;																			
			}
		}


		//Diagonales "/" : quand i augmente, j descend.
		for (i=baseHexa;i<xPlat+baseHexa;i++)
		{
			if (i%2==0)
			{
				k=0;//position courante dans "liste"
				int[][] liste=new int[yPlat][2];
				for (m=0;m<yPlat;m++)
				{
					if (i-m>0 && i-m<xPlat)
					{
						if ( plateau[i-m][m])
						{
							liste[k][0]=i-m;
							liste[k][1]=m;
							k++;

						}
					}
				}


				catalogue[a]=new int[k][2];
				System.arraycopy(liste, 0, catalogue[a], 0, k);
				a++;
			}
		}


		//droite horizontales
		for (j=1;j<yPlat-1;j++)
		{
			k=0;//position courante dans "liste"
			int[][] liste=new int[yPlat][2];

			for (i=0;i<xPlat;i++)
			{
				if ( plateau[i][j] )
				{
					liste[k][0]=i;
					liste[k][1]=j;
					k++;

				}

			}

			catalogue[a]=new int[k][2];
			System.arraycopy(liste, 0, catalogue[a], 0, k);
			a++;
		}

		return(catalogue);
	}




	
	
	
	
	/**
	 * All the following methods allow to symetrise a list of position or a list of rank, according to X and/or Y axes. 
	 * @param theList  The list to symetrise
	 * @return  The list symmetrised : "add" means that the symmetric positions are add to the original list
	 */
	
	public ArrayList<XY> addSymetricPosX( ArrayList<XY> theList)  {
		int k;
		ArrayList<XY> output=new ArrayList<XY>();
		
		int xPlat = dataGiven.baseHexa+(dataGiven.baseHexa-1)+2*(dataGiven.baseHexa-1)+2;

		output.addAll(theList);
		
		for (k=0;k<theList.size();k++)
		{
			output.add(new XY( xPlat-1-theList.get(k).x,theList.get(k).y));
		}
		return(output);
	}

	
	
	/**
	 * A variant of the previous method. This time, the input list is not included in the return.
	 * @param theList
	 * @return 
	 */
	public ArrayList<XY> symetricPosX( ArrayList<XY> theList)  {
		int k;
		ArrayList<XY> output=new ArrayList<XY>();
		
		int xPlat = dataGiven.baseHexa+(dataGiven.baseHexa-1)+2*(dataGiven.baseHexa-1)+2;
		
		for (k=0;k<theList.size();k++)
		{
			output.add(new XY( xPlat-1-theList.get(k).x,theList.get(k).y));
		}
		return(output);
	}
	
	
	public ArrayList<XY> addSymetricPosY( ArrayList<XY> theList)  {
		int k;
		ArrayList<XY> output=new ArrayList<XY>();
		
		int yPlat = 2*dataGiven.baseHexa-1+2;

		output.addAll(theList);
		
		for (k=0;k<theList.size();k++)
		{
			output.add(new XY( theList.get(k).x,yPlat-1-theList.get(k).y));
		}
		return(output);
	}

	
	public ArrayList<XY> symetricPosY( ArrayList<XY> theList)  {
		int k;
		ArrayList<XY> output=new ArrayList<XY>();
		
		int yPlat = 2*dataGiven.baseHexa-1+2;
		
		for (k=0;k<theList.size();k++)
		{
			output.add(new XY( theList.get(k).x,yPlat-1-theList.get(k).y));
		}
		return(output);
	}
	
	public ArrayList<XY> symetricPosXY( ArrayList<XY> theList)  {
		ArrayList<XY> output=new ArrayList<XY>();		
		output=symetricPosX(theList);
		output=symetricPosY(output);					
		return(output);
	}	
	
	public ArrayList<XY> addSymetricPosXandY( ArrayList<XY> theList)  {
		ArrayList<XY> output=new ArrayList<XY>();		
		output.addAll(addSymetricPosX(theList));
		output.addAll(addSymetricPosY(output));		
		return(output);
	}
	
	
	
	

	public ArrayList<Integer> addSymetricRankX(ArrayList<Integer> inputRank){				
		ArrayList <Integer> outputRank=new ArrayList<Integer>();
		ArrayList <XY> pos=new ArrayList<XY>();				
		pos=rank2Pos(inputRank);
		pos=addSymetricPosX(pos);		
		outputRank=pos2Rank(pos);				
		return(outputRank);			
	}
	
	public ArrayList<Integer> symetricRankX(ArrayList<Integer> inputRank){				
		ArrayList <Integer> outputRank=new ArrayList<Integer>();
		ArrayList <XY> pos=new ArrayList<XY>();				
		pos=rank2Pos(inputRank);
		pos=symetricPosX(pos);		
		outputRank=pos2Rank(pos);				
		return(outputRank);			
	}
	
	public ArrayList<Integer> addSymetricRankY(ArrayList<Integer> inputRank){				
		ArrayList <Integer> outputRank=new ArrayList<Integer>();
		ArrayList <XY> pos=new ArrayList<XY>();				
		pos=rank2Pos(inputRank);
		pos=addSymetricPosY(pos);		
		outputRank=pos2Rank(pos);				
		return(outputRank);			
	}
	public ArrayList<Integer> symetricRankY(ArrayList<Integer> inputRank){				
		ArrayList <Integer> outputRank=new ArrayList<Integer>();
		ArrayList <XY> pos=new ArrayList<XY>();				
		pos=rank2Pos(inputRank);
		pos=symetricPosY(pos);		
		outputRank=pos2Rank(pos);				
		return(outputRank);			
	}
	
	public ArrayList<Integer> symetricRankXY(ArrayList<Integer> inputRank){				
		ArrayList <Integer> outputRank=new ArrayList<Integer>();
		ArrayList <XY> pos=new ArrayList<XY>();				
		pos=rank2Pos(inputRank);
		pos=symetricPosXY(pos);		
		outputRank=pos2Rank(pos);				
		return(outputRank);			
	}		
	public ArrayList<Integer> addSymetricRankXandY(ArrayList<Integer> inputRank){				
		ArrayList <Integer> outputRank=new ArrayList<Integer>();
		ArrayList <XY> pos=new ArrayList<XY>();				
		pos=rank2Pos(inputRank);
		pos=addSymetricPosXandY(pos);		
		outputRank=pos2Rank(pos);				
		return(outputRank);			
	}
	
	
	
	
	
	public int[] addSymetricRankX(int[] inputRank){				
		ArrayList<Integer> theList=new ArrayList<Integer>();
		int[] theArray=new int[inputRank.length]; 		
		theList=Tool.array2List(inputRank);
		theList=addSymetricRankX(theList);
		theArray=Tool.list2Array(theList);			
		return(theArray);			
	}
	public int[] symetricRankX(int[] inputRank){				
		ArrayList<Integer> theList=new ArrayList<Integer>();
		int[] theArray=new int[inputRank.length]; 		
		theList=Tool.array2List(inputRank);
		theList=symetricRankX(theList);
		theArray=Tool.list2Array(theList);			
		return(theArray);			
	}
	public int[] addSymetricRankY(int[] inputRank){				
		ArrayList<Integer> theList=new ArrayList<Integer>();
		int[] theArray=new int[inputRank.length]; 		
		theList=Tool.array2List(inputRank);
		theList=addSymetricRankY(theList);
		theArray=Tool.list2Array(theList);			
		return(theArray);			
	}
	public int[] symetricRankY(int[] inputRank){				
		ArrayList<Integer> theList=new ArrayList<Integer>();
		int[] theArray=new int[inputRank.length]; 		
		theList=Tool.array2List(inputRank);
		theList=symetricRankY(theList);
		theArray=Tool.list2Array(theList);			
		return(theArray);			
	}
	public int[] symetricRankXY(int[] inputRank){				
		ArrayList<Integer> theList=new ArrayList<Integer>();
		int[] theArray=new int[inputRank.length]; 		
		theList=Tool.array2List(inputRank);
		theList=symetricRankXY(theList);
		theArray=Tool.list2Array(theList);			
		return(theArray);			
	}
	public int[] addSymetricRankXandY(int[] inputRank){				
		ArrayList<Integer> theList=new ArrayList<Integer>();
		int[] theArray=new int[inputRank.length]; 		
		theList=Tool.array2List(inputRank);
		theList=addSymetricRankXandY(theList);
		theArray=Tool.list2Array(theList);			
		return(theArray);			
	}

	



}
