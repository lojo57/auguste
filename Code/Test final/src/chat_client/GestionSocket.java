package chat_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class GestionSocket {


    public static final int SERVER_PORT = 2002;
    
	private Socket m_Socket = null;

    private BufferedReader in = null;

    private PrintWriter out = null;

    private ChatApplet m_Applet;
    
    public String message=null;
    
    

    public GestionSocket (ChatApplet anApplet)
    {
         this.m_Applet = anApplet;
    }

    public PrintWriter getOutput ()
    {
         return out;
    }

    public BufferedReader getInput ()
    {
         return in;
    }

    public boolean connect (String login)
    {
         boolean successfull = true;
         String serverHost = m_Applet.getCodeBase().getHost();
         if (serverHost.length()==0)
         {
              m_Applet.addSystemMessage("Warning: Applet is loaded from a local file,");
              m_Applet.addSystemMessage("not from a web server. Web browser's security");
              m_Applet.addSystemMessage("will probably disable socket connections.");
              serverHost = "localhost";
         }
         try
         {
              m_Socket = new Socket(serverHost, SERVER_PORT);
              in = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
              out = new PrintWriter(new OutputStreamWriter(m_Socket.getOutputStream()));
              out.println (login);
              out.flush();
              message=in.readLine();
              if(message.equals("ok"))
              {
            	  m_Applet.addSystemMessage("Connected to server " + serverHost + ":" + SERVER_PORT);
            	  m_Applet.addSystemMessage("with the login : " + login);
              }
              else
              {
            	  System.out.println("Ce pseudo est déjà prit, choisissez en un autre.");
            	  m_Socket.close();
              }
         }
         catch (SecurityException se)
         {
        	 
             m_Applet.addSystemMessage("Security policy does not allow " + "connection to " + serverHost + ":" + SERVER_PORT);
             successfull = false;
        }
        catch (IOException e)
        {
             m_Applet.addSystemMessage("Can not establish connection to " + serverHost + ":" + SERVER_PORT);
             successfull = false;
        }

         // Create and start Listener thread

         Listener listener = new Listener(m_Applet, in, this);
         listener.setDaemon(true);
         listener.start();
         return successfull;
    }

    public void disconnect()
    {
         if (!m_Applet.getConnected())
         {
              m_Applet.addSystemMessage("Can not disconnect. Not connected.");
              return;
         }
         m_Applet.setConnected(false);
         try
         {
              m_Socket.close();
         } 
         catch (IOException ioe)
         {
         }
         m_Applet.addSystemMessage("Disconnected.");
    }

    class Listener extends Thread
    {
         private BufferedReader mIn;
         private ChatApplet mCA;
         private GestionSocket mGs;
         /**

          * Constructor initiliaze InputStream, and ChatApplet reference

          * @param aCA - ChatApplet reference

          * @param aIn - InputStream from server connection

          */

         public Listener (ChatApplet aCA, BufferedReader aIn, GestionSocket aGs)
         {
              mCA = aCA;
              mIn  = aIn;
              mGs = aGs;
         }

         public void run()
         {
              try
              {
                   while (!isInterrupted())
                   {
                        String message = mIn.readLine();
                        int colon2index = message.indexOf(":",message.indexOf(":")+1);
                        mCA.addText(message);
                   }

              }
              catch (Exception ioe)
              {
                   if (m_Applet.getConnected())
                   {
                	   if(mGs.message.equals("!ok"))
                	   {
                		   m_Applet.addSystemMessage("Login already used.");
                	   }
                	   else
                	   {
                		   m_Applet.addSystemMessage("Connexion error.");
                	   }
                   }
              }
              m_Applet.setConnected(false);
         }
    }
}

