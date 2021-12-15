import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class signUpPage extends JFrame {

   private JPanel contentPane;
   private JTextField NameField;
   private JTextField IDField;
   private JTextField PWField;
   private JLabel Question;
   private JTextField AnswerField;
   private JTextField BirthdayField;
   private JTextField ContactField;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               signUpPage frame = new signUpPage();
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
   public signUpPage() {
      setTitle("sign up");
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setBounds(100, 100, 250, 388);
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
      
      IDField = new JTextField();
      IDField.setBounds(89, 56, 116, 21);
      contentPane.add(IDField);
      IDField.setColumns(10);
      
      JLabel ID = new JLabel("ID");
      ID.setBounds(27, 59, 57, 15);
      contentPane.add(ID);
      
      JLabel PW = new JLabel("PW");
      PW.setBounds(27, 90, 57, 15);
      contentPane.add(PW);
      
      PWField = new JTextField();
      PWField.setBounds(89, 87, 116, 21);
      contentPane.add(PWField);
      PWField.setColumns(10);
      
      Question = new JLabel("Question");
      Question.setBounds(27, 183, 57, 15);
      contentPane.add(Question);
      
      JComboBox QuestionCombo = new JComboBox();
      QuestionCombo.setModel(new DefaultComboBoxModel(new String[] {"where are you live?", "What game did you play recently?", "What did you do last Christmas?", "What high school are you from?", "What's the latest movie you watched?", "Who do you like more, mom or dad?"}));
      QuestionCombo.setToolTipText("");
      QuestionCombo.setBounds(27, 208, 178, 23);
      contentPane.add(QuestionCombo);
      
      JLabel Answer = new JLabel("Answer");
      Answer.setBounds(27, 241, 57, 15);
      contentPane.add(Answer);
      
      AnswerField = new JTextField();
      AnswerField.setBounds(27, 266, 178, 21);
      contentPane.add(AnswerField);
      AnswerField.setColumns(10);
            
      JLabel Birthday = new JLabel("Birthday");
      Birthday.setBounds(27, 121, 57, 15);
      contentPane.add(Birthday);
      
      BirthdayField = new JTextField();
      BirthdayField.setColumns(10);
      BirthdayField.setBounds(89, 121, 116, 21);
      contentPane.add(BirthdayField);
      
      JLabel Contact = new JLabel("Contact");
      Contact.setBounds(27, 152, 57, 15);
      contentPane.add(Contact);
      
      ContactField = new JTextField();
      ContactField.setColumns(10);
      ContactField.setBounds(89, 152, 116, 21);
      contentPane.add(ContactField);
      
      JButton Sign_Up_Button = new JButton("sign up");
      Sign_Up_Button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BufferedReader in = null;
            BufferedWriter out = null;
            Socket socket = null;
            Scanner scanner = new Scanner(System.in);

            try {
               socket = new Socket("58.148.64.228", 5518);// 내 맥북 서버와 포트
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
               
               out.write("signUp\n");
               System.out.println("서버에게 관련정보를 전달합니다");

               out.write(NameField.getText() + "\n");
               out.write(IDField.getText() + "\n");
               out.write(PWField.getText() + "\n");
               out.write(BirthdayField.getText() + "\n");
               out.write(ContactField.getText() + "\n");
               out.write(QuestionCombo.getSelectedItem() + "\n");
               out.write(AnswerField.getText() + "\n");

               out.flush();

               String inputMessage = in.readLine(); // 서버로부터 결과 수신

               if (inputMessage.equals("Fin")) {
                  JOptionPane.showMessageDialog(null, "회원가입이 정상적으로 진행되었습니다", "find_PW", JOptionPane.INFORMATION_MESSAGE);
               } else {
                  JOptionPane.showMessageDialog(null, "동일한 ID가 있습니다. 다른 ID로 회원가입을 진행해 주세요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                  NameField.setText("");
                  IDField.setText("");
                  PWField.setText("");
                  BirthdayField.setText("");
                  ContactField.setText("");
                  AnswerField.setText("");
               } // 회원가입이 정상적으로 진행되지 않은 경우

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
            
            //signup(NameField.getText(), IDField.getText(), PWField.getText(), BirthdayField.getText(), ContactField.getText(), QuestionCombo.getSelectedItem().toString(), AnswerField.getText());
         }
      });
      Sign_Up_Button.setBounds(66, 309, 97, 23);
      contentPane.add(Sign_Up_Button);
   }
   
      
   
   
}