package game;

import java.util.ArrayList;


/**
 * U=Utilities.  This little class gather some general methods. Perhaps this can be put in an independent library. 
 * @author vigon
 *
 */
public class Tool {


	
	
	/**
	 * Conversion of an Array ot integer to a list of integer
	 * @param theArray the array
	 * @return the list
	 */
	
	public static ArrayList<Integer> array2List(int [] theArray){
		
		ArrayList<Integer> theList =new ArrayList<Integer>();
		
		for (int i=0;i<theArray.length;i++){
			theList.add(theArray[i]);			
		}		
		return(theList);
		
	}
	
	

	
	public static int[] list2Array(ArrayList<Integer> theList){
		
		int[] theArray=new int[theList.size()];
		
		
		for (int i=0;i<theList.size();i++){
			theArray[i]=theList.get(i);			
		}
		
		
		return(theArray);
		
		
	}


	
	

	
	
	/**
	 * To now if an integer belongs to an array. This method already exist for list. 
	 * @param list
	 * @param elem
	 * @return
	 */
	public static boolean contain(int [] list,int elem){
		
		boolean output=false;
		
		for (int i=0;i<list.length;i++){
			if (list[i]==elem) output=true;			
		}
		return(output);
		
		
	}
	
	
	
	/**
	 * To wait until the user push the button "enter"
	 */
	public static void attend()
	{
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	/*
	 * Several print methods to debug. 
	 * 
	 */
	public static void print(String str){
		System.out.println(str);
	}
	
	
	public static void print(int[] a) {
		for (int k = 0; k < a.length; k++)
		    System.out.print("  " + a[k]);
		System.out.println();
	    }
	
	
	public static void print(ArrayList <Integer> a) {
		for (int k = 0; k < a.size(); k++)
		    System.out.print("  " + a.get(k));
		System.out.println();
	    }
	
	

	public static void print2(ArrayList <XY> a) {
		for (int k = 0; k < a.size(); k++)
		    System.out.print("(" + a.get(k).x+","+a.get(k).y+")");
		System.out.println();
	    }
	
	
	public static void print(boolean[] a) {
		for (int k = 0; k < a.length; k++)
		    System.out.print(" " + a[k]);
		System.out.println();
	    }
	
	
	public static void print(String[] a) {
		for (int k = 0; k < a.length; k++)
		    System.out.print("  " + a[k]);
		System.out.println();
	    }
	
}
