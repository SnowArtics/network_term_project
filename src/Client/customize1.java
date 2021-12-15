import java.awt.Color;
import java.awt.Container;
// import java.awt.BorderLayout;
// import java.awt.FlowLayout;
// import java.awt.Graphics;
// import java.awt.Image;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.ImageIcon;

// import com.sun.tools.javac.launcher.Main;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

// import java.awt.Panel;
// import java.awt.GridLayout;
// import javax.swing.BoxLayout;
import java.awt.Font;
// import java.awt.Canvas;
// import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

// 1920 * 1080
public class customize1 extends JFrame {
   // extends ChatServerProcessThread
   
   private static final long serialVersionUID = 1L;
   // private static final long serialVersionUID = 1L;
   private JFrame frm1 = new JFrame();
   JPanel panel1 = new JPanel();
   JLabel imageLabel = new JLabel();
   JPanel panel2 = new JPanel();
   JPanel panel3 = new JPanel();
   
   Color curr = null;
   
   static Socket socket = null;
   static List<PrintWriter> listWriters = null;
    static List<ClientInfo> clientInfo = null;
   
   public customize1(Socket socket, List<PrintWriter> listWriters, List<ClientInfo> clientInfo) {
      
      // super(socket, listWriters);
      // frame ũ ⼳  ,       ġ
      
      this.socket = socket;
      this.listWriters = listWriters;
        this.clientInfo = clientInfo;
        
      Container contentPane = frm1.getContentPane();
      frm1.setTitle("User Information & Customizing");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frm1.setBounds(0,0,1920,1080);
      contentPane.setLayout(null);
      
      // panel, button     
      panel1.setLayout(null); // panel       ġ
      panel1.setBounds(30,30,450,100);
      panel1.setBackground(Color.BLUE);
      JButton btnNewButton = new JButton("Return to Chatting Room");
      btnNewButton.setFont(new Font("              Bold", Font.PLAIN, 30));
      btnNewButton.setBackground(Color.BLUE);
      btnNewButton.setBounds(0,0,450,100); // button   ġ,ũ       
      panel1.add(btnNewButton); // panel   button  ߰ 
      contentPane.add(panel1); // frame   panel  ߰   
      
      btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              // chatting room       ̵  // frame      ȭ         
         
              
				/*
				 * String serverIP = null; int portNumber = 0;
				 * 
				 * try { serverIP = "127.0.0.1"; portNumber = 5000; } catch(Exception exc) {
				 * serverIP = "localhost"; portNumber = 1234; }
				 * 
				 * BufferedReader in = null; BufferedWriter out = null;
				 * 
				 * try { Socket socket = new Socket(serverIP, portNumber); // inputStream ->
				 * pass user information (distinguish user) in = new BufferedReader(new
				 * InputStreamReader(socket.getInputStream())); // outputStream -> pass changed
				 * user info (color) out = new BufferedWriter(new
				 * OutputStreamWriter(socket.getOutputStream()));
				 * 
				 * while(true) { Color changedColor = curr;
				 * 
				 * // Use outputStream to modify user color info out.flush();
				 * 
				 * String inputMessage = in.readLine();
				 * 
				 * } } catch(IOException exc) { // System.out.println(e.getMessage()); } finally
				 * { try { if(socket != null) { socket.close(); } } catch(IOException exc) {
				 * 
				 * } }
				 */
            	frm1.dispose();
            }
       });
      
      // current user state
      
      imageLabel = new JLabel(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img0.png"));
      imageLabel.setBounds(195,210,340,340);
      contentPane.add(imageLabel);
      
      
      // user information
      panel2.setLayout(null);
      panel2.setBounds(140,650,480,300);
      panel2.setBackground(Color.LIGHT_GRAY);
      JLabel label1 = new JLabel("<html><body style='text-align:left;'># Number of Connections: <br /></body></html>");
      label1.setForeground(new Color(0, 0, 255));
      label1.setFont(new Font("              Regular", Font.PLAIN, 15));
      JLabel label2 = new JLabel("<html><body style='text-align:left;'># Recent Log In: <br /></body></html>");
      label2.setForeground(new Color(0, 0, 255));
      label2.setFont(new Font("              Regular", Font.PLAIN, 15));
      JLabel label3 = new JLabel("<html><body style='text-align:left;'># IP Address: <br /></body></html>");
      label3.setForeground(new Color(0, 0, 255));
      label3.setFont(new Font("              Regular", Font.PLAIN, 15));
      JLabel label4 = new JLabel("<html><body style='text-align:left;'># Play Time: <br /></body></html>");
      label4.setForeground(new Color(0, 0, 255));
      label4.setFont(new Font("              Regular", Font.PLAIN, 15));
      JLabel label5 = new JLabel("<html><body style='text-align:left;'># 1st place holding time: <br /></body></html>");
      label5.setForeground(new Color(0, 0, 255));
      label5.setFont(new Font("              Regular", Font.PLAIN, 15));
      
      label1.setBounds(50,55,201,30);
      label1.setBackground(Color.WHITE);
      label2.setBounds(50,95,129,30);
      label2.setBackground(Color.WHITE);
      label3.setBounds(50,135,106,30);
      label3.setBackground(Color.WHITE);
      label4.setBounds(50,175,97,30);
      label4.setBackground(Color.WHITE);
      label5.setBounds(50,215,184,30);
      label5.setBackground(Color.WHITE);
      
      panel2.add(label1);
      panel2.add(label2);
      panel2.add(label3);
      panel2.add(label4);
      panel2.add(label5);
      contentPane.add(panel2);
      
      
      JLabel lblNewLabel_1 = new JLabel("New label");
      lblNewLabel_1.setBounds(308, 95, 97, 30);
      panel2.add(lblNewLabel_1);
      
      JLabel lblNewLabel_1_1 = new JLabel("New label");
      lblNewLabel_1_1.setBounds(308, 135, 97, 30);
      panel2.add(lblNewLabel_1_1);
      
      JLabel lblNewLabel_1_1_1 = new JLabel("New label");
      lblNewLabel_1_1_1.setBounds(308, 175, 97, 30);
      panel2.add(lblNewLabel_1_1_1);
      
      JLabel lblNewLabel_1_1_1_1 = new JLabel("New label");
      lblNewLabel_1_1_1_1.setBounds(308, 215, 97, 30);
      panel2.add(lblNewLabel_1_1_1_1);
      
      
      //    ο       ư             ϱ 
      JButton color1 = new JButton();
      color1.setSize(110, 110);
      color1.setLocation(120, 150);
      color1.setFocusPainted(false);
      color1.setBorderPainted(false);
      ImageIcon circle1 = new ImageIcon("~~~");
      color1.setIcon(circle1);
      color1.setBackground(Color.BLACK);
      color1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img1.png"));
              curr = Color.BLACK;
            }
       });
      
      JButton color2 = new JButton();
      color2.setSize(110, 110);
      color2.setLocation(345, 150);
      color2.setFocusPainted(false);
      color2.setBorderPainted(false);
      ImageIcon circle2 = new ImageIcon("~~~");
      color2.setIcon(circle2);
      color2.setBackground(Color.YELLOW);
      color2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img2.png"));
               curr = Color.YELLOW;
            }
       });
      
      JButton color3 = new JButton();
      color3.setSize(110, 110);
      color3.setLocation(570, 150);
      color3.setFocusPainted(false);
      color3.setBorderPainted(false);
      ImageIcon circle3 = new ImageIcon("~~~");
      color3.setIcon(circle3);
      color3.setBackground(Color.GREEN);
      color3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img3.png"));
               curr = Color.GREEN;
            }
       });
      
      JButton color4 = new JButton();
      color4.setSize(110, 110);
      color4.setLocation(120, 375);
      color4.setFocusPainted(false);
      color4.setBorderPainted(false);
      ImageIcon circle4 = new ImageIcon("~~~");
      color4.setIcon(circle4);
      color4.setBackground(Color.BLUE);
      color4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img4.png"));
               curr = Color.BLUE;
            }
       });
      
      JButton color5 = new JButton();
      color5.setSize(110, 110);
      color5.setLocation(345, 375);
      color5.setFocusPainted(false);
      color5.setBorderPainted(false);
      ImageIcon circle5 = new ImageIcon("~~~");
      color5.setIcon(circle5);
      color5.setBackground(Color.ORANGE);
      color5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img5.png"));
               curr = Color.ORANGE;
            }
       });
      
      JButton color6 = new JButton();
      color6.setSize(110, 110);
      color6.setLocation(570, 375);
      color6.setFocusPainted(false);
      color6.setBorderPainted(false);
      ImageIcon circle6 = new ImageIcon("~~~");
      color6.setIcon(circle6);
      color6.setBackground(Color.CYAN);
      color6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img6.png"));
               curr = Color.CYAN;
            }
       });
      
      JButton color7 = new JButton();
      color7.setSize(110, 110);
      color7.setLocation(120, 600);
      color7.setFocusPainted(false);
      color7.setBorderPainted(false);
      ImageIcon circle7 = new ImageIcon("~~~");
      color7.setIcon(circle7);
      color7.setBackground(Color.MAGENTA);
      color7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img7.png"));
               curr = Color.MAGENTA;
            }
       });
      
      JButton color8 = new JButton();
      color8.setSize(110, 110);
      color8.setLocation(345, 600);
      color8.setFocusPainted(false);
      color8.setBorderPainted(false);
      ImageIcon circle8 = new ImageIcon("~~~");
      color8.setIcon(circle8);
      color8.setBackground(Color.PINK);
      color8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img8.png"));
               curr = Color.PINK;
            }
       });
      
      JButton color9 = new JButton();
      color9.setSize(110, 110);
      color9.setLocation(570, 600);
      color9.setFocusPainted(false);
      color9.setBorderPainted(false);
      ImageIcon circle9 = new ImageIcon("~~~");
      color9.setIcon(circle9);
      color9.setBackground(Color.GRAY);
      color1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               imageLabel.setIcon(new ImageIcon("C:\\Users\\chado\\eclipse-workspace\\NetworkGUI\\src\\img9.png"));
               curr = Color.GRAY;
            }
       });
      
      
      // panel3    ְ    ġ     
      panel3.setSize(800, 829);
      panel3.setBackground(Color.LIGHT_GRAY);
      panel3.setLayout(null);
      panel3.add(color1);
      panel3.add(color2);
      panel3.add(color3);
      panel3.add(color4);
      panel3.add(color5);
      panel3.add(color6);
      panel3.add(color7);
      panel3.add(color8);
      panel3.add(color9);
      contentPane.add(panel3);
      panel3.setLocation(960, 121);
      
      
   }

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { customize1 obj1 = new
	 * customize1(socket, listWriters, clientInfo); obj1.frm1.setVisible(true); }
	 * catch (Exception e) { e.printStackTrace(); } } }); }
	 */
   
   public void show() {
	   frm1.setVisible(true);
   }
   
   
}

