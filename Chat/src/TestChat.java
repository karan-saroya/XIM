import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.jivesoftware.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.*;

 class TestChat extends JFrame implements MouseMotionListener,MouseListener
{
	String username="";
	String password="";
	String server="";
	boolean is_connected=true;
	XMPPConnection conn;
	ConnectionConfiguration config;
	String error="";
	GlassPane pane=new GlassPane(server);
	ChatFrame chat_frame;
	int offset_x=0;
	int offset_y=0;
	Timer shakeTimer=null;
	static int NUM_HOSTS=3;
	JTextField user_name;
	JPasswordField pass;
	JDialog login_window;
	boolean first_pass=true;
	boolean first_user=true;
	JCheckBox rem_me;
	Save save;
	boolean saved=false;
	File save_file;
	public TestChat()
	{
		try
		{
			// Adding the Listener Section
			 save_file = new File(System.getProperty("user.dir")+File.separator+"res"+ File.separator+"db.dat");
			if(!save_file.exists())
			{
				try
				{
					save_file.createNewFile();
				}
				catch(IOException io)
				{
					JOptionPane.showMessageDialog(this,"Error Creating Save File", "Error", 1);
					System.exit(0);
				}
				save=new Save();
			}
			else
			{
			    ObjectInputStream ois= new ObjectInputStream(new FileInputStream(save_file));	
			    save=(Save)ois.readObject();
			    ois.close();
				
			}
			
			
			
			this.addKeyListener(new KeyAdapter()
									{
										public void keyPressed(KeyEvent ke)
										{
											if(ke.getKeyCode()==KeyEvent.VK_ESCAPE)
											{
												if(conn != null)
													{
														conn.disconnect(new Presence(Presence.Type.unavailable));
													}
												setVisible(false);
												System.exit(0);
												
											}
											
										}
				
			
									});
			
			this.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowIconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowClosing(WindowEvent arg0) 
				{
					conn.disconnect();
					
					
				}
				
				@Override
				public void windowClosed(WindowEvent arg0) {
					
					
				}
				
				@Override
				public void windowActivated(WindowEvent arg0) 
				{
					login_window.toFront();
					
				}
			
				
			});
			
		
			this.setUndecorated(true);
			this.add(new CustPanel("3"));
			
			login_window= new JDialog();
			login_window.setSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width/3),(int)(Toolkit.getDefaultToolkit().getScreenSize().height/2.5)));
			login_window.setUndecorated(true);
			login_window.setOpacity(0.4f);
			
			login_window.setBackground(new Color(0xfff0f0f0));
			login_window.addMouseListener(this);
			login_window.addMouseMotionListener(this);
			login_window.addKeyListener(new KeyAdapter()
					{
						public void keyPressed(KeyEvent ke)
						{
							if(ke.getKeyCode()==KeyEvent.VK_ESCAPE)
							{
								if(conn != null)
									conn.disconnect();
								setVisible(false);
								System.exit(0);
								
							}
							
						}


					});
			login_window.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-200,Toolkit.getDefaultToolkit().getScreenSize().height/2-150);
			//login_window.setAlwaysOnTop(true);
		
			login_window.setLayout(new GridBagLayout());
	
			
			
			GridBagConstraints user_cons = new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,50,0,50),0,0);
			user_name= new JTextField("Username@example.com");
			user_name.addFocusListener(new FocusListener()
										{

											@Override
											public void focusGained(
													FocusEvent arg0) 
											{
												if(first_user)
												{
													user_name.setText("");
													first_user=false;
												}
												
												
												
											}

											@Override
											public void focusLost(
													FocusEvent arg0) {
												// TODO Auto-generated method stub
												
											}
				
				
										});	
			user_name.setOpaque(true);
			user_name.setFont(new Font("Segoe UI",Font.PLAIN,18));
			user_name.setBackground(new Color(0xff000000));
			user_name.setForeground(new Color(0xAfAfAf));
			user_name.setSelectionColor(new Color(0xffaFaFaF));
			login_window.add(user_name,user_cons);
			
			GridBagConstraints pass_cons = new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(50,50,0,50),0,0);
			pass= new JPasswordField("password");
			pass.setSelectionColor(new Color(0xffaFaFaF));
			pass.setOpaque(true);
			pass.addFocusListener(new FocusListener()
									{

										@Override
										public void focusGained(FocusEvent arg0)
										{
											if(first_pass)
											{
												pass.setText("");
												first_pass=false;
											}
											if(save.info.containsKey(user_name.getText()))
											{
												pass.setText(save.info.get(user_name.getText()));
												pass.getPassword();
												
												
											}
											
										}

										@Override
										public void focusLost(FocusEvent arg0) {
											// TODO Auto-generated method stub
											
										}
										
				
									});
			pass.setFont(new Font("Segoe UI",Font.PLAIN,18));
			pass.setBackground(new Color(0xff000000));
			pass.setForeground(new Color(0xAfAfAf));
			login_window.add(pass,pass_cons);
			
			
			
			GridBagConstraints rem_cons = new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(20,50,0,0),0,0);
			rem_me = new JCheckBox("Remember Me");
			rem_me.setForeground(new Color(0x0F0F0F));
			login_window.add(rem_me,rem_cons);
			
			CustBtn login_btn= new CustBtn("login_but");
			login_btn.addActionListener(new ActionListener()
											{
												public void actionPerformed(ActionEvent ae)
												{
													
													if(rem_me.isSelected())
													{
														if(!(save.info.containsKey(user_name.getText()) && save.info.get(user_name.getText()).equals(pass.getText())))
														{
															if(save.info.containsKey(user_name.getText()))
															{
																save.info.remove(user_name.getText());
																save.info.put(user_name.getText(),pass.getText());
																
															}
															else
															{
																save.info.put(user_name.getText(),pass.getText());
																
															}
															try
															{
																ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(save_file));
																oos.writeObject(save);
																oos.close();
															}
															catch(IOException io)
															{
																io.printStackTrace();
															}
															
														}
														
													}
													
													
													username=user_name.getText();
													password=pass.getText();
													
													connect();
													
												}
				
				
											});	
			
			
			GridBagConstraints login_btn_cons = new GridBagConstraints(0,3,1,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(50,20,0,50),0,0);
			login_window.add(login_btn,login_btn_cons);
			
			
			
			
			
			
			
	
			
		
		  
		  
		  
		  
		  
		  
		  
		  
			
		
		}
		catch(Exception e)
		{
			
			
		}
		
		
	
	}
	private BufferedImage give_Image(Dimension size)
	{
		BufferedImage img= new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D img_graph=(Graphics2D)img.getGraphics();
		img_graph.setPaint(new GradientPaint(0,0,new Color(0.1f,0.1f,0.1f,0.9f),img.getWidth()/2,img.getHeight()/2,new Color(0.1f,0.1f,0.1f,0.6f)));
		img_graph.fillRect(0, 0,size.width, size.height);
		img_graph.dispose();
		return img;
	}
	public void mouseDragged(MouseEvent me)
	{
		
		login_window.setLocation(me.getLocationOnScreen().x-(offset_x), me.getLocationOnScreen().y-(offset_y));
		
	}
	/*
	public void paintComponent(Graphics g)
	{
		Graphics2D graph=(Graphics2D)g;
		GradientPaint p= new GradientPaint(0,0,Color.white,0,this.getHeight(),Color.black);
		Paint oldPaint= graph.getPaint();
		graph.setPaint(p);
		//graph.setBackground(Color.BLACK);
		graph.fillRect(0,0,this.getWidth(),this.getHeight());
	
	}
	*/
 public void connect()
	{
		if(username.indexOf("@") == -1)
			{
				server="gmail.com";
			}
		else
		{
			if(username.indexOf("@")!= -1 && username.length() !=( username.indexOf("@")))
				{
						server=username.substring(username.indexOf("@") +1);
						username=username.substring(0,username.indexOf("@"));
				}
			
		}
		
		pane = new GlassPane(server);
		pane.setVisible(true);
		repaint();
	    pane.addKeyListener(new KeyAdapter(){
	    	
	    	
	    });
		pane.addMouseListener(new MouseAdapter()
		{		
		 });
	    
		this.setGlassPane(pane);
		this.getGlassPane().setVisible(true);
		
	
   Runnable job= new Runnable()
     {     
	   public void run()
	   {
		try{
			if(server.equals("gmail.com"))
			 {

			    config = new ConnectionConfiguration("talk.google.com",5223,"gmail.com");
			    config.setReconnectionAllowed(true);
				config.setCompressionEnabled(false);
				config.setSASLAuthenticationEnabled(false);
				config.setSecurityMode(SecurityMode.enabled);
				config.setSocketFactory(new DummySSLSocketFactory());
			//	config.setDebuggerEnabled(true);
			    conn= new XMPPConnection(config);
			    conn.connect();
				
				
		     	} 
		   	else if(server.equals("chat.facebook.com"))
			{
		   		config = new ConnectionConfiguration("chat.facebook.com",5222);
			    config.setReconnectionAllowed(true);
				config.setCompressionEnabled(false);
				
				config.setSASLAuthenticationEnabled(true);
				//config.setSecurityMode(SecurityMode.enabled);
				//config.setSocketFactory(new DummySSLSocketFactory());
				
			    conn= new XMPPConnection(config);
			    conn.connect();
				
				
			}
			
			
		}
		catch(XMPPException xe)
		{
		      is_connected=false;
		    
		      if(xe != null)
		    	  JOptionPane.showMessageDialog(null,xe,"Error",JOptionPane.ERROR_MESSAGE);
				
		     	  
		 }
	 
     if(conn.isConnected())
		{
			try
			{
				conn.login(username,password,"karanveer_singh");	
				pane.setVisible(false);
	            logged_in(); 		
				
			}
			catch(XMPPException xe)
			{
				pane.removeAll();
				
				pane.setVisible(false);
				JOptionPane.showMessageDialog(null,xe.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				
				
			}
		
		}
		else
		{
			
			pane.removeAll();
			pane.setVisible(false);
			JOptionPane.showMessageDialog(null,error,"Error",JOptionPane.ERROR_MESSAGE);
			
		}
	   }
     };	
     		
     new Thread(job).start();
	}
 	public void logged_in()
 	{
 		
 		JPanel panel= new JPanel();
 		panel.setOpaque(true);
 		this.setGlassPane(panel);
 		
 		Runnable job= new Runnable()
		{
			public void run()
			{
				ChatFrame frame = new ChatFrame(conn,config);
				Toolkit toolkit= Toolkit.getDefaultToolkit();
				frame.setSize(toolkit.getScreenSize().width,toolkit.getScreenSize().height);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);						
			}	
		};
		EventQueue.invokeLater(job);
 		
 		
 		
 		Packet presence= new Presence(Presence.Type.available);
 		conn.sendPacket(presence); 
 		login_window.dispose();
 		this.dispose();
 		
 	}
 	
	public static void  main(String[] args)
	{
		Runnable job= new Runnable()
						{
							public void run()
							{
								TestChat window = new TestChat();
								window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								Toolkit toolkit=Toolkit.getDefaultToolkit();
								toolkit.setDynamicLayout(true);
								window.setSize(toolkit.getScreenSize().width,toolkit.getScreenSize().height);
								//window.setSize(1024,768);
								window.setVisible(true);
								window.requestFocus();
								window.login_window.setVisible(true);
								
							}
			
			
			
						};
			EventQueue.invokeLater(job);
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent me) 
	{
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent me)
	{
		offset_x=me.getLocationOnScreen().x-login_window.getLocationOnScreen().x;
		offset_y=me.getLocationOnScreen().y-login_window.getLocationOnScreen().y;
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
class GlassPane extends JComponent
{
	
	JPanel contentPane;
	GlassPane(String server)
	{
		
		
		setLayout(new GridLayout(2,1));
		setOpaque(false);
	    contentPane= new JPanel();
	    contentPane.setLayout(new GridLayout(2,1));
		contentPane.setOpaque(false);
		JPanel barpane = new JPanel();
		barpane.setLayout(new FlowLayout());
		JProgressBar bar = new JProgressBar();
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setPreferredSize(new Dimension(100,40));
		bar.setMinimumSize(new Dimension(100,40));
		bar.setMaximumSize(new Dimension(100,40));
		bar.setIndeterminate(true);	
		barpane.add(bar);
		barpane.setOpaque(false);
		Font ui_font = new Font("Segoe UI",Font.BOLD,32);
		JLabel conn_label=new JLabel("Connecting to "+ server,JLabel.CENTER);
		conn_label.setFont(ui_font);
		contentPane.add(conn_label);	
		contentPane.add(barpane);
		
		add(contentPane);
		
	
	}
	
	public void paintComponent(Graphics g)
	{
		Rectangle r = g.getClipBounds();
		Color alphaWhite = new Color(1.0f,1.0f,1.0f,0.75f);
		g.setColor(alphaWhite);
		g.fillRect(r.x,r.y,r.width,r.height);
	}
	
	
}



class CustPanel extends JPanel
{
	Image img;
	BufferedImage reflec;
	CustPanel(String name)
	{
		setOpaque(true);
		ImageIcon icon= new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+ File.separator+name+".jpg");
		img=icon.getImage();
		
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(img, 0, 0,null);
		
		
	}
	
	

}

class CustBtn extends JButton implements MouseListener
{
	Image img;
	ImageIcon icon;
	CustBtn(String name)
	{
		
		super("Login");
		this.addMouseListener(this);
		this.setOpaque(true);
		 icon= new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+ File.separator+name+".jpg");
		img=icon.getImage();
		this.setPreferredSize(new Dimension(img.getWidth(null),img.getHeight(null)));
		this.setBorder(null);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(img,0,0,img.getWidth(null),img.getHeight(null),null);
		
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		this.setBorder(null);
	}
	@Override
	public void mousePressed(MouseEvent arg0)
	{
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		this.setBorder(null);
		
	}
	
}
class Save implements Serializable
{
	HashMap<String,String> info = new HashMap<String,String>();
}




