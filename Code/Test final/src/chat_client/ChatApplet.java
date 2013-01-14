package chat_client;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ChatApplet extends Applet implements ActionListener, MouseListener
{
	 private GestionSocket gs = new GestionSocket (this);

    private TextField textField1 = new TextField();
    
    private TextField textField2 = new TextField();

    private Button connectButton = new Button ();

    private Button disconnectButton = new Button ();

    private Button sendButton = new Button ();

    public TextArea textArea1 = new TextArea();
    
    public Label label = new Label();

    private boolean isConnected     = false;
	
	public void init() {

        try {

             apInit();

        }

        catch(Exception e) {

             e.printStackTrace();

        }

   }
	private void apInit() throws Exception {

        this.setLayout (null);



        // -- Begin buttons section

        sendButton.setLabel("Send");

        sendButton.setBounds (new Rectangle(246, 245, 67, 23));

        sendButton.addActionListener (this);

        this.add(sendButton);



        connectButton.setLabel("connect");

        connectButton.setBounds(new Rectangle(157, 270, 75, 22));

        connectButton.addActionListener(this);

        this.add(connectButton, null);



        disconnectButton.addActionListener(this);

        disconnectButton.setBounds(new Rectangle(235, 270, 78, 22));

        disconnectButton.setLabel("disconnect");

        this.add(disconnectButton, null);

        // -- End buttons section



        // -- Begin edit controls

        textField1.setBounds(new Rectangle(10, 245, 233, 23));

        textField1.addActionListener(this);

        textField1.setBackground(Color.lightGray);

        this.add(textField1, null);



        textField2.setBounds(new Rectangle(63, 270, 88, 23));

        textField2.addActionListener(this);

        textField2.setBackground(Color.lightGray);

        this.add(textField2, null);



        label.setBounds(new Rectangle(10, 270, 50, 23));
        
        label.setText("login :");
        
        label.setBackground(Color.white);
        
        this.add(label, null);



        textArea1.setBounds(new Rectangle(10, 10, 303, 233));

        textArea1.setBackground(Color.lightGray);

        this.add(textArea1, null);

        // -- End edit controls

        this.setBackground(Color.black);

   }
	
	public void setConnected(boolean aConnected) 
	{

        isConnected = aConnected;

	}



   public boolean getConnected()   
   {

        return isConnected;

   }

   public TextArea getTextArea () 
   {
        return textArea1;
   }
   
   public synchronized void addText (String aText)
   {
	   textArea1.append (aText + "\n");
   }

   public synchronized void addSystemMessage(String aText)
   {
       textArea1.append(aText + "\n");
   }
   
   public void sendMessage (String aMessage)
   {
       gs.getOutput().println (aMessage);
       gs.getOutput().flush();
   }
   
   private void sendButtonPressed()
   {
       if (!isConnected)
       {
            textArea1.append("Please connect first.\n");
            return;
       }
       String text = textField1.getText();
       if (text.equals(""))
       {
    	   return;
       }
       sendMessage (text);
       textField1.setText ("");
   }

	public void actionPerformed (ActionEvent ae) 
	{
        if (ae.getSource().equals(textField1)) //catch ActionEvents coming from textField1
        {
             sendButtonPressed();

        }
        else if (ae.getSource().equals(connectButton))//catch ActionEvents coming connect button from textField1
        {
        	 if(textField2.getText().length()==0)
             {
            	 addSystemMessage("Please choose a login.");
             }
             else if (!isConnected) 
             {
                  addSystemMessage("Connecting...");
                  isConnected = gs.connect(textField2.getText());
             }
             else
             {
                  addSystemMessage("Already connected.");
             }
        }
        else if (ae.getSource().equals(sendButton))//catch ActionEvents coming send button from textField1
        {
             sendButtonPressed();
        } 
        else if (ae.getSource().equals(disconnectButton))//catch ActionEvents comming from disconnect button
        {
             gs.disconnect();
        } 
   }

	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
