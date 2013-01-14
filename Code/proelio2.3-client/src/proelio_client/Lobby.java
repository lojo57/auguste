package proelio_client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Lobby implements ActionListener
{
	public boolean lancementPartie=false;
	
	String nbJoueursFinal;
	String taillePlateauFinal;
	String numLegionFinal;
	
	Connexion connexion;
	
	JPanel lobby;
	JPanel recherchePartie;
	
	JPanel explorateur;
	JPanel explorateurBarre;
	JButton jb_rejoindre;
	JButton jb_creer;
	JButton jb_rafraichir;

	JTextArea listeJoueurs;
	
	JPanel chat;
	JPanel chatBarre;
	JTextArea chatMessage;
	JTextField chatInput;
	JButton chatButton;
	
	JScrollPane jsp;
	
	Vector<String> idColonnes = new Vector<String>();
	
	DefaultTableModel dtm = new DefaultTableModelNonEditable();
	JTable listeParties=new JTable();
	
	JPanel creationPartie;
	JTextField jtf_nomPartie;
	JComboBox<String> nbJoueurs;
	JComboBox<String> taillePlateau;
	
	JButton creerPartie;
	JButton annuler;
	
	String pseudonyme;
	
	@SuppressWarnings("serial")
	public class DefaultTableModelNonEditable extends DefaultTableModel
	{
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
	
	public Lobby(Connexion connec, String pseudo)
	{
		idColonnes.add("Nom de la partie");
		idColonnes.add("Nombre de joueurs");
		idColonnes.add("Nom du créateur");
		
		lobby=new JPanel();
		pseudonyme=pseudo;
		connexion=connec;
		dessinerLobby();
	}
	
	public void dessinerLobby()
	{
		lobby.setLayout(new BorderLayout());

		explorateur=new JPanel();
		explorateur.setLayout(new BorderLayout());
		
		explorateurBarre=new JPanel();
		explorateurBarre.setLayout(new BorderLayout());
		
		jb_rejoindre=new JButton("Rejoindre");
		jb_creer=new JButton("Creer");
		jb_rafraichir=new JButton("Rafraichir");
		
//		listeJoueurs=new JTextArea();
//		listeJoueurs.setRows(6);
//		listeJoueurs.setEditable(false);
		
		chat=new JPanel();
		chat.setLayout(new BorderLayout());
		chatBarre=new JPanel();
		chatBarre.setLayout(new BorderLayout());
		
		chatMessage=new JTextArea();
		chatMessage.setEditable(false);
		chatMessage.setRows(6);
		
		chatInput=new JTextField();
		chatButton=new JButton("Envoyer");
		
		explorateurBarre.add(jb_rejoindre,BorderLayout.WEST);
		explorateurBarre.add(jb_rafraichir,BorderLayout.CENTER);
		explorateurBarre.add(jb_creer,BorderLayout.EAST);
		
		
		jsp=new JScrollPane(listeParties);
//		listeParties.setSize(500,300);
		
		rafraichirListe();

		explorateur.add(jsp,BorderLayout.NORTH);
		explorateur.add(explorateurBarre,BorderLayout.WEST);
		
		chat.add(chatMessage,BorderLayout.NORTH);
		chatBarre.add(chatInput,BorderLayout.CENTER);
		chatBarre.add(chatButton,BorderLayout.EAST);
		chat.add(chatBarre,BorderLayout.SOUTH);
		
//		lobby.add(listeJoueurs,BorderLayout.EAST);
		lobby.add(chat,BorderLayout.SOUTH);
		lobby.add(explorateur,BorderLayout.NORTH);
		
		jb_rejoindre.addActionListener(this);
		jb_creer.addActionListener(this);
		jb_rafraichir.addActionListener(this);
	}

	public void dessinerCreationPartie()
	{
		lobby.removeAll();
		
		JPanel jp_nomPartie=new JPanel();
		JPanel top=new JPanel();
		top.setLayout(new BorderLayout());
		JPanel jp_nbJoueurs=new JPanel();
		JPanel top2=new JPanel();
		JPanel bottom=new JPanel();
		
		creationPartie=new JPanel();
		creationPartie.setLayout(new BorderLayout());
		
		JLabel jl_nomPartie=new JLabel("Nom de la partie :");
		
		jtf_nomPartie=new JTextField();
		jtf_nomPartie.setPreferredSize(new Dimension(100,40));
		
		jp_nomPartie.add(jl_nomPartie);
		jp_nomPartie.add(jtf_nomPartie);
		
		JLabel jl_nbJoueurs=new JLabel("Nombre de joueurs :");
		
		String tab[]={"2","4","6"};
		nbJoueurs=new JComboBox<String>(tab);
		nbJoueurs.setPreferredSize(new Dimension(100,40));
		
		jp_nbJoueurs.add(jl_nbJoueurs);
		jp_nbJoueurs.add(nbJoueurs);
		
		top.add(jp_nbJoueurs,BorderLayout.CENTER);
		
		JLabel jl_taillePlateau=new JLabel("Taille du plateau :");
		
		String tab2[]={"Très petit","Petit","Moyen","Grand"};
		taillePlateau=new JComboBox<String>(tab2);
		taillePlateau.setPreferredSize(new Dimension(100,40));
		
		top2.add(jl_taillePlateau);
		top2.add(taillePlateau);
		
		creerPartie=new JButton("Creer");
		annuler=new JButton("Annuler");
		
		bottom.add(creerPartie);
		bottom.add(annuler);
		
		top.add(jp_nomPartie,BorderLayout.NORTH);
		creationPartie.add(top,BorderLayout.NORTH);
		creationPartie.add(top2,BorderLayout.CENTER);
		creationPartie.add(bottom,BorderLayout.SOUTH);
		
		creerPartie.addActionListener(this);
		annuler.addActionListener(this);
		
		lobby.add(creationPartie);
		jtf_nomPartie.requestFocusInWindow();
		lobby.revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(jb_rejoindre))
		{
			String nomPartie="";
			if(listeParties.getSelectedRowCount()==0)
			{
				
			}
			else
			{
				nomPartie=(String) listeParties.getValueAt(listeParties.getSelectedRow(),0);
				rejoindrePartie(nomPartie);
			}
		}
		else if(e.getSource().equals(jb_creer))
		{
			dessinerCreationPartie();
		}
		else if(e.getSource().equals(jb_rafraichir))
		{
			rafraichirListe();
		}
		else if(e.getSource().equals(creerPartie))
		{
			if(jtf_nomPartie.getText().equals(""))
			{
				JOptionPane.showMessageDialog(lobby,
					    "Veuillez entrer un nom pour votre partie.",
					    "Nom de la partie vide",
					    JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				String nomPartie=jtf_nomPartie.getText();
				String joueurs=(String) nbJoueurs.getSelectedItem();
				String taille=(String) taillePlateau.getSelectedItem();
				if(taille.equals("Très petit"))
				{
					taille="5";
				}
				else if(taille.equals("Petit"))
				{
					taille="7";
				}
				else if(taille.equals("Moyen"))
				{
					taille="9";
				}
				else if(taille.equals("Grand"))
				{
					taille="11";
				}
				
				String res=nomPartie+";"+joueurs+";"+taille;
				connexion.outLobby.println("###CREERPARTIE###");
				connexion.outLobby.println(res);
				
				String confirmation="";
				try {
					confirmation = connexion.inLobby.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(confirmation.equals("###DEJAUTILISE###"))
				{
					JOptionPane.showMessageDialog(lobby,
						    "Le nom que vous avez entré est déjà utilisé.",
						    "Nom de partie déjà utilisé",
						    JOptionPane.WARNING_MESSAGE);
				}
				else if(confirmation.equals("###OK###"))
				{
					rejoindrePartie(nomPartie);
				}
			}
		}
		else if(e.getSource().equals(annuler))
		{
			new Thread(new Runnable() 
			{
				public void run() 
				{
           
					SwingUtilities.invokeLater(new Runnable() 
					{
						public void run() 
						{
							lobby.removeAll();
							dessinerLobby();
							lobby.validate();
							lobby.repaint();
						}
					});
				}
			}).start();	
			
		}
	}

	private void rejoindrePartie(String nomPartie)
	{
		String res="";
		connexion.outLobby.println("###REJOINDREPARTIE###");
		connexion.outLobby.println(nomPartie);
		try 
		{
			res=connexion.inLobby.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if(res.equals("###OK###"))
		{
			String tab[];
			//connexion.lancementPartie();
			try {
				res=connexion.inLobby.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String split=";";
			Pattern pattern=Pattern.compile(split);
			tab=pattern.split(res);
			nbJoueursFinal=tab[0];
			taillePlateauFinal=tab[1];
			
			lancementPartie=true;
		}
		else if(res.equals("###PLEIN###"))
		{
			JOptionPane.showMessageDialog(lobby,
				    "La partie que vous essayez de rejoindre est pleine.",
				    "Partie pleine",
				    JOptionPane.WARNING_MESSAGE);
		}
		else if(res.equals("###PLUS###"))
		{
			JOptionPane.showMessageDialog(lobby,
				    "La partie que vous essayez de rejoindre n'existe plus.",
				    "Partie non existante",
				    JOptionPane.WARNING_MESSAGE);
		}
		else if(res.equals("###ERREURSERVEUR###"))
		{
			JOptionPane.showMessageDialog(lobby,
				    "Erreur du serveur.",
				    "Partie pleine",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void rafraichirListe() 
	{	
		String res="";
		String[] partie;
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		connexion.outLobby.println("###BESOINLISTE###");
		try 
		{
			res=connexion.inLobby.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		String split=";";
		Pattern pattern=Pattern.compile(split);
		while(!res.equals("###FINLISTE###"))
		{
			Vector<String> infoPartie=new Vector<String>();
			
			partie=pattern.split(res);
			infoPartie.add(partie[0]);
			infoPartie.add(partie[1]);
			infoPartie.add(partie[2]);
			data.add(infoPartie);
			try 
			{
				res=connexion.inLobby.readLine();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		
		dtm.setDataVector(data,idColonnes);
		listeParties.setModel(dtm);
	}
}
