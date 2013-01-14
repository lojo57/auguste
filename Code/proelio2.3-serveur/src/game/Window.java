package game;


//import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * All the display takes place here. 
 * @author vigon
 *
 */
@SuppressWarnings("serial")
public class Window extends JFrame{


	// INPUT
	private DataGiven dataGiven;
	private Plateau plateau;


	//OUTPUT
	/**
	 * Where the plateau is printed
	 */
	private CentralPanel centralPanel;
	/**
	 * For some message
	 */
	private SouthPanel southPanel;
	/**
	 * To indicate which legion had already played
	 */
	private EastPanel eastPanel;
	
	/**
	 * The image back to the central panel is in this panel
	 */
	private BackCentral backCentral;
	
	private JPanel container = new JPanel(); 


	
	


	public Window(DataGiven dG,Plateau plat){


		plateau=plat;
		dataGiven=dG;


		setTitle("Proelio");
		setSize(dataGiven.dimWindow);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(0,0);


		SetOfLegion sOL=dataGiven.setOfLegion;
		SetOfVertex sOV=new SetOfVertex();

			
		backCentral= new BackCentral();
		backCentral.setPreferredSize(dataGiven.dimWindow);
		

		centralPanel=new CentralPanel(sOV);			
		centralPanel.setPreferredSize(dataGiven.dimCentralPanel);


		container.setLayout(new BorderLayout());
		backCentral.add(centralPanel);
		container.add(backCentral,BorderLayout.CENTER);
		

		
		if (dataGiven.southPanelVisible){
			southPanel=new SouthPanel("SERVEUR");
			southPanel.setPreferredSize(dataGiven.dimSouthPanel);	
			container.add(southPanel, BorderLayout.SOUTH);
		}
		
		if (dataGiven.eastPanelVisible){
			eastPanel=new EastPanel(sOL);
			eastPanel.setPreferredSize(dataGiven.dimEastPanel);
			container.add(eastPanel, BorderLayout.EAST);
		}

		




		
		
		
		setContentPane(container);
		setVisible(true);
		
		
		
		
		


	}





	
	
	


	public void drawSouth(String messa){
		southPanel.message=messa;		
		southPanel.repaint(); 	
	}



	public void drawCentral(SetOfVertex setOf){
		centralPanel.setOfVertex=setOf;
		//centralPanel.setOfLegion=sOL;
		centralPanel.repaint(); 	

	}
	
	



	public void drawEast(SetOfLegion sOL){
		eastPanel.setOfLegion=sOL;
		eastPanel.repaint(); 	
	}





	private class EastPanel extends JPanel {

		//INPUT
		private SetOfLegion setOfLegion;		


		public EastPanel(SetOfLegion sOL){		
			setOfLegion=sOL;
		}



		// Etrange: La procedure ci-dessous se lance automatique a chaque fois qu'on appelle pan.repaint()
		public void paintComponent(Graphics g){

			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(dataGiven.strokeNormal);

			// interpolation of color:
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);





			int wi=getWidth();
			int diam=wi*1/2;
			int yEsp=wi*4/3;



			g2.setColor(Color.white); 
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setColor(Color.black); 
			g2.drawRect(0, 0, getWidth(), getHeight());


			for (int i =0;i< dataGiven.nbLegion;i++){

				int x=(wi-diam)/2;
				int y=(wi-diam)/2 + i*yEsp;

				g2.setColor(setOfLegion.element(i).theColor);

				if (!setOfLegion.element(i).alreadyPlayed){

					if (setOfLegion.element(i).team=="square"){
						g2.drawRect(x, y, diam, diam);
					}
					else if (setOfLegion.element(i).team=="circle"){
						g2.drawOval(x, y, diam, diam);
					}

				}
				else{
					if (setOfLegion.element(i).team=="square"){
						g2.fillRect(x, y, diam, diam);
					}
					else if (setOfLegion.element(i).team=="circle"){
						g2.fillOval(x, y, diam, diam);
					}
				}




			}



		}
	}










	private class SouthPanel extends JPanel {

		//INPUT
		private String message;		



		public SouthPanel(String messa){		
			message=messa;
		}



		// Etrange: La procedure ci-dessous se lance automatique a chaque fois qu'on appelle pan.repaint()
		public void paintComponent(Graphics g){

			Graphics2D g2 = (Graphics2D) g;


			g2.setStroke(new BasicStroke(3.0f,                     // Line width
					BasicStroke.CAP_ROUND,    // End-cap style
					BasicStroke.JOIN_ROUND)); // Vertex join style
			// interpolation of color:
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


			g2.setColor(Color.white); 
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setColor(Color.black); 
			g2.drawRect(0, 0, getWidth(), getHeight());


			g2.setColor(Color.red);
			Font font = new Font("Courier", Font.BOLD, 20);
			g2.setFont(font);

			g2.setColor(Color.red);          
			g2.drawString(message, 10, 20);




		}
	}







	private class BackCentral extends JPanel {



		public BackCentral(){		

		}



		public void paintComponent(Graphics g){

			Graphics2D g2 = (Graphics2D) g;




			g2.setColor(Color.white); 
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			g2.setColor(Color.black); 
			g2.drawRect(0, 0, getWidth(), getHeight());
			

			Dimension dimNO =new Dimension(getWidth()/2,getHeight()/3);
			Dimension decayNO =new Dimension(getWidth()/15,getHeight()/40);

			Image imgNO = Toolkit.getDefaultToolkit().getImage("gard.jpg");				
			g2.drawImage(imgNO, -decayNO.width, -decayNO.height , dimNO.width  , dimNO.height , this);


			Dimension dimNE =new Dimension(getWidth()/3,getHeight()/2);
			Dimension decayNE =new Dimension(0,getHeight()/40);

			Image imgNE = Toolkit.getDefaultToolkit().getImage("cesar.jpg");				
			g2.drawImage(imgNE, getWidth()-dimNE.width + decayNE.width, -decayNE.height , dimNE.width  , dimNE.height , this);


			Dimension dimSO =new Dimension(getWidth()/2,getHeight()/3);
			Dimension decaySO =new Dimension(getWidth()/7,getHeight()/20);

			Image imgSO = Toolkit.getDefaultToolkit().getImage("alexandre.jpg");				
			g2.drawImage(imgSO, - decaySO.width, getHeight()-dimSO.height  +decaySO.height , dimSO.width  , dimSO.height , this);




			Dimension dimSE =new Dimension(getWidth()/3,getWidth()/3);
			Dimension decaySE =new Dimension(getWidth()/50,getHeight()/50);

			Image imgSE = Toolkit.getDefaultToolkit().getImage("rome.jpg");				
			g2.drawImage(imgSE, getWidth()-dimSE.width +decaySE.width, getHeight()-dimSE.height + decaySE.height , dimSE.width  , dimSE.height , this);

			g2.finalize();


		}
	}






	private class CentralPanel extends JPanel {

		//INPUT
		private SetOfVertex setOfVertex;		
		//private SetOfLegion setOfLegion;



		public CentralPanel(SetOfVertex sOV){		
			setOfVertex=sOV;
			//setOfLegion=sOL;
		}



		// Etrange: La procedure ci-dessous se lance automatique a chaque fois qu'on appelle pan.repaint()
		public void paintComponent(Graphics g){


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
			for (int r=0;r<setOfVertex.size();r++){

				Vertex vertex=setOfVertex.element(r);

				// drawing of the hexagons of the plateau
				drawHexagon(g2,vertex);


				//to draw the legionary 
				if (!dataGiven.emptyPlateau){
					drawLegionary(g2,vertex);
				}


			}// end first coat



			//SECOND COAT
			Image img3 = Toolkit.getDefaultToolkit().getImage("sesterce.gif");
			for (int r=0;r<setOfVertex.size();r++){

				Vertex vertex=setOfVertex.element(r);
				int x=vertex.pos.x*xUnit/2;
				int y=vertex.pos.y*yUnit;
				
				
				/**	to write the name of the hexagon and add centralObject*/

				if (vertex.objectCentral=="none"){
					
					/**Name of pions*/
					Dimension dimString=new Dimension((int) g2.getFontMetrics().getStringBounds(vertex.word, g2).getWidth() , (int)  g2.getFontMetrics().getStringBounds(vertex.word, g2).getHeight());
					XY decay=new XY((int) xUnit/2-dimString.width/2 , (int) dataGiven.longSideHexa/2+dimString.height/2);
					
								
					
					g2.setColor(Color.black);	

					g2.setFont(dataGiven.font);
					g2.drawString(vertex.word, x+decay.x, y + decay.y);
				}
				else if (vertex.objectCentral=="laurel"){					
					Image img1 = Toolkit.getDefaultToolkit().getImage("laurel.gif");				
					g2.drawImage(img1, x+(xUnit-dataGiven.dimLaurel.width)/2, y, dataGiven.dimLaurel.width, dataGiven.dimLaurel.height, this);					
					g2.finalize();				
				}
				else if (vertex.objectCentral=="bang"){
					Image img2 = Toolkit.getDefaultToolkit().getImage("bang.gif");	

					g2.drawImage(img2, x+(xUnit-dataGiven.dimBang.width)/2, y, dataGiven.dimBang.width, dataGiven.dimBang.height, this);					
					g2.finalize();			

				}


				//	to add south object
				if (vertex.objectSouth=="tentChief")	{

					if (dataGiven.emptyPlateau) g2.setColor(Color.gray);
					else g2.setColor(vertex.objectSouthColor);

					//g2.fillRect(x+xUnit/4,y+dataGiven.longSideHexa,xUnit/2,dataGiven.longSideHexa/4);

					int [] xxx={x+xUnit/3,x+ xUnit/2,x+ xUnit*2/3 , x+ xUnit/2, };
					int [] yyy={y+longSideHexa+shortSideHexa/2,y+yUnit,  y+longSideHexa+shortSideHexa/2,y+longSideHexa  };
					g2.drawPolygon(xxx,yyy,4);

				}


				if (vertex.objectSouth=="tent"&& dataGiven.emptyPlateau)	{

					g2.setColor(Color.gray);		
					int [] xxx={x+xUnit/3,x+ xUnit*2/3 ,x+ xUnit/2};
					int [] yyy={y+longSideHexa+shortSideHexa/2,y+longSideHexa+shortSideHexa/2,y+yUnit  };
					g2.drawPolygon(xxx,yyy,3);

				}


				// To add north object
				if (vertex.objectNorth=="sesterce"){
					g2.setColor(Color.black);

					//g2.fillOval(x+xUnit/2-dimSesterce.width/2  ,y-dimSesterce.height/2,dimSesterce.width,dimSesterce.height);




					g2.drawImage(img3, x+xUnit/2-dataGiven.dimSesterce.width/2 , y-yUnit/7-dataGiven.dimSesterce.height/2, dataGiven.dimSesterce.width, dataGiven.dimSesterce.height, this);					
					g2.finalize();

				}
			} //end second coat




			// THIRD COAT
			for (int r=0;r<setOfVertex.size();r++){

				//Drawing the displacement of legionary and laurel
				Vertex vertex=setOfVertex.element(r);
				if (vertex.travelTo!=-1){

					drawArrow( g2, vertex);			
				}

				//Drawing tenaille
				if (vertex.tenailleTo.size()>0){					
					drawTenaille( g2, vertex);			
				}

				//Drawing fight
				if (vertex.winFightAgainst.size()>0){					
					drawFight( g2, vertex);			
				}
			}//end third coat



		}//end paint componant 



	}//end central panel


//
//
//	public void chooseColor(Graphics2D g2,String theColor){
//
//		if (theColor=="red")	{
//			g2.setColor(Color.red); 
//		}
//		else if (theColor=="blue"){
//			g2.setColor(Color.blue); 
//		}
//		else if (theColor=="green"){
//			g2.setColor(Color.green); 
//		}
//		else if (theColor=="orange"){
//			g2.setColor(Color.orange); 
//		}
//		else if (theColor=="brown"){
//			g2.setColor(new Color(156, 93, 82)); 
//		}
//		else if (theColor=="yellow"){
//			g2.setColor(Color.yellow.brighter()); 
//		}
//	}






	private void drawHexagon(Graphics2D g2, Vertex vertex ){

		g2.setColor(Color.black);

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
		g2.setColor(Color.white);
		g2.fillPolygon(xx,yy  ,6);
		g2.setColor(Color.black);
		g2.drawPolygon(xx, yy, 6);

	}




	private void drawLegionary(Graphics2D g2,Vertex vertex){

		
		g2.setColor(vertex.legion.theColor);

		int x=vertex.pos.x*dataGiven.unit.x/2;
		int y=vertex.pos.y*dataGiven.unit.y;



		XY center=new XY(x+dataGiven.unit.x/2,y+dataGiven.longSideHexa/2);


		if (vertex.killed) {
			g2.setStroke(dataGiven.strokeDash);
		}


		if (vertex.legion.team=="circle"){

			g2.drawOval(center.x-dataGiven.dimCircle.width/2, center.y-dataGiven.dimCircle.height/2 , dataGiven.dimCircle.width, dataGiven.dimCircle.height);

			if (vertex.armor){
				g2.drawOval(center.x-dataGiven.dimCircleArmor.width/2, center.y-dataGiven.dimCircleArmor.height/2 , dataGiven.dimCircleArmor.width, dataGiven.dimCircleArmor.height);	
			}
		}
		else if (vertex.legion.team=="square"){
			g2.drawRect(center.x-dataGiven.dimSquare.width/2, center.y-dataGiven.dimSquare.height/2 , dataGiven.dimSquare.width, dataGiven.dimSquare.height);


			if (vertex.armor){
				g2.drawRect(center.x-dataGiven.dimSquareArmor.width/2, center.y-dataGiven.dimSquareArmor.height/2 , dataGiven.dimSquareArmor.width, dataGiven.dimSquareArmor.height);

			}
		}

		g2.setStroke(dataGiven.strokeNormal);



	}







	private void drawArrow(Graphics2D g2, Vertex vertex ){


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




	private void drawTenaille(Graphics2D g2, Vertex vertex ){



		int rDep=vertex.rank;

		for (int i=0;i<vertex.tenailleTo.size();i++){


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
			//			int l= dataGiven.dimArrow.height;
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



	private void drawFight(Graphics2D g2, Vertex vertex ){



		int rDep=vertex.rank;

		for (int i=0;i<vertex.winFightAgainst.size();i++){


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

	
	
	

//
//
///**
// * This method transform our central panel into an impage png (better quality )  of jpg (worse quality). 
// * @param name  name of the output file
// * @param type  png of jpg
// */
//
//	public void all2Image(String name,String type ) {
//
//		BufferedImage outImage=new BufferedImage(backCentral.getWidth(),backCentral.getHeight(),BufferedImage.TYPE_INT_RGB);
//
//
//		Graphics2D graphics=outImage.createGraphics();
//		container.paint(graphics);
//		File outFile=new File( name+"."+type );
//		try {
//			if (!ImageIO.write(outImage,type,outFile))
//				System.out.println("Format d'Žcriture non pris en charge" );
//		} catch (Exception e) {
//			System.out.println("erreur dans l'enregistrement de l'image :" );
//			e.printStackTrace();
//		}
//	}
//
//
//	
//	
//	public void all2pdf() {
//		
//		JPanel panel    = backCentral;
////
////		//just to ensure that the panel has content...
////		JLabel label    = new JLabel("i am a label0");
////		panel.add(label);
////		panel.setSize(100,600);
////		//so that even if the label doesn't get added... 
////		//i can see that the panel does
////		panel.setBackground(Color.red);
////		
////
////		//the frame containing the panel
////		JFrame f = new JFrame();
////		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////
////		f.add(panel);
////		f.setVisible(true);
////		f.setSize(100,600);
//
//		//print the panel to pdf
//		Document document = new Document();
//		try {
//		    //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\temp\\test.pdf"));
//			document.setPageSize(PageSize.A3);//???
//		    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("toto.pdf"));
//
//		    document.open();
//		    PdfContentByte contentByte = writer.getDirectContent();
//		    PdfTemplate template = contentByte.createTemplate(500, 500);
//		    Graphics2D g2 = template.createGraphics(500, 500);
//		    panel.print(g2);
//		    g2.dispose();
//		    //contentByte.addTemplate(template, 30, 300);
//		    contentByte.addTemplate(template, 0, 690);
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//		finally{
//		    if(document.isOpen()){
//		        document.close();
//		    }
//		}
//
//	}
//	
//	
//	
//	
	
	
	




}// end of the class

