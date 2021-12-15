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
	
	private DefaultListModel model;	//JList에 보이는 실제 데이터이다.
	private Socket socket;
	
	int i=5;	// 카운트용 변수 선언
	Timer time = new Timer();	// 카운트용 변수 선언
	TimerTask task = null;	// 카운트용 변수 선언
	
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
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);//시작하자마자 RoomGUI한테 join메세지를 보낸다.
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
		
		EnterChat.setFont(new Font("굴림", Font.BOLD, 25));
		EnterChat.setText("Please enter your chat");
		EnterChat.setBounds(169, 819, 877, 56);
		frame.getContentPane().add(EnterChat);
		EnterChat.setColumns(20);
		EnterChat.setBorder(lineBorder);
		EnterChat.addActionListener(action);
		
		ExitTheGame.addActionListener(new ActionListener() {//게임 종료 버튼 눌렀을때
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "정말 게임을 종료하시겟습니까?", "확인",JOptionPane.OK_CANCEL_OPTION);
				
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
				//여기에 커스터마이징 클래스로 옮겨지는 액션 수행 버튼을 넣자
			}
		});
		Customizing.setSize(262,77);
		Customizing.setBounds(1213, 817, 264, 75);
		Customizing.setBorderPainted(false);
		Customizing.setContentAreaFilled(false);
		frame.getContentPane().add(Customizing);
		
		GameStart = new JButton(new ImageIcon(".\\Image\\GameStart.png"));	//아이콘 넣어줌
		GameStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//게임시작 버튼을 눌렀을때
				i=5;
				
				task = new TimerTask() {
					public void run() {
						if(i>-1) {
							ChatTextArea.append(i+"초 후에 게임이 시작됩니다.\n");
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
		Room.setFont(new Font("굴림", Font.BOLD, 25));
		frame.getContentPane().add(Room);
		
		Room = new JComboBox();
		Room.setBackground(Color.WHITE);
		Room.setModel(new DefaultComboBoxModel(new String[] {"Main Waiting Room"}));
		Room.setToolTipText("Main Waiting Room");
		Room.setBounds(1213, 199, 539, 50);
		Room.setFont(new Font("굴림", Font.BOLD, 25));
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
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);//메세지를 내보내기 위한 프린트 Writer
            String message = EnterChat.getText();//텍스트 필드에 입력된 메세지를 받음
            String request = "message:" + message + "\r";
            pw.println(request);

            EnterChat.setText("");//텍스트 필드 초기화
            EnterChat.requestFocus();//JTextField에 focus 주기
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private class ChatClientReceiveThread extends Thread{
        Socket socket = null;

        ChatClientReceiveThread(Socket socket){
            this.socket = socket;//클라이언트에게 받은 소켓을 여기에 넣어줌
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));//다른 클라이언트들이 입력한 메세지를 받음
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
                    	ChatTextArea.append(msg);//텍스트 Area에 추가한다
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
