package proelio_client;

import java.net.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import javax.swing.*;

public class Connexion implements ActionListener, KeyListener
{
	Socket connect;
	Socket connectMsg;
	Socket connectLobby;
	PrintStream out;
	BufferedReader in;
	PrintStream outMsg;
	BufferedReader inLobby;
	PrintStream outLobby;
	ReceptionMsg receptionMsg;
	Window window;
	JOptionPane optionPane;
	JPanel jp;
	JButton jb_ok;
	JTextField jtf_pseudo;
	
	String pseudonyme="";
	
	public Connexion() 
	{
		try 
		{
			connect = new Socket("127.0.0.1",2002);
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("Impossible de se connecter à l'adresse "+connect.getLocalAddress()+"\n");
		} 
		catch (IOException e) 
		{
			System.out.println("Aucun serveur à l'écoute du port 2002 \n");
		}
		try 
		{
			connectMsg = new Socket("127.0.0.1",2003);
			System.out.println("Connexion établie avec le serveur, en attente de confirmation : \n");
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("Impossible de se connecter à l'adresse "+connect.getLocalAddress()+"\n");
		} 
		catch (IOException e) 
		{
			System.out.println("Aucun serveur à l'écoute du port 2003 \n");
		}
		
		try 
		{
			connectLobby = new Socket("127.0.0.1",2004);
			System.out.println("Connexion établie avec le serveur, en attente de confirmation : \n");
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("Impossible de se connecter à l'adresse "+connect.getLocalAddress()+"\n");
		} 
		catch (IOException e) 
		{
			System.out.println("Aucun serveur à l'écoute du port 2004 \n");
		}
		
		try
		{
			in  = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			out = new PrintStream(connect.getOutputStream());
			outMsg = new PrintStream(connectMsg.getOutputStream());
			inLobby = new BufferedReader(new InputStreamReader(connectLobby.getInputStream()));
			outLobby = new PrintStream(connectLobby.getOutputStream());
		} 
		catch (Exception e)
		{ 
			System.err.println("Socket getInput: "+e); 
		}
	}
	
	public void lancementLobby()
	{
		jp=new JPanel();
		JLabel jl_pseudo=new JLabel("Entrez votre pseudonyme");
		
		jtf_pseudo=new JTextField();
		jtf_pseudo.setPreferredSize(new Dimension(100,40));
		jtf_pseudo.addKeyListener(this);
		jb_ok=new JButton("Ok");
		
		jb_ok.addActionListener(this);
		
		jp.add(jl_pseudo);
		jp.add(jtf_pseudo);
		
		jp.add(jb_ok);
		jtf_pseudo.setVisible(true);
		
		
	}
	
	public void creerReceptionMsg(Window aWindow)
	{
		window = aWindow;
		try
		{
			receptionMsg = new ReceptionMsg(connectMsg, this);
			receptionMsg.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if(jtf_pseudo.getText().equals(""))
			{
				JOptionPane.showMessageDialog(jp,
					    "Veuillez entrer un pseudonyme.",
					    "Pseudonyme vide",
					    JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				outLobby.println(jtf_pseudo.getText());
				String res="";
				try
				{
					res = inLobby.readLine();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				if(res.equals("###OK###"))
				{
					pseudonyme=jtf_pseudo.getText();
				}
				else if(res.equals("###DEJAUTILISE###"))
				{
					JOptionPane.showMessageDialog(jp,
						    "Le pseudonyme choisi est déjà utilisé.",
						    "Pseudonyme déjà utilisé",
						    JOptionPane.WARNING_MESSAGE);
				}
				
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(jb_ok))
		{
			if(jtf_pseudo.getText().equals(""))
			{
				JOptionPane.showMessageDialog(jp,
					    "Veuillez entrer un pseudonyme.",
					    "Pseudonyme vide",
					    JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				outLobby.println(jtf_pseudo.getText());
				String res="";
				try
				{
					res = inLobby.readLine();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				System.out.println(res);
				if(res.equals("###OK###"))
				{
					pseudonyme=jtf_pseudo.getText();
				}
				else if(res.equals("###DEJAUTILISE###"))
				{
					JOptionPane.showMessageDialog(jp,
						    "Le pseudonyme choisi est déjà utilisé.",
						    "Pseudonyme déjà utilisé",
						    JOptionPane.WARNING_MESSAGE);
				}
				
			}
		}
	}
}
