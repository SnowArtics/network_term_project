import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ChatRoomGUI {
private String name;
	
	private JFrame frame;
	
	private JTextField EnterChat;
	
	private JButton ExitTheGame;	// exit the game button
	private JButton Customizing;	// customizing button
	private JButton GameStart;		// GameStart button
	private JTextArea ChatTextArea;	// Chat viewer
	//private JTextArea List;			// Player viwer List
	private JComboBox Room;			// Room
	private JScrollPane scrollPane;	// ChatTextArea ScrollPane
	private JScrollPane playerScrollPane; //Player View Scroll
	private JList playerList;	//Player List
	
	private DefaultListModel model;	//JList�� ���̴� ���� �������̴�.
	private Socket socket;
	
	int i=5;	// ī��Ʈ�� ���� ����
	Timer time = new Timer();	// ī��Ʈ�� ���� ����
	TimerTask task = null;	// ī��Ʈ�� ���� ����
	
	public ChatRoomGUI(String name, Socket socket) {
		this.name = name;
		this.socket=socket;
		frame = new JFrame();
		EnterChat = new JTextField();
		ExitTheGame = new JButton(new ImageIcon(".\\Image\\\\Exit_the_game.png"));
		GameStart = new JButton(new ImageIcon(".\\Image\\GameStart.png"));
		//List = new JTextArea();
		Room = new JComboBox();
		scrollPane = new JScrollPane();
		ChatTextArea = new JTextArea();
		Customizing = new JButton(new ImageIcon(".\\Image\\Customizing.png"));
		playerScrollPane = new JScrollPane();
		model = new DefaultListModel();
		playerList = new JList(model);
		
		new ChatClientReceiveThread(socket).start();
		
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);//�������ڸ��� RoomGUI���� join�޼����� ������.
			String request = "join:" + name + "\r\n";
			pw.println(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void show() {
		
		
		
		Border lineBorder = BorderFactory.createLineBorder(Color.black, 2);
		
		frame.setBounds(100, 100, 1980, 1080);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		Action action = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(!EnterChat.getText().equals("")) {
					sendMessage();
					ChatTextArea.append(EnterChat.getText());
					EnterChat.setText(null);
				}
			}
		};
		
		EnterChat.setFont(new Font("����", Font.BOLD, 25));
		EnterChat.setText("Please enter your chat");
		EnterChat.setBounds(169, 819, 877, 56);
		frame.getContentPane().add(EnterChat);
		EnterChat.setColumns(20);
		EnterChat.setBorder(lineBorder);
		EnterChat.addActionListener(action);
		
		ExitTheGame.addActionListener(new ActionListener() {//���� ���� ��ư ��������
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "���� ������ �����Ͻðٽ��ϱ�?", "Ȯ��",JOptionPane.OK_CANCEL_OPTION);
				
				if(result == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		ExitTheGame.setBackground(Color.WHITE);
		ExitTheGame.setForeground(Color.WHITE);
		ExitTheGame.setBounds(169, 99, 251, 75);
		ExitTheGame.setBorderPainted(false);
		ExitTheGame.setContentAreaFilled(false);
		frame.getContentPane().add(ExitTheGame);
		
		Customizing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//���⿡ Ŀ���͸���¡ Ŭ������ �Ű����� �׼� ���� ��ư�� ����
			}
		});
		Customizing.setSize(262,77);
		Customizing.setBounds(1213, 817, 264, 75);
		Customizing.setBorderPainted(false);
		Customizing.setContentAreaFilled(false);
		frame.getContentPane().add(Customizing);
		
		GameStart = new JButton(new ImageIcon(".\\Image\\GameStart.png"));	//������ �־���
		GameStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//���ӽ��� ��ư�� ��������
				i=5;
				
				task = new TimerTask() {
					public void run() {
						if(i>-1) {
							ChatTextArea.append(i+"�� �Ŀ� ������ ���۵˴ϴ�.\n");
							i--;
						}else {
							time.cancel();
							task.cancel();
						}
					}
				};
				time.schedule(task,0,1000);
				
			}
		});
		GameStart.setSize(262,77);
		GameStart.setBounds(1526, 815, 261, 77);
		GameStart.setBorderPainted(false);
		GameStart.setContentAreaFilled(false);
		frame.getContentPane().add(GameStart);
		
		
		playerScrollPane.setBounds(1213,272,536,503);
		frame.getContentPane().add(playerScrollPane);
		playerScrollPane.setViewportView(playerList);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setBorder(new LineBorder(Color.BLACK, 2, true));
		playerList.setBackground(Color.WHITE);
		playerList.setBounds(1213, 272, 536, 503);
		
		
		Room.setBackground(Color.WHITE);
		Room.setModel(new DefaultComboBoxModel(new String[] {"Main Waiting Room"}));
		Room.setToolTipText("Main Waiting Room");
		Room.setBounds(1213, 199, 539, 50);
		Room.setFont(new Font("����", Font.BOLD, 25));
		frame.getContentPane().add(Room);
		
		Room = new JComboBox();
		Room.setBackground(Color.WHITE);
		Room.setModel(new DefaultComboBoxModel(new String[] {"Main Waiting Room"}));
		Room.setToolTipText("Main Waiting Room");
		Room.setBounds(1213, 199, 539, 50);
		Room.setFont(new Font("����", Font.BOLD, 25));
		frame.getContentPane().add(Room);

		scrollPane.setBounds(169, 199, 877, 594);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setViewportView(ChatTextArea);
		ChatTextArea.setBackground(Color.WHITE);
		ChatTextArea.setBorder(new LineBorder(Color.BLACK, 2, true));
		
		frame.setVisible(true);
	}
	
	private void sendMessage() {
        PrintWriter pw;
        try {
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);//�޼����� �������� ���� ����Ʈ Writer
            String message = EnterChat.getText();//�ؽ�Ʈ �ʵ忡 �Էµ� �޼����� ����
            String request = "message:" + message + "\r";
            pw.println(request);

            EnterChat.setText("");//�ؽ�Ʈ �ʵ� �ʱ�ȭ
            EnterChat.requestFocus();//JTextField�� focus �ֱ�
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private class ChatClientReceiveThread extends Thread{
        Socket socket = null;

        ChatClientReceiveThread(Socket socket){
            this.socket = socket;//Ŭ���̾�Ʈ���� ���� ������ ���⿡ �־���
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));//�ٸ� Ŭ���̾�Ʈ���� �Է��� �޼����� ����
                while(true) {
                    String msg = br.readLine();
                    String[] tokens = msg.split(":");
                    if("nickname".equals(tokens[0])) {
                    	model.addElement(tokens[1]);
                    }else if("delete".equals(tokens[0])){
                    	//model.removeElementAt(Integer.parseInt(tokens[1]));
                    	model.removeElement(tokens[1]);
                    }
                    else {
                    	ChatTextArea.append(msg);//�ؽ�Ʈ Area�� �߰��Ѵ�
                    	ChatTextArea.append("\n");
                    }
                    
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
