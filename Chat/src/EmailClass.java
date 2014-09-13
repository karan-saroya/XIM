import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

import javax.swing.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import javax.activation.*;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
public class EmailClass extends JFrame implements KeyListener
{
	JPanel pass;
	JPanel user;
	JPanel to;
	JPanel from;
	JPanel sub;
	JPanel body;
	JTextField to_txt;
	JTextField from_txt;
	JTextField sub_txt; 
	JTextArea body_txt;
	JTextField user_txt;
	JPasswordField pass_txt;
	EmailClass obj;
	JPanel main_panel;
	EmailClass()
	{
		this.addKeyListener(this);
		obj=this;
		main_panel= new JPanel();
		//this.setUndecorated(true);
		//this.setOpacity(0.95f);
		
		main_panel.setLayout(new GridBagLayout());
		main_panel.setBackground(new Color(0xffffffff));
		
		user= new JPanel();
		user.setLayout(new GridBagLayout());
		JLabel user_lbl = new JLabel("Username:");
		user.setBackground(new Color(0xffffffff));
		user_lbl.setOpaque(false);
		user_lbl.setForeground(Color.BLACK);
		user_lbl.setBackground(new Color(0xffffffff));
		GridBagConstraints user_lbl_cons= new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
		user_txt = new JTextField();
		user_txt.setForeground(Color.BLACK);
		user_txt.setBackground(new Color(0xffffffff));
		
		GridBagConstraints user_txt_cons = new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,50,0,50),0,0);
		user.add(user_lbl,user_lbl_cons);
		user.add(user_txt,user_txt_cons);
		main_panel.add(user,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		
		
		pass= new JPanel();
		pass.setLayout(new GridBagLayout());
		JLabel pass_lbl = new JLabel("Password:");
		pass.setBackground(new Color(0xffffffff));
		pass_lbl.setOpaque(false);
		pass_lbl.setForeground(Color.BLACK);
		pass_lbl.setBackground(new Color(0xffffffff));
		GridBagConstraints pass_lbl_cons= new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
		pass_txt = new JPasswordField("");
		pass_txt.setForeground(Color.BLACK);
		pass_txt.setBackground(new Color(0xffffffff));
		
		GridBagConstraints pass_txt_cons = new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,50,0,50),0,0);
		pass.add(pass_lbl,pass_lbl_cons);
		pass.add(pass_txt,pass_txt_cons);
		
		
		main_panel.add(pass,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		
		
		
		to= new JPanel();
		to.setLayout(new GridBagLayout());
		JLabel to_lbl = new JLabel("To:");
		to.setBackground(new Color(0xffffffff));
		to_lbl.setOpaque(false);
		to_lbl.setForeground(Color.BLACK);
		to_lbl.setBackground(new Color(0xffffffff));
		
		GridBagConstraints to_lbl_cons= new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
		to_txt = new JTextField();
		to_txt.setForeground(Color.BLACK);
		to_txt.setBackground(new Color(0xffffffff));
		
		GridBagConstraints to_txt_cons = new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,50,0,50),0,0);
		to.add(to_lbl,to_lbl_cons);
		to.add(to_txt,to_txt_cons);
		
		main_panel.add(to,new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		from= new JPanel();
		
		from.setLayout(new GridBagLayout());
		JLabel from_lbl = new JLabel("From:");
		from.setBackground(new Color(0xffffffff));
		from_lbl.setOpaque(false);
		from_lbl.setForeground(Color.BLACK);
		from_lbl.setBackground(new Color(0xffffffff));
		
		GridBagConstraints from_lbl_cons= new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
		from_txt = new JTextField();
		from_txt.setForeground(Color.BLACK);
		from_txt.setBackground(new Color(0xffffffff));
		
		GridBagConstraints from_txt_cons = new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,50,0,50),0,0);
		from.add(from_lbl,from_lbl_cons);
		from.add(from_txt,from_txt_cons);
		
		main_panel.add(from,new GridBagConstraints(0,3,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		
		
		sub= new JPanel();
		sub.setLayout(new GridBagLayout());
		JLabel sub_lbl = new JLabel("Subject:");
		sub.setBackground(new Color(0xffffffff));
		sub_lbl.setOpaque(false);
		sub_lbl.setForeground(Color.BLACK);
		sub_lbl.setBackground(new Color(0xffffffff));
		
		GridBagConstraints sub_lbl_cons= new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
		sub_txt = new JTextField();
		sub_txt.setForeground(Color.BLACK);
		sub_txt.setBackground(new Color(0xffffffff));
		
		GridBagConstraints sub_txt_cons = new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,50,0,50),0,0);
		sub.add(sub_lbl,sub_lbl_cons);
		sub.add(sub_txt,sub_txt_cons);
		
		main_panel.add(sub,new GridBagConstraints(0,4,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		
		body= new JPanel();
		body.setLayout(new GridBagLayout());
		body.setBackground(new Color(0xffffffff));
		
		body_txt = new JTextArea();
		body_txt.setForeground(Color.BLACK);
		body_txt.setBackground(new Color(0xffffffff));
		body_txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		GridBagConstraints body_txt_cons = new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,50,0,50),0,0);
		body.add(body_txt,body_txt_cons);
		
		
		main_panel.add(body,new GridBagConstraints(0,5,1,1,1,4,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		
		
		JButton send = new JButton("Send");
		JButton close= new JButton("Close");
		main_panel.add(send,new GridBagConstraints(0,6,1,1,0,4,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		main_panel.add(close,new GridBagConstraints(0,6,1,1,1,4,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		
		
		send.addActionListener(new ActionListener()
									{
											public void actionPerformed(ActionEvent ae)
											{
												Properties props = new Properties();
												props.put("mail.smtp.auth", "true");
												props.put("mail.smtp.starttls.enable", "true");
												props.put("mail.smtp.host", "smtp.gmail.com");
												props.put("mail.smtp.port", "587");
										 
												Session session = Session.getInstance(props,
												  new javax.mail.Authenticator() {
													protected PasswordAuthentication getPasswordAuthentication() {
														return new PasswordAuthentication(user_txt.getText(), pass_txt.getText());
													}
												  });
										 
												try
												{
													final MimeMessage mess= new MimeMessage(session);
													mess.setFrom(new InternetAddress(from_txt.getText()));
													mess.addRecipient(RecipientType.TO, new InternetAddress(to_txt.getText()));
													mess.setText(body_txt.getText());
													mess.setSubject(sub_txt.getText());
													new Thread(new Runnable()
													{
														public void run()
														{
															try
															{
																Transport.send(mess);
															}
															catch(MessagingException me)
															{
																
																
															}
														}
														
														
													}
													).start();
												
												}
												catch(MessagingException e)
												{
													e.printStackTrace();
													
												}
												
											}
			
			
									});
		close.addActionListener(new ActionListener()
									{
											public void actionPerformed(ActionEvent ae)
											{
												obj.dispose();
											}
			
									});
		
		JScrollPane scroll= new JScrollPane(main_panel);
		this.add(scroll);
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) 
	{
		if(arg0.getKeyCode() == KeyEvent.VK_F4 )
		{
			this.setVisible(false);
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
