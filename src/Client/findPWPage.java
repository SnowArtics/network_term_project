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

public class findPWPage extends JFrame {

   private JPanel contentPane;
   private JTextField NameField;
   private JTextField IDField;
   private JTextField BirthdayField;
   private JLabel Question;
   private JTextField AnswerField;
   private JTextField ContactField;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               findPWPage frame = new findPWPage();
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
   public findPWPage() {
      setTitle("find PW");
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setBounds(100, 100, 250, 366);
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

      JLabel Birthday = new JLabel("Birthday");
      Birthday.setBounds(27, 90, 57, 15);
      contentPane.add(Birthday);

      BirthdayField = new JTextField();
      BirthdayField.setBounds(89, 87, 116, 21);
      contentPane.add(BirthdayField);
      BirthdayField.setColumns(10);

      Question = new JLabel("Question");
      Question.setBounds(27, 152, 57, 15);
      contentPane.add(Question);

      JComboBox QuestionCombo = new JComboBox();
      QuestionCombo.setModel(new DefaultComboBoxModel(new String[] { "where are you live?",
            "What game did you play recently?", "What did you do last Christmas?", "What high school are you from?",
            "What's the latest movie you watched?", "Who do you like more, mom or dad?" }));
      QuestionCombo.setToolTipText("");
      QuestionCombo.setBounds(27, 177, 178, 23);
      contentPane.add(QuestionCombo);

      JLabel Answer = new JLabel("Answer");
      Answer.setBounds(27, 210, 57, 15);
      contentPane.add(Answer);

      AnswerField = new JTextField();
      AnswerField.setBounds(27, 235, 178, 21);
      contentPane.add(AnswerField);
      AnswerField.setColumns(10);

      JButton btnNewButton = new JButton("find PW");
      btnNewButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BufferedReader in = null;
            BufferedWriter out = null;
            Socket socket = null;
            Scanner scanner = new Scanner(System.in);

            try {
               socket = new Socket("58.148.64.228", 5518);// �� �ƺ� ������ ��Ʈ
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

               out.write("findPW\n");
               System.out.println("�������� ���������� �����մϴ�");

               out.write(NameField.getText() + "\n");
               out.write(IDField.getText() + "\n");
               out.write(BirthdayField.getText() + "\n");
               out.write(ContactField.getText() + "\n");
               out.write(QuestionCombo.getSelectedItem() + "\n");
               out.write(AnswerField.getText() + "\n");

               out.flush();

               String inputMessage = in.readLine(); // �����κ��� ��� ����

               if (inputMessage.equals("False")) {
                  JOptionPane.showMessageDialog(null, "type it again correctly.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                  NameField.setText("");
                  IDField.setText("");
                  BirthdayField.setText("");
                  ContactField.setText("");
                  AnswerField.setText("");
               } else {
                  JOptionPane.showMessageDialog(null, "PW: " + inputMessage, "find_PW",
                        JOptionPane.INFORMATION_MESSAGE);
               } // server�� ���� ���� ID ����ϱ�

            } catch (IOException f) {
               System.out.println(f.getMessage());
            } finally {
               try {
                  scanner.close();
                  if (socket != null)
                     socket.close(); // Ŭ���̾�Ʈ ���� �ݱ�
               } catch (IOException f) {
                  System.out.println("������ ä�� �� ������ �߻��߽��ϴ�.");
               }
            }

         }
      });
      btnNewButton.setBounds(66, 278, 97, 23);
      contentPane.add(btnNewButton);

      JLabel Contact = new JLabel("Contact");
      Contact.setBounds(27, 121, 57, 15);
      contentPane.add(Contact);

      ContactField = new JTextField();
      ContactField.setColumns(10);
      ContactField.setBounds(89, 121, 116, 21);
      contentPane.add(ContactField);
   }

}