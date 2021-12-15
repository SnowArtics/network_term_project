import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.*;
import java.net.*;
import java.util.*;

public class findIDPage extends JFrame {

   private JPanel contentPane;
   private JTextField NameField;
   private JTextField BirthdayField;
   private JTextField ContactField;
   private JLabel Question;
   private JTextField AnswerField;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               findIDPage frame = new findIDPage();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the frame.
    */
   public findIDPage() {
      setTitle("find ID");
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setBounds(100, 100, 250, 319);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      JLabel Name = new JLabel("Name");
      Name.setBounds(27, 28, 57, 15);
      contentPane.add(Name);

      NameField = new JTextField();
      NameField.setBounds(89, 25, 116, 21);
      contentPane.add(NameField);
      NameField.setColumns(10);

      BirthdayField = new JTextField();
      BirthdayField.setBounds(89, 56, 116, 21);
      contentPane.add(BirthdayField);
      BirthdayField.setColumns(10);

      JLabel Birthday = new JLabel("Birthday");
      Birthday.setBounds(27, 59, 57, 15);
      contentPane.add(Birthday);

      JLabel Contact = new JLabel("Contact");
      Contact.setBounds(27, 90, 57, 15);
      contentPane.add(Contact);

      ContactField = new JTextField();
      ContactField.setBounds(89, 87, 116, 21);
      contentPane.add(ContactField);
      ContactField.setColumns(10);

      Question = new JLabel("Question");
      Question.setBounds(27, 121, 57, 15);
      contentPane.add(Question);

      JComboBox QuestionCombo = new JComboBox();
      QuestionCombo.setModel(new DefaultComboBoxModel(new String[] { "where are you live?",
            "What game did you play recently?", "What did you do last Christmas?", "What high school are you from?",
            "What's the latest movie you watched?", "Who do you like more, mom or dad?" }));
      QuestionCombo.setToolTipText("");
      QuestionCombo.setBounds(27, 146, 178, 23);
      contentPane.add(QuestionCombo);

      JLabel Answer = new JLabel("Answer");
      Answer.setBounds(27, 179, 57, 15);
      contentPane.add(Answer);

      AnswerField = new JTextField();
      AnswerField.setBounds(27, 204, 178, 21);
      contentPane.add(AnswerField);
      AnswerField.setColumns(10);

      
      JButton btnNewButton = new JButton("find ID");
      btnNewButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BufferedReader in = null;
            BufferedWriter out = null;
            Socket socket = null;
            Scanner scanner = new Scanner(System.in);

            try {
               socket = new Socket("58.148.64.228", 5518);// 내 맥북 서버와 포트
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

               out.write("findID\n");
               System.out.println("서버에게 관련정보를 전달합니다");

               out.write(NameField.getText() + "\n");
               out.write(BirthdayField.getText() + "\n");
               out.write(ContactField.getText() + "\n");
               out.write(QuestionCombo.getSelectedItem() + "\n");
               out.write(AnswerField.getText() + "\n");

               out.flush();

               String inputMessage = in.readLine(); // 서버로부터 결과 수신
               
               if (inputMessage.equals("False")) {
                  JOptionPane.showMessageDialog(null, "type it again correctly.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                  NameField.setText("");
                  BirthdayField.setText("");
                  ContactField.setText("");
                  AnswerField.setText("");
                  
               } else {
                  JOptionPane.showMessageDialog(null, "ID: " + inputMessage, "find_ID", JOptionPane.INFORMATION_MESSAGE);
               } // server로 부터 받은 ID 출력하기

            } catch (IOException f) {
               System.out.println(f.getMessage());
            } finally {
               try {
                  scanner.close();
                  if (socket != null)
                     socket.close(); // 클라이언트 소켓 닫기
               } catch (IOException f) {
                  System.out.println("서버와 채팅 중 오류가 발생했습니다.");
               }
            }

         }
      });
      btnNewButton.setBounds(66, 247, 97, 23);
      contentPane.add(btnNewButton);
   }

}