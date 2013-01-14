package proelio_client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.*;

@SuppressWarnings("serial")
public class MainGame extends JApplet
{
	public void init()  
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Thread thread = new Thread() {
        @SuppressWarnings("unused")
		public void run() 
        {
        	setSize(850,800);
			DataGiven dataGiven=new DataGiven();
			final Connexion connexion=new Connexion();
			connexion.lancementLobby();
			new Thread(new Runnable() 
			{
				public void run() 
				{
           
					SwingUtilities.invokeLater(new Runnable() 
					{
						public void run() 
						{
							add(connexion.jp);
							getContentPane().revalidate();
							connexion.jtf_pseudo.requestFocusInWindow();
						}
					});
				}
			}).start();	
			while(connexion.pseudonyme.equals(""))
			{
				try 
				{
					sleep(500);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			String pseudonyme=connexion.pseudonyme;
			getContentPane().removeAll();
			final Lobby lobby=new Lobby(connexion,pseudonyme);
			
			new Thread(new Runnable() 
			{
				public void run() 
				{
           
					SwingUtilities.invokeLater(new Runnable() 
					{
						public void run() 
						{
							add(lobby.lobby);
							getContentPane().revalidate();
						}
					});
				}
			}).start();	
			
			while(lobby.lancementPartie==false)
			{
				try 
				{
					sleep(500);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			
			getContentPane().removeAll();

			// Indication des différents paramètres du jeu
			dataGiven.nbLegion=Integer.parseInt(lobby.nbJoueursFinal);
			dataGiven.baseHexa=Integer.parseInt(lobby.taillePlateauFinal);
			dataGiven.dimWindow=new Dimension(1000, 708);
			dataGiven.dimCentralPanel=new Dimension(750, 665);
	
			dataGiven.emptyPlateau=false;
			dataGiven.eastPanelVisible=true;
			dataGiven.southPanelVisible=true;
			dataGiven.font=new Font("Helvetica", Font.PLAIN, 14);
			
			dataGiven.computeUnit(dataGiven.dimWindow, dataGiven.baseHexa );		
						
			dataGiven.computeData(dataGiven.unit, dataGiven.baseHexa,dataGiven.nbLegion, dataGiven.nbTeam);
	
			Plateau plateau = new Plateau(dataGiven);				
			SetOfVertex initialSituation=plateau.initialSituation();
			
			Image img[]=new Image[8];
			
			img[0]=getImage(getCodeBase(),"laurel.gif");
			img[1]=getImage(getCodeBase(),"sesterce.gif");
			img[2]=getImage(getCodeBase(),"bang.gif");
			img[3]=getImage(getCodeBase(),"dague.gif");
			img[4]=getImage(getCodeBase(),"gard.jpg");
			img[5]=getImage(getCodeBase(),"cesar.jpg");
			img[6]=getImage(getCodeBase(),"rome.jpg");
			img[7]=getImage(getCodeBase(),"alexandre.jpg");
			
			final Window window = new Window(dataGiven,plateau,connexion,img);
			new Thread(new Runnable() 
			{
				public void run() 
				{
           
					SwingUtilities.invokeLater(new Runnable() 
					{
						public void run() 
						{
							add(window.container);  
							getContentPane().revalidate();
						}
					});
				}
			}).start();	
			try {
				sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//window.drawCentral(initialSituation);
			connexion.creerReceptionMsg(window);
			String listePseudo[]=new String[dataGiven.nbLegion];
			String split=";";
			Pattern pattern=Pattern.compile(split);
			boolean joueursPresents=false;
			while(!joueursPresents)
			{
				joueursPresents=true;
				String res="";
				try
				{
					res = connexion.in.readLine();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				listePseudo=pattern.split(res);
				for(int i=0;i<listePseudo.length;i++)
				{
					if(!listePseudo[i].equals(" "))
					{
						dataGiven.setOfLegion.element(i).player=listePseudo[i];
					}
					else
					{
						joueursPresents=false;
					}
				}
				window.drawEast(dataGiven.setOfLegion);
			}
			String numLegion="";
			for(int i=0;i<listePseudo.length;i++)
			{
				if(listePseudo[i].equals(pseudonyme))
				{
					numLegion=i+"";
				}
			}
			Game game=new Game(dataGiven,plateau,window,initialSituation,connexion,numLegion);
			
        }};
        thread.start();
	}
}
