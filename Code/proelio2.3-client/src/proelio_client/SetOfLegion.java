package proelio_client;




/**
 * It is a set of Legion. It is implemented by an array of legion (similarly to setOfVertex). 
 * @author vigon
 *
 */
public class SetOfLegion {

	private  Legion [] content;
	private int size;


	public SetOfLegion(int r){

		size=r;
		content = new Legion [r];
		for (int i=0;i<r;i++){			
			content[i]= new Legion();
		}


	}

	
	

	public int size(){

		return (size);
	}


	
	public Legion element(int r){		
		return(content[r]);		
	}
	

	
	
	
	public void setElement(int r,Legion leg){		
		content[r]=leg;		
	}

	
	
	
	
	public int findLegionOfName(String legName){		

		int i=0;
				
			while (i<size &&  content[i].name!=legName   ){
				i++;				
			}			
					
		
		if (i<size) return(i);
		else return(-1);
		
		
	}
	
	
	
	
	public void cleanAction(){
		
		for (int i=0;i<size;i++){
			content[i].arrival=-1;
			content[i].departure=-1;
			//content[i].havePlayedLaurel=false;
			content[i].alreadyPlayed=false;
			content[i].typeAction="none";
			
		}
		
		
	}






}
