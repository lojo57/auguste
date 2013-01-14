package proelio_client;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Tout l'affichage du jeu se fait ici.
 * @author vigon
 */
@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener,KeyListener, MouseListener{
	
	public boolean isPlayingLaurel;
	public boolean secondClic=false;
	public List<Hexagon> listHexagon=new LinkedList<Hexagon>();
	public boolean entree;

	public DataGiven dataGiven;
	public Plateau plateau;
	public Connexion connexion;
	public Image imgTab[];
	public String sentence="";	
	public String mouseDepartureWord="";
	public String mouseArrivalWord="";

	/** Container principal : c'est le container le plus haut niveau, il contient tout le reste.*/
	public JPanel container = new JPanel(); 
	
	/** Le plateau est affiché dans ce panneau. */
	public CentralPanel centralPanel;
	
	/** Dans ce panneau, on indique quelle légion à déjà jouée, et les pseudonymes des joueurs*/
	public EastPanel eastPanel;
	
	/** Ce panneau est derrière le panneau central : il contient les images de fond. */
	public BackCentral backCentral;
	
	/** Panneau utilisé pour le chat en jeu. */
	public JPanel jp_messages=new JPanel();
	
	/** Le joueur peut taper des jmessages ici. */
	public JTextField jtf_input= new JTextField();
	
	/** Le joueur receptionnera les messages du serveur et des autres joueurs ici. */
	public JTextArea jta_chat=new JTextArea();
	
	/** Barre de défilement pour le chat en jeu. */
	JScrollPane scrollPane;
	
	/** Bouton pour envoyer les messages. */
	public JButton jb_envoyer=new JButton("Envoyer");

	/** SetOfVertex contenant toutes les cases du plateau. Necessaire a la surbrillance des cases */
	public SetOfVertex cases;
	
	/** Booleen utilise pour empecher l'utilisateur de cliquer lors des animations. */
	public boolean freezeClic=false;
	
	/** Numero de legion du joueur. */
	public int numLegion=-1;
	
	/** Constructeur de la fenetre du jeu */
	public Window(final DataGiven dG,final Plateau plat, final Connexion connec, final Image[] img)
	{
		Runnable code = new Runnable() //Le placement des panneaux/boutons doit etre dans un thread, pour eviter tout blocage et artefacts graphiques
		{
			public void run() 
			{
			    cases=plat.getStrata(); //On recupere les cases du plateau
				plateau=plat;			
				dataGiven=dG;
				connexion=connec;
				imgTab=img;
				SetOfLegion sOL=dataGiven.setOfLegion;
				SetOfVertex sOV=new SetOfVertex();
					
				backCentral= new BackCentral();
				backCentral.setPreferredSize(dataGiven.dimWindow);
				
				centralPanel=new CentralPanel(sOV);			
				centralPanel.setPreferredSize(dataGiven.dimCentralPanel);
		
				container.setLayout(new BorderLayout());
				jp_messages.setLayout(new BorderLayout());
				backCentral.setLayout(new BorderLayout());
				
				jta_chat.setRows(8);
				jta_chat.setEditable(false);
			    jta_chat.setLineWrap(true);
			    
			    JScrollPane scrollPane = new JScrollPane( jta_chat ); 
				
				jp_messages.add(jtf_input);
				container.add(scrollPane,BorderLayout.SOUTH);
				jp_messages.add(jb_envoyer,BorderLayout.EAST);
				
				backCentral.add(jp_messages,BorderLayout.SOUTH);
				backCentral.add(centralPanel,BorderLayout.CENTER);
				
				eastPanel=new EastPanel(sOL);
				eastPanel.setPreferredSize(dataGiven.dimEastPanel);
				
				container.add(eastPanel, BorderLayout.EAST);
				container.add(backCentral,BorderLayout.CENTER);
			}
		};
		
		if (SwingUtilities.isEventDispatchThread()) 
		{
			code.run();
		} 
		else 
		{
			SwingUtilities.invokeLater(code);
		}
		
		// Ajout des listener pour la gestion des evenements
		container.addMouseListener(this);
		jtf_input.addKeyListener(this);
		jb_envoyer.addActionListener(this);
	}

	/** Fonction utilisee pour ecrire dans le jta_chat */
	public void write(String s)
	{
		jta_chat.append(s+"\n");
		jta_chat.setCaretPosition((int) (jta_chat.getDocument().getLength()));
	}
	
	/** Cette fonction recupere le contenu de jtf_input */
	public String send()
	{
		String s=jtf_input.getText().substring(1, jtf_input.getText().length());
		return s;
	}
	
	/** Dessine le centralPanel selon le SetOfVertex passe en parametre. */
	public void drawCentral(final SetOfVertex setOf)
	{
		new Thread(new Runnable() // On thread le repaint(), pour optimiser les performances et eviter un blocage, ou des artefacts.
		{
			public void run() 
			{	
				centralPanel.setOfVertex=setOf;	
				SwingUtilities.invokeLater(new Runnable() 
				{
			          public void run() 
			          {
			        	  centralPanel.repaint(); 
			          }
			    });
		    }
		}).start();	
	}
	
	/** Dessine le eastPanel selon le SetOfVLegion passe en parametre. */
	public void drawEast(final SetOfLegion sOL)
	{
		new Thread(new Runnable() // On thread le repaint(), pour optimiser les performances et eviter un blocage, ou des artefacts.
		{
			public void run() 
			{
				eastPanel.setOfLegion=sOL;
				
				SwingUtilities.invokeLater(new Runnable() 
				{
			          public void run() 
			          {
			            eastPanel.repaint(); 	
			          }
			    });
		    }
		}).start();	
	}

	/** EastPanel est la partie droite du jeu, avec les pseudonymes et l'état des joueurs. */
	public class EastPanel extends JPanel
	{
		public SetOfLegion setOfLegion;		

		public EastPanel(SetOfLegion sOL)
		{		
			setOfLegion=sOL;
		}

		public void paint(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g; // On cast g en Graphics2D, qui nous permettra de dessiner rectangles, polygones...

			g2.setStroke(dataGiven.strokeNormal);

			// Anti-aliasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			int wi=getWidth();
			int diam=wi*1/2;
			int yEsp=wi*4/3;

			g2.setColor(Color.white); 
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setColor(Color.black); 
			g2.drawRect(0, 0, getWidth(), getHeight());

			//Cette boucle est utilisée pour dessiner si les joueurs ont valides leurs coups ou non.
			for (int i =0;i< dataGiven.nbLegion;i++)
			{
				int x=(wi-diam)/2;
				int y=(wi-diam)/2 + i*yEsp;

				g2.setColor(setOfLegion.element(i).theColor); 	// On recupere la couleur du joueur
				if (!setOfLegion.element(i).alreadyPlayed) 		// Si le joueur n'a pas joué
				{			
					if (setOfLegion.element(i).team=="square")  // Et si il fait partie de la team rectangle
					{
						g2.drawRect(x, y, diam, diam);			// On dessine un rectangle vide
					}
					else if (setOfLegion.element(i).team=="circle") // Si il fait partie de la team cercle
					{
						g2.drawOval(x, y, diam, diam);			// On dessine un cercle vide
					}
				}
				else // Si le joueur a joué
				{
					if (setOfLegion.element(i).team=="square") // Et si il fait partie de la team rectangle
					{
						g2.fillRect(x, y, diam, diam); // On dessine un rectangle plein
					}
					else if (setOfLegion.element(i).team=="circle") // Si il fait partie de la team cercle
					{
						g2.fillOval(x, y, diam, diam); // On dessine un cercle plein
					}
				}
				
				if(!setOfLegion.element(i).player.equals("")) // Si le joueur a son pseudonyme defini, et donc qu'il est present
				{
					g2.drawString(setOfLegion.element(i).player, x, y+45); // On affiche son pseudonyme en dessous de son pion
				}
			} // Fin de la boucle
		} // Fin de paint()
	} // Fin de la classe

	/** BackCentral est le panneau qui est en arriere plan, au centre, "sous" centralPanel. Il contient notamment les images de fond. */
	public class BackCentral extends JPanel 
	{
		public BackCentral()
		{		
		}

		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(Color.white); 
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			g2.setColor(Color.black); 
			g2.drawRect(0, 0, getWidth(), getHeight());
			
			Dimension dimNO =new Dimension(getWidth()/2,getHeight()/3);
			Dimension decayNO =new Dimension(getWidth()/15,getHeight()/40);
				
			g2.drawImage(imgTab[4], -decayNO.width, -decayNO.height , dimNO.width  , dimNO.height , this);

			Dimension dimNE =new Dimension(getWidth()/3,getHeight()/2);
			Dimension decayNE =new Dimension(0,getHeight()/40);
			
			g2.drawImage(imgTab[5], getWidth()-dimNE.width + decayNE.width, -decayNE.height , dimNE.width  , dimNE.height , this);

			Dimension dimSO =new Dimension(getWidth()/2,getHeight()/3);
			Dimension decaySO =new Dimension(getWidth()/7,getHeight()/20);
			
			g2.drawImage(imgTab[7], - decaySO.width, getHeight()-dimSO.height  +decaySO.height , dimSO.width  , dimSO.height , this);

			Dimension dimSE =new Dimension(getWidth()/3,getWidth()/3);
			Dimension decaySE =new Dimension(getWidth()/50,getHeight()/50);
			
			g2.drawImage(imgTab[6], getWidth()-dimSE.width +decaySE.width, getHeight()-dimSE.height + decaySE.height , dimSE.width  , dimSE.height , this);

			g2.finalize();
		}
	}

	public class CentralPanel extends JPanel 
	{
		//INPUT
		public SetOfVertex setOfVertex;		
		//private SetOfLegion setOfLegion;
		
		public CentralPanel(SetOfVertex sOV)
		{		
			setOfVertex=sOV;
			//setOfLegion=sOL;
		}
		
		public void paintComponent(Graphics g)
		{
			listHexagon.clear();
			Graphics2D g2 = (Graphics2D) g;

			// anti-aliasing			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g2.setStroke(dataGiven.strokeNormal);

			int xUnit=dataGiven.unit.x;
			int yUnit=dataGiven.unit.y;
			int longSideHexa=dataGiven.longSideHexa;
			int shortSideHexa=dataGiven.shortSideHexa;

			//FIRST COAT (=couches de peinture)
			for (int r=0;r<setOfVertex.size();r++)
			{
				Vertex vertex=setOfVertex.element(r);

				// drawing of the hexagons of the plateau
				drawHexagon(g2,vertex);

				//to draw the legionary 
				if (!dataGiven.emptyPlateau)
				{
					drawLegionary(g2,vertex);
				}
			}// end first coat

			//SECOND COAT
			//Image img3 = Toolkit.getDefaultToolkit().getImage("sesterce.gif");
			for (int r=0;r<setOfVertex.size();r++)
			{
				Vertex vertex=setOfVertex.element(r);
				int x=vertex.pos.x*xUnit/2;
				int y=vertex.pos.y*yUnit;
			
				/**	to write the name of the hexagon and add centralObject*/

				if (vertex.objectCentral=="none")
				{		
					/**Name of pions*/
					Dimension dimString=new Dimension((int) g2.getFontMetrics().getStringBounds(vertex.word, g2).getWidth() , (int)  g2.getFontMetrics().getStringBounds(vertex.word, g2).getHeight());
					XY decay=new XY((int) xUnit/2-dimString.width/2 , (int) dataGiven.longSideHexa/2+dimString.height/2);
							
					g2.setColor(Color.black);	

					g2.setFont(dataGiven.font);
					g2.drawString(vertex.word, x+decay.x, y + decay.y);
				}
				else if (vertex.objectCentral=="laurel")
				{					
					//Image img1 = Toolkit.getDefaultToolkit().getImage("laurel.gif");				
					g2.drawImage(imgTab[0], x+(xUnit-dataGiven.dimLaurel.width)/2, y, dataGiven.dimLaurel.width, dataGiven.dimLaurel.height, this);					
					g2.finalize();

				}
				else if (vertex.objectCentral=="bang")
				{
					//Image img2 = Toolkit.getDefaultToolkit().getImage("bang.gif");	

					g2.drawImage(imgTab[2], x+(xUnit-dataGiven.dimBang.width)/2, y, dataGiven.dimBang.width, dataGiven.dimBang.height, this);					
					g2.finalize();	
				}

				//	to add south object
				if (vertex.objectSouth=="tentChief")
				{
					if (dataGiven.emptyPlateau) g2.setColor(Color.gray);
					else g2.setColor(vertex.objectSouthColor);

					//g2.fillRect(x+xUnit/4,y+dataGiven.longSideHexa,xUnit/2,dataGiven.longSideHexa/4);

					int [] xxx={x+xUnit/3,x+ xUnit/2,x+ xUnit*2/3 , x+ xUnit/2, };
					int [] yyy={y+longSideHexa+shortSideHexa/2,y+yUnit,  y+longSideHexa+shortSideHexa/2,y+longSideHexa  };
					g2.drawPolygon(xxx,yyy,4);
				}
				if (vertex.objectSouth=="tent"&& dataGiven.emptyPlateau)
				{
					g2.setColor(Color.gray);		
					int [] xxx={x+xUnit/3,x+ xUnit*2/3 ,x+ xUnit/2};
					int [] yyy={y+longSideHexa+shortSideHexa/2,y+longSideHexa+shortSideHexa/2,y+yUnit  };
					g2.drawPolygon(xxx,yyy,3);
				}

				// To add north object
				if (vertex.objectNorth=="sesterce")
				{
					g2.setColor(Color.black);

					//g2.fillOval(x+xUnit/2-dataGiven.dimSesterce.width/2  ,y-dataGiven.dimSesterce.height/2,dataGiven.dimSesterce.width,dataGiven.dimSesterce.height);

					g2.drawImage(imgTab[1], x+xUnit/2-dataGiven.dimSesterce.width/2 , y-yUnit/7-dataGiven.dimSesterce.height/2, dataGiven.dimSesterce.width, dataGiven.dimSesterce.height, this);					
					g2.finalize();
				}
			} //end second coat

			// THIRD COAT
			for (int r=0;r<setOfVertex.size();r++)
			{
				//Drawing the displacement of legionary and laurel
				Vertex vertex=setOfVertex.element(r);
				if (vertex.travelTo!=-1)
				{
					drawArrow( g2, vertex);			
				}

				//Drawing tenaille
				if (vertex.tenailleTo.size()>0)
				{					
					drawTenaille( g2, vertex);			
				}

				//Drawing fight
				if (vertex.winFightAgainst.size()>0)
				{					
					drawFight( g2, vertex);			
				}
			}//end third coat
		}//end paint componant 
	}//end central panel

	private void drawHexagon(Graphics2D g2, Vertex vertex )
	{
		g2.setColor(Color.black);
		
		String word=vertex.word;
		boolean hasLaurel=vertex.objectCentral.equals("laurel");
		boolean isSelected=vertex.selected;
		boolean isFree=vertex.free;
		boolean hasArmor=vertex.objectNorth.equals("sesterce");
		boolean isHighlighted=vertex.isHighlighted;
	
		int x=vertex.pos.x*dataGiven.unit.x/2;
		int y=vertex.pos.y*dataGiven.unit.y;

		int xUnit=dataGiven.unit.x;
		int yUnit=dataGiven.unit.y;

		int xx[] =new int[6];
		int yy[] =new int[6];

		xx[0]=x;
		yy[0]=y;

		xx[1]=x+xUnit/2;
		yy[1]= y  -   dataGiven.shortSideHexa;

		xx[2]=x+xUnit;
		yy[2]=y;

		xx[3]=x+xUnit;
		yy[3]=  y+ dataGiven.longSideHexa;

		xx[4]= x+xUnit/2;
		yy[4]=y + yUnit;

		xx[5]=x;
		yy[5]=y+dataGiven.longSideHexa;

		//draw an white filled hexagon  to suppress old things.
		//draw an black hexagon  to draw the contour	
		//g2.fillPolygon(xx,yy  ,6);
		
		Hexagon hexa=new Hexagon(xx,yy,6,word,hasLaurel,isSelected,isFree,hasArmor,isHighlighted);
		
		if(hexa.isSelected==true)
		{
			g2.setColor(dataGiven.setOfLegion.element(numLegion).theBackgroundColor);
		}
		else if(hexa.isHighlighted)
		{
			g2.setColor(dataGiven.setOfLegion.element(numLegion).theBackgroundColor);
		}
		else
		{
			g2.setColor(Color.white);
		}
		g2.fillPolygon(hexa);
		
		listHexagon.add(hexa);
		
		g2.setColor(Color.black);
		g2.drawPolygon(xx, yy, 6);
	}

	public class Hexagon extends Polygon
	{
		public boolean isSelected;
		public Color fillColor;
		public String hexaWord;
		public boolean laurel;
		public boolean free;
		public boolean armor;
		public boolean isHighlighted;
		
		public Hexagon(int[] xpoints, int[] ypoints, int npoints, String word, boolean hasLaurel, boolean isSelected, boolean isFree, boolean hasArmor, boolean highlighted)
		{
			super(xpoints,ypoints,npoints);
			this.isSelected=isSelected;
			this.laurel=hasLaurel;
			this.hexaWord=word;
			this.free=isFree;
			this.armor=hasArmor;
			this.isHighlighted=highlighted;
		}
	}

	private void drawLegionary(Graphics2D g2,Vertex vertex)
	{	
		g2.setColor(vertex.legion.theColor);

		int x=vertex.pos.x*dataGiven.unit.x/2;
		int y=vertex.pos.y*dataGiven.unit.y;

		XY center=new XY(x+dataGiven.unit.x/2,y+dataGiven.longSideHexa/2);

		if (vertex.killed)
		{
			g2.setStroke(dataGiven.strokeDash);
		}

		if (vertex.legion.team=="circle")
		{
			g2.drawOval(center.x-dataGiven.dimCircle.width/2, center.y-dataGiven.dimCircle.height/2 , dataGiven.dimCircle.width, dataGiven.dimCircle.height);

			if (vertex.armor)
			{
				g2.drawOval(center.x-dataGiven.dimCircleArmor.width/2, center.y-dataGiven.dimCircleArmor.height/2 , dataGiven.dimCircleArmor.width, dataGiven.dimCircleArmor.height);	
			}
		}
		else if (vertex.legion.team=="square")
		{
			g2.drawRect(center.x-dataGiven.dimSquare.width/2, center.y-dataGiven.dimSquare.height/2 , dataGiven.dimSquare.width, dataGiven.dimSquare.height);

			if (vertex.armor)
			{
				g2.drawRect(center.x-dataGiven.dimSquareArmor.width/2, center.y-dataGiven.dimSquareArmor.height/2 , dataGiven.dimSquareArmor.width, dataGiven.dimSquareArmor.height);
			}
		}
		g2.setStroke(dataGiven.strokeNormal);
	}

	private void drawArrow(Graphics2D g2, Vertex vertex )
	{
		int rDep=vertex.rank;
		int rArr=vertex.travelTo;

		g2.setColor(vertex.legion.theColor);

		XY xyDep = plateau.rank2Pos(rDep);
		XY xyArr = plateau.rank2Pos(rArr);

		int xUnit=dataGiven.unit.x;
		int yUnit=dataGiven.unit.y;

		int x1=xyDep.x *xUnit/2 +xUnit/2;
		int y1=xyDep.y *yUnit +dataGiven.longSideHexa/2;
		int x2=xyArr.x*xUnit/2+xUnit/2;
		int y2=xyArr.y*yUnit+dataGiven.longSideHexa/2;
		int a=x2-x1;
		int b=y2-y1;
		double sqrt= Math.sqrt( a*a + b*b);

		int l=dataGiven.dimArrow.height;
		int h=dataGiven.dimArrow.width;

		g2.drawLine(x1 , y1 ,   x2   ,y2 );

		double x3= x1+  a/sqrt*(sqrt-l) -b/sqrt*h;
		double y3=y1+b/sqrt*(sqrt-l)+ a/sqrt*h;

		double x4= x1+  a/sqrt*(sqrt-l) +b/sqrt*h;
		double y4=y1+b/sqrt*(sqrt-l)- a/sqrt*h;

		int [] xx=new int[3];
		int [] yy=new int[3];

		xx[0]=(int) x2;
		xx[1]=(int) x3;
		xx[2]=(int) x4;

		yy[0]=(int) y2;
		yy[1]=(int) y3;
		yy[2]=(int) y4;

		g2.fillPolygon(xx,yy,3);
	}

	private void drawTenaille(Graphics2D g2, Vertex vertex )
	{
		int rDep=vertex.rank;

		for (int i=0;i<vertex.tenailleTo.size();i++)
		{
			int rArr=vertex.tenailleTo.get(i);

			g2.setColor(Color.gray);

			XY xyDep = plateau.rank2Pos(rDep);
			XY xyArr = plateau.rank2Pos(rArr);

			int xUnit=dataGiven.unit.x;
			int yUnit=dataGiven.unit.y;

			int x1=xyDep.x *xUnit/2 +xUnit/2;
			int y1=xyDep.y *yUnit +dataGiven.longSideHexa/2;
			int x2=xyArr.x*xUnit/2+xUnit/2;
			int y2=xyArr.y*yUnit+dataGiven.longSideHexa/2;
			int a=x2-x1;
			int b=y2-y1;
			double sqrt= Math.sqrt( a*a + b*b);
			//int l= dataGiven.dimArrow.height;
			int h=dataGiven.dimArrow.width;

			g2.drawLine(x1 , y1 ,   x2   ,y2 );

			double x3= x1+  a/sqrt*(sqrt) -b/sqrt*h;
			double y3=y1+b/sqrt*(sqrt)+ a/sqrt*h;

			double x4= x1+  a/sqrt*(sqrt) +b/sqrt*h;
			double y4=y1+b/sqrt*(sqrt)- a/sqrt*h;

			int [] xx=new int[3];
			int [] yy=new int[3];

			xx[0]=(int) x2;
			xx[1]=(int) x3;
			xx[2]=(int) x4;

			yy[0]=(int) y2;
			yy[1]=(int) y3;
			yy[2]=(int) y4;

			g2.drawLine((int) x3,(int) y3,(int) x4,(int) y4);

			g2.fillOval(x1-dataGiven.dimCircleTenaille.width/2, y1-dataGiven.dimCircleTenaille.height/2 , dataGiven.dimCircleTenaille.width, dataGiven.dimCircleTenaille.height);
			g2.fillOval(x2-dataGiven.dimCircleTenaille.width/2, y2-dataGiven.dimCircleTenaille.height/2 , dataGiven.dimCircleTenaille.width, dataGiven.dimCircleTenaille.height);
		}
	}

	private void drawFight(Graphics2D g2, Vertex vertex )
	{

		int rDep=vertex.rank;

		for (int i=0;i<vertex.winFightAgainst.size();i++)
		{
			int rArr=vertex.winFightAgainst.get(i);
			
			g2.setColor(vertex.legion.theColor);

			XY xyDep = plateau.rank2Pos(rDep);
			XY xyArr = plateau.rank2Pos(rArr);

			int xUnit=dataGiven.unit.x;
			int yUnit=dataGiven.unit.y;

			int x1=xyDep.x *xUnit/2 +xUnit/2;
			int y1=xyDep.y *yUnit +dataGiven.longSideHexa/2;
			int x2=xyArr.x*xUnit/2+xUnit/2;
			int y2=xyArr.y*yUnit+dataGiven.longSideHexa/2;
			int a=x2-x1;
			int b=y2-y1;
			double sqrt= Math.sqrt( a*a + b*b);

			int l=dataGiven.dimDague.width;
			int h=dataGiven.dimDague.height;

			g2.drawLine(x1 , y1 ,   x2   ,y2 );

			double x3= x1+  a/sqrt*(sqrt-l) -b/sqrt*h;
			double y3=y1+b/sqrt*(sqrt-l)+ a/sqrt*h;

			double x4= x1+  a/sqrt*(sqrt-l) +b/sqrt*h;
			double y4=y1+b/sqrt*(sqrt-l)- a/sqrt*h;

			int [] xx=new int[3];
			int [] yy=new int[3];

			xx[0]=(int) x2;
			xx[1]=(int) x3;
			xx[2]=(int) x4;

			yy[0]=(int) y2;
			yy[1]=(int) y3;
			yy[2]=(int) y4;

			g2.fillPolygon(xx,yy,3);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if (jtf_input.getText().substring(0, 1).equals("/"))
			{
				sentence=this.send();
			}
			else
			{
				connexion.outMsg.println(jtf_input.getText());
				connexion.outMsg.flush();
			}
			jtf_input.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(jb_envoyer))
		{
			if (jtf_input.getText().substring(0, 1).equals("/"))
			{
				sentence=this.send();
			}
			else
			{
				connexion.outMsg.println(jtf_input.getText());
				connexion.outMsg.flush();
			}
			jtf_input.setText("");
		}
	}

	int rankSelected;
	int rankHighlighted[];
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(freezeClic==false)
		{
			for(int i=0;i<listHexagon.size();i++)
			{
				if(listHexagon.get(i).contains(e.getPoint()))
				{
					if(secondClic==false)
					{
						if(listHexagon.get(i).free==false)
						{
							if(listHexagon.get(i).laurel==true)
							{
								sentence="laurel";
								secondClic=true;
								rankSelected=plateau.word2Rank(listHexagon.get(i).hexaWord);
								rankHighlighted=plateau.neighbor(rankSelected);
								
								container.repaint();
								
							}
							else if(listHexagon.get(i).armor==false)
							{
								rankSelected=plateau.word2Rank(listHexagon.get(i).hexaWord);
								String name=plateau.situation.element(rankSelected).legion.name;
								int k=dataGiven.setOfLegion.findLegionOfName(name);
								String kk=k+"";
								if(kk.equals(numLegion+""))
								{
									mouseDepartureWord=listHexagon.get(i).hexaWord;
									secondClic=true;
									
									cases.element(rankSelected).selected=true;
									listHexagon.get(i).isSelected=true;
									
									rankHighlighted=edgeGroup(rankSelected,plateau.situation);
									if(plateau.situation.element(rankSelected).armor==true)
									{
										for(int j=0;j<rankHighlighted.length;j++)
										{
											if(!plateau.situation.element(rankHighlighted[j]).objectNorth.equals("sesterce"))
											{
												cases.element(rankHighlighted[j]).isHighlighted=true;
											}
										}
									}
									else
									{
										for(int j=0;j<rankHighlighted.length;j++)
										{
											cases.element(rankHighlighted[j]).isHighlighted=true;
										}
									}
									container.repaint();
								}
							}
						}
					}
					else
					{
						if(isPlayingLaurel==true)
						{
							if(listHexagon.get(i).laurel==true)
							{
								sentence="NOWHERE";
							}
							else
							{
								sentence=listHexagon.get(i).hexaWord;
							}
						}
						else if(listHexagon.get(i).free==true)
						{
							mouseArrivalWord=listHexagon.get(i).hexaWord;
							sentence=mouseDepartureWord+" "+mouseArrivalWord;
							secondClic=false;
							cases.element(rankSelected).selected=false;
							for(int j=0;j<rankHighlighted.length;j++)
							{
								cases.element(rankHighlighted[j]).isHighlighted=false;
							}
							container.repaint();	
						}
						else if(listHexagon.get(i).hexaWord==mouseDepartureWord)
						{
							mouseDepartureWord="";
							secondClic=false;
							cases.element(rankSelected).selected=false;
							for(int j=0;j<rankHighlighted.length;j++)
							{
								cases.element(rankHighlighted[j]).isHighlighted=false;
							}
							container.repaint();
						}
					}	
				}
			}
		}
	}
	
	public void drawLaurelPossibilities()
	{
		rankHighlighted=plateau.neighbor(rankSelected);
		for(int j=0;j<rankHighlighted.length;j++)
		{
			if(!plateau.situation.element(rankHighlighted[j]).objectNorth.equals("sesterce") && plateau.situation.element(rankHighlighted[j]).free)
			{
				cases.element(rankHighlighted[j]).isHighlighted=true;
			}
		}
	}
	
	public void eraseLaurelPossibilities()
	{
		for(int j=0;j<rankHighlighted.length;j++)
		{
			cases.element(rankHighlighted[j]).isHighlighted=false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0){}

	@Override
	public void mouseExited(MouseEvent arg0){}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

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

				for (int elem:neighbor)
				{					
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
}// end of the class