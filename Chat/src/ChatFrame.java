import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.jivesoftware.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import sun.rmi.runtime.Log;

import com.sun.awt.AWTUtilities;
public class ChatFrame extends JFrame 
{
	static XMPPConnection conn ;
	ConnectionConfiguration config;
	JLabel acc_label;
	JScrollPane ros_scroll;
	ArrayList<RosEntry> ros_list= new ArrayList<RosEntry>();
	static Roster roster;
	JPanel roster_pane;
	static JPanel chat_pane;
	static String to="";
	static String from="karanveer singh";
	static String to_user="";
	static JPanel show_chat;
	JTextField chat_area;
	static int sent_row_number=0;
	JTabbedPane muc_pane;
	static ChatFrame obj;
	String text="";
	JPanel join_pane;
	JPanel create_pane;
	JTextField room_name,room_nick;
	JLabel rname,rnick;
	JButton create;
	JButton join;
	MultiUserChat muc;
	boolean is_muc=false;
	ChatFrame( XMPPConnection conn1,ConnectionConfiguration config1)
	{
		conn=conn1;
		config=config1;
		obj=this;
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
		this.setUndecorated(true);
		this.setOpacity(0.95f);
		
		this.setLayout(new GridBagLayout());
		this.addKeyListener(new KeyAdapter()
								{
									public void keyPressed(KeyEvent ke)
									{
										if(ke.getKeyCode()==KeyEvent.VK_ESCAPE)
										{
											System.exit(0);
											
										}
										
									}
			
		
								});
		


		
		JPanel top_panel= new JPanel();
		top_panel.setOpaque(true);
		top_panel.setBackground(new Color(0xff000000));
		top_panel.setLayout(new GridBagLayout());
		GridBagConstraints xim_cons = new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0); 
		CustPanel xim_panel=new CustPanel("xim_small");
		top_panel.add(xim_panel,xim_cons);
		
		acc_label = new JLabel(conn.getUser().substring(0, conn.getUser().indexOf("/")));
		acc_label.setIcon(new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+File.separator+"avail.png"));
		acc_label.setForeground(Color.WHITE);
		acc_label.setBackground(Color.black);
		acc_label.setOpaque(true);
		acc_label.addMouseListener(new MouseAdapter()
										{
												public void mouseEntered(MouseEvent me)
												{
													
													acc_label.setBackground(new Color(0xff0f0f0f));
													
												}
												public void mouseExited(MouseEvent me)
												{
													
													acc_label.setBackground(new Color(0xff000000));
												}
					
										});	
		GridBagConstraints acc_cons= new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		top_panel.add(acc_label,acc_cons);
		
		roster_pane = new JPanel();
		roster_pane.setVisible(true);
		roster_pane.setOpaque(true);
		roster_pane.setBackground(new Color(0xff2f2f2f));
	
		chat_pane = new JPanel();
		chat_pane.setOpaque(true);
		chat_pane.setBackground(new Color(0xff3f3f3f));
		ChatFrame.chat_pane.setLayout(new GridBagLayout());
		JPanel choice = new JPanel();
		choice.setBackground(new Color(0xff101010));
		choice.setLayout(new GridBagLayout());
		CustBtn chat= new CustBtn("chat");
		chat.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent ae)
				{
					
					show_chat = new JPanel();
					show_chat.setBackground(new Color(0xffffffff));
					show_chat.setLayout(new GridBagLayout());	
					
					
					GridBagConstraints show_cons= new GridBagConstraints(0,1,2,1,1,4,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
					
					chat_area = new JTextField();
					chat_area.setBackground(new Color(0xfff0f0f0));
					chat_area.setForeground(Color.BLACK);
					GridBagConstraints chat_area_cons=  new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
					
					
					JButton send = new JButton("Send");
					send.addActionListener(new ActionListener()
					{
							public void actionPerformed(ActionEvent ae)
							{
								if(!is_muc)
								{
									Message msg= new Message(to_user,Message.Type.chat);
									msg.setBody(chat_area.getText());
									conn.sendPacket(msg);
									String new_text=chat_area.getText();
									new_text="me"+":"+new_text;
									chat_area.setText("");
									GridBagConstraints speech_cons= new GridBagConstraints(0,sent_row_number++,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
									show_chat.add(new SpeechPanel(new_text),speech_cons);
									obj.revalidate();
								}
								else
								{
									Message msg= new Message(to_user,Message.Type.chat);
									msg.setBody(chat_area.getText());
									try
									{
										muc.sendMessage(msg);
									}
									catch(XMPPException xe )
									{
										
										
									}
									String new_text=chat_area.getText();
									new_text="me"+":"+new_text;
									chat_area.setText("");
									GridBagConstraints speech_cons= new GridBagConstraints(0,sent_row_number++,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
									show_chat.add(new SpeechPanel(new_text),speech_cons);
									obj.revalidate();
									
									
								}
							}
					});
				GridBagConstraints send_cons=  new GridBagConstraints(1,2,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
					
					
					JScrollPane show_pane= new JScrollPane(show_chat);
					show_pane.setOpaque(true);
					chat_pane.setBackground(Color.WHITE);
					chat_pane.add(show_pane,show_cons);
					chat_pane.add(chat_area,chat_area_cons);
					chat_pane.add(send,send_cons);
					
					
					
				}

		});

		GridBagConstraints chat_btn_cons=  new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		choice.add(chat,chat_btn_cons);
		CustBtn mail= new CustBtn("mail");
		mail.addActionListener(new ActionListener()
									{
											public void actionPerformed(ActionEvent ae)
											{
												
												EventQueue.invokeLater(new Runnable()
																			{
																				public void run()
																				{
																					
																					EmailClass email= new EmailClass();
																					Toolkit toolkit=Toolkit.getDefaultToolkit();
																					email.setSize(toolkit.getScreenSize().width/2,toolkit.getScreenSize().height/2);
																					email.setVisible(true);
																					email.setLocation(obj.getX()+obj.getWidth()/4 , obj.getY() +obj.getHeight()/4);
																					email.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
																					obj.revalidate();
																				}
													
													
																			});
												
												
											}
	
									});
		
		GridBagConstraints mail_btn_cons=  new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		choice.add(mail,mail_btn_cons);
		
		CustBtn group = new CustBtn("group");
		group.addActionListener(new ActionListener()
										{
			
												public void actionPerformed(ActionEvent ae)
												{
													
													if(show_chat!= null)
													{
														show_chat.removeAll();
														muc_pane= new JTabbedPane();
														create_pane= new JPanel();
														create_pane.setLayout(new GridBagLayout());
														room_name=new JTextField();
														rname=new JLabel("Enter Room Name");
													    create_pane.add(rname,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
													    create_pane.add(room_name,new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
														
													    
													    room_nick=new JTextField();
														rnick=new JLabel("Enter Nick Name");
													    create_pane.add(rnick,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
													    create_pane.add(room_nick,new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
														
													    create= new JButton("Create");
													    create.addActionListener(new ActionListener()
													    								{
													    	
													    									public void actionPerformed(ActionEvent ae)
													    									{
													    										is_muc=true;
													    										
													    										
													    										muc= new MultiUserChat(conn,room_name.getText());
													    										
													    										try
													    										{

														    										
													    											muc.create(rnick.getText());
													    											
													    										}
													    										catch(Exception e)
													    										{
													    											
													    										}
													    										muc.addMessageListener(new PacketListener()
						    																	{

																									@Override
																									public void processPacket(
																											Packet packet)
																									{
																										if(packet instanceof Message )
																										{
																											
																											
																										}
																										
																									}

																									
						    												
						    												
						    																	});
													    										try
													    										{
													    											muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
													    											
													    										}
													    										catch(XMPPException xe)
													    										{
													    											try
													    											{
													    												JOptionPane.showMessageDialog(null, "MultiChat not supported", "Error", JOptionPane.OK_OPTION);
													    											}
													    											catch(HeadlessException he)
													    											{
													    												
													    												
													    											}
													    										}
													    									}
													    	
													    								});
													    
													    create_pane.add(create,new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
													    
													    muc_pane.addTab("Create", create_pane);
													    
													    
													    
													    
													    
													    join_pane= new JPanel();
														join_pane.setLayout(new GridBagLayout());
														room_name=new JTextField();
														rname=new JLabel("Enter Room Name");
													    join_pane.add(rname,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
													    join_pane.add(room_name,new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
														
													    
													    room_nick=new JTextField();
														rnick=new JLabel("Enter Nick Name");
													    join_pane.add(rnick,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
													    join_pane.add(room_nick,new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
														
													    join= new JButton("Join");
													    join.addActionListener(new ActionListener()
													    								{
													    	
													    									public void actionPerformed(ActionEvent ae)
													    									{
													    										is_muc=true;
													    										
													    										muc= new MultiUserChat(conn,room_name.getText());
													    										try
													    										{
													    											muc.join(rnick.getText());
													    										}
													    										catch(Exception e)
													    										{
													    											
													    											
													    										}
													    										
													    				
													    										muc.addMessageListener(new PacketListener()
						    																	{

																									@Override
																									public void processPacket(
																											Packet packet)
																									{
																										if(packet instanceof Message )
																										{
																											
																											
																										}
																										
																									}

																									
						    												
						    												
						    																	});
													    										try
													    										{
													    											muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
													    											
													    										}
													    										catch(XMPPException xe)
													    										{
													    											try
													    											{
													    												JOptionPane.showMessageDialog(null, "MultiUser not supported", "Error", JOptionPane.OK_OPTION);
													    											}
													    											catch(HeadlessException he)
													    											{
													    												
													    												
													    											}
													    										}
													    									}
													    	
													    								});
													    
													    join_pane.add(join,new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
													    
													    muc_pane.addTab("Join", join_pane);
														show_chat.add(muc_pane,new GridBagConstraints(0,0,1,2,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
														obj.revalidate();
													}
													
												}
			
										});
		
		GridBagConstraints g_btn_cons=  new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
		choice.add(group,g_btn_cons);
		
		
		
		
		GridBagConstraints choice_cons= new GridBagConstraints(0,0,2,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		chat_pane.add(choice,choice_cons);
		
		
		conn.addPacketListener(new PacketListener()
									{
										public void processPacket(Packet packet)
										{
											if(packet.getClass() == Message.class)
											{
												Message msg=(Message)packet;
												
												if(msg.getBody()!=null)
												 text=msg.getBody();
											
													if(to_user.equals(msg.getFrom())|| to_user.contains(msg.getFrom().subSequence(0,msg.getFrom().length())))
													{
														String new_text=roster.getEntry(msg.getFrom()).getName()+":"+text;
														
														GridBagConstraints speech_cons= new GridBagConstraints(1,sent_row_number++,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
														show_chat.add(new SpeechPanelLeft(new_text),speech_cons);
														obj.revalidate();
													
													
													}
													else
													{
														to_user=msg.getFrom();
														to=roster.getEntry(to_user).getName();
														System.out.println(msg.getFrom());
														String new_text="";
														if(msg.getFrom().indexOf("/")!=-1)
															new_text=to_user.substring(0,to_user.indexOf("/"))+":"+text;
														else
															new_text=to+":"+text;
														
														sent_row_number=0;
														show_chat.removeAll();
														GridBagConstraints speech_cons= new GridBagConstraints(1,sent_row_number++,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
														show_chat.add(new SpeechPanelLeft(new_text),speech_cons);
														obj.revalidate();
														obj.repaint();
													
													}
												
												
												}
											}
										
			
									},null);
		
	
		
		
		GridBagConstraints top_cons = new GridBagConstraints(0,0,2,2,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0); 
		this.add(top_panel,top_cons);
		GridBagConstraints roster_cons = new GridBagConstraints(0,2,1,1,4,22,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		
		GridBagConstraints chat_cons =   new GridBagConstraints(1,2,0,1,6,22,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		 ros_scroll = new JScrollPane(roster_pane);
		ros_scroll.setOpaque(true);
	 Runnable get_roster =new Runnable()
		{
			synchronized public void run()
			{
				roster=conn.getRoster();
				
				
				roster.addRosterListener(new RosterListener()
				{

					@Override
					public void entriesAdded(
							Collection<String> arg0)
					{
						
						
					}

					@Override
					public void entriesDeleted(
							Collection<String> arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void entriesUpdated(
							Collection<String> arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void presenceChanged(
							Presence pres)
					{
						
						String jid=pres.getFrom();
						String user=null;
						if(jid.indexOf("/")!=-1)
						{
							user=jid.substring(0,jid.indexOf("/"));
						}
						for(int i=0;i<ros_list.size();i++)
						{
							if(ros_list.get(i).user.equals(user) && !(ros_list.get(i).presence.equals(pres.toString())))
							{
								RosEntry ros=ros_list.remove(i);
								roster_pane.remove(i);
								ros_list.add(i, new RosEntry(ros.name,ros.user,pres.toString()));
								roster_pane.add(ros_list.get(i), 0);
								
							}
							
								
						}
						obj.revalidate();
						
					}


				});
				Collection<RosterEntry> entries=roster.getEntries();
				Iterator<RosterEntry> iter=entries.iterator();
				while(iter.hasNext())
				{
				   RosterEntry entry= iter.next();
				   
				   ros_list.add(new RosEntry(entry.getName(),entry.getUser(),roster.getPresence(entry.getUser()).toString()));
				   
				}
				
				
			
				call_back();
			}

		};
		EventQueue.invokeLater(get_roster);	
		this.add(ros_scroll,roster_cons);
		this.add(chat_pane,chat_cons);
	}
	public void call_back()
	{
		roster_pane.setLayout(new GridLayout(ros_list.size(),1));
		
		for(int i=0;i<ros_list.size();i++)
		  	roster_pane.add(ros_list.get(i));
					
	}
	
	
}

class RosEntry extends JPanel implements MouseListener
{
	
	String name;
	String user;
	String status;
	String presence;
	
	RosEntry(String text,String user,String presence)
	{
		addMouseListener(this);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		name=text;
		this.user=user;
		setOpaque(true);
		this.setBackground(new Color(0xff000000));
		this.presence=presence;
		setLayout(new GridBagLayout());
		ImageIcon icon_un=null,icon_a=null;
		if(presence.equals("unavailable"))
		{
			
			icon_un = new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+File.separator+"grey.png");
			
		}
		else
		{
			icon_a = new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+File.separator+"avail.png");
			
		}
		
		JLabel name= new JLabel(this.name);
		
		if(presence.equals("unavailable"))
			name.setIcon(icon_un);
		else
			name.setIcon(icon_a);
		name.setForeground(Color.WHITE);
		name.setBackground(Color.BLACK);
		GridBagConstraints name_cons= new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		add(name,name_cons);
		
		JLabel pres= new JLabel(this.presence);
		pres.setForeground(Color.WHITE);
		pres.setBackground(Color.BLACK);
		GridBagConstraints pres_cons= new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
		add(pres,pres_cons);
	
	
		
		
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		ChatFrame.sent_row_number=0;
	   ChatFrame.to=this.name;
	   ChatFrame.to_user=this.user;
	   ChatFrame.show_chat.removeAll();
	   ChatFrame.obj.revalidate();
	   
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		setBackground(new Color(0xff1f1f1f));
		
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		setBackground(new Color(0xff000000));
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}

class SpeechPanel extends JPanel
{
	String text;
	Image img;
	BufferedImage buf_img;
	SpeechPanel(String text)
	{
		
		this.setOpaque(true);
		this.text=text;
		ImageIcon icon = new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+File.separator+"speech_right.png");
		img=icon.getImage();
		setPreferredSize(new Dimension(img.getWidth(null),img.getHeight(null)));
		buf_img= new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
	}
	public void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		this.setBackground(new  Color(0xff00000));
		Graphics2D graph_img=(Graphics2D)buf_img.getGraphics();
		graph_img.drawImage(img,0,0,null);
		graph_img.drawString(text,70,90);
		graph_img.dispose();
		g.drawImage(buf_img,0,0,null);
		
	}
	
}

class SpeechPanelLeft extends JPanel
{
	String text;
	Image img;
	BufferedImage buf_img;
	SpeechPanelLeft(String text)
	{
		
		this.setOpaque(true);
		this.text=text;
		ImageIcon icon = new ImageIcon(System.getProperty("user.dir")+File.separator+"res"+File.separator+"speech_left.png");
		img=icon.getImage();
		setPreferredSize(new Dimension(img.getWidth(null),img.getHeight(null)));
		buf_img= new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
	}
	public void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		this.setBackground(new  Color(0xff00000));
		Graphics2D graph_img=(Graphics2D)buf_img.getGraphics();
		graph_img.drawImage(img,0,0,null);
		graph_img.drawString(text,20,60);
		graph_img.dispose();
		g.drawImage(buf_img,0,0,null);
	}
	
}
