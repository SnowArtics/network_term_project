import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;

import java.sql.*;
import java.util.Scanner;

public class LoginPage {

   private JFrame frmWelcome;
   private JPasswordField passwordField;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               LoginPage window = new LoginPage();
               window.frmWelcome.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the application.
    */
   public LoginPage() {
      initialize();
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {
      JScrollPane scrollPane;
      ImageIcon icon = new ImageIcon("background.png");

      frmWelcome = new JFrame();
      frmWelcome.setTitle("Welcome");
      frmWelcome.setBounds(100, 100, 450, 300);
      frmWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frmWelcome.setSize(1024, 768);

      // ��� Panel ������ �������������� ����
      JPanel background = new JPanel() {
         public void paintComponent(Graphics g) {
            // Approach 1: Dispaly image at at full size
            g.drawImage(icon.getImage(), 0, 0, null);
            // Approach 2: Scale image to size of component
            // Dimension d = getSize();
            // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
            // Approach 3: Fix the image position in the scroll pane
            // Point p = scrollPane.getViewport().getViewPosition();
            // g.drawImage(icon.getImage(), p.x, p.y, null);
            setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
            super.paintComponent(g);
         }
      };

      frmWelcome.add(background);
      
      background.setLayout(null);
      
      
      JLabel Agario = new JLabel("AGAR.IO", SwingConstants.CENTER);
      Agario.setFont(new Font("���� ���", Font.PLAIN, 200));
      Agario.setBounds(20, 30, 1000, 300);
      background.add(Agario);

      JLabel ID = new JLabel("ID: ");
      ID.setFont(new Font("���� ���", Font.PLAIN, 50));
      ID.setBounds(100, 320, 100, 70);
      background.add(ID);

      JTextPane IDField = new JTextPane();
      IDField.setBounds(250, 320, 400, 70);
      IDField.setFont(new Font("���� ���", Font.PLAIN, 50));
      background.add(IDField);
      
      JLabel PW = new JLabel("PW: ");
      PW.setFont(new Font("���� ���", Font.PLAIN, 50));
      PW.setBounds(100, 420, 130, 70);
      background.add(PW);

      passwordField = new JPasswordField();
      passwordField.setBounds(247, 420, 410, 77);
      passwordField.setFont(new Font("���� ���", Font.PLAIN, 50));
      background.add(passwordField);

      JButton Login_Button = new JButton("Login");
      Login_Button.setFont(new Font("���� ���", Font.PLAIN, 25));
      Login_Button.setBounds(760, 380, 200, 70);
      background.add(Login_Button);
      Login_Button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BufferedReader in = null;
            BufferedWriter out = null;
            Socket socket = null;
            Scanner scanner = new Scanner(System.in);

            try {
               socket = new Socket("58.148.64.228", 5518);// �� �ƺ� ������ ��Ʈ
               
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

               out.write("login\n");
               System.out.println("�������� ���������� �����մϴ�");

               String ID = IDField.getText();

               out.write(ID + "\n");
               out.write(passwordField.getText() + "\n");

               out.flush();

               String inputMessage = in.readLine(); // �����κ��� ��� ����

               if (inputMessage.equals("True")) {
                  JOptionPane.showMessageDialog(null, "Login success", "Login", JOptionPane.INFORMATION_MESSAGE);

                  ChatClientApp app = new ChatClientApp();
                  app.enter_room(ID, socket);
                  // �α��ΰ� ���ÿ� �α��� â �������°�
                  // �����ؾߵɵ�------------------------------------------------------------

               } else {
                  JOptionPane.showMessageDialog(null, "type it again correctly.", "ERROR_MESSAGE",
                        JOptionPane.ERROR_MESSAGE);
                  IDField.setText("");
                  passwordField.setText("");
               }

            } catch (IOException f) {
               System.out.println(f.getMessage());
            }
         }
      });

      JButton Sign_Up_Button = new JButton("Sign Up");
      Sign_Up_Button.setFont(new Font("���� ���", Font.PLAIN, 25));
      background.add(Sign_Up_Button);
      Sign_Up_Button.setBounds(25, 600, 200, 70);
      Sign_Up_Button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            signUpPage sign_up_Page = new signUpPage();
            sign_up_Page.setVisible(true);
         }
      });
      

      JButton Find_ID_Button = new JButton("Find ID");
      Find_ID_Button.setFont(new Font("���� ���", Font.PLAIN, 25));
      background.add(Find_ID_Button);
      Find_ID_Button.setBounds(400, 600, 200, 70);
      Find_ID_Button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            findIDPage find_ID_Page = new findIDPage();
            find_ID_Page.setVisible(true);
         }
      });

      JButton Find_PW_Button = new JButton("Find PW");
      Find_PW_Button.setFont(new Font("���� ���", Font.PLAIN, 25));
      background.add(Find_PW_Button);
      Find_PW_Button.setBounds(775, 600, 200, 70);
      Find_PW_Button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            findPWPage find_PW_Page = new findPWPage();
            find_PW_Page.setVisible(true);
         }
      });

      

      
   }
}