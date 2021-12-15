

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerProcessPartyThread extends Thread{
    private String nickname = null;	//���� �������� �г��� ����
    private Socket socket = null;	//���� ������ ���� ����
    private PrintWriter thisWriter = null;//���� ������ PrintWriter�� �����ϱ� ���� ����
    private ClientInfo thisClientInfo = null;
    private RoomManager roomManager = null;//��������� �����ϴ� �ڿ��� RoomManager
    private GameRoom gameRoom = null;
    List<PrintWriter> listWriters = null;
    List<ClientInfo> clientInfo=null;
    
    BufferedReader buffereedReader = null;
    
    public ChatServerProcessPartyThread(Socket socket, List<PrintWriter> listWriters, List<ClientInfo> clientInfo) {
    	this.socket = socket;
        this.listWriters = listWriters;
        this.clientInfo = clientInfo;
    }

    public ChatServerProcessPartyThread(Socket socket, List<PrintWriter> listWriters, List<ClientInfo> clientInfo, RoomManager roomManager) {
        this.socket = socket;
        this.listWriters = listWriters;
        this.clientInfo = clientInfo;
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        try {
        	buffereedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));	//Ŭ���̾�Ʈ���� �ް�

            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));//Ŭ���̾�Ʈ���� ������

            while(true) {
                String request = buffereedReader.readLine();//���⿡ ���ڿ��� �����

                
                if( request == null) {
                    consoleLog("Ŭ���̾�Ʈ�κ��� ���� ����");
                    doQuit();
                    break;
                }

                String[] tokens = request.split(":");//Ŭ���̾�Ʈ ������ ��ȣ�� 3���� �����µ�
                String[] functionTokens;//�ʴ볪 �ӼӸ��� ���� ����� �����ϱ� ���� tokens�� �ѹ��� ��ũ�������ؾ��� �� ����� �����ϱ� ���� ����
                if("join".equals(tokens[0])) {//join�̸� doJoin ����
                    doJoin(tokens[1], printWriter);
                }
                else if("message".equals(tokens[0])) {//message �̸� doMessage ����
                	functionTokens = tokens[1].split(" ");
                	if(functionTokens[0].equals("/invite")) {
                		invite(functionTokens[1]);
                	}else {
                		doMessage(tokens[1]);
                	}
                }
                else if("quit".equals(tokens[0])) {//quit�̸� doQuit����
                    doQuit();
                }
            }
        }
        catch(IOException e) {
        	doQuit();
            consoleLog(this.nickname + "���� ä�ù��� �������ϴ�.");//������ ���
        }
    }

    private void doJoin(String nickname, PrintWriter writer) {
    	this.nickname = nickname;
    	
    	String data = "join:"+nickname + "���� �����Ͽ����ϴ�.";
    	
    	addWriter(writer);// writer pool�� ����
    	addClient(nickname, socket, writer); //Ŭ���̾�Ʈ�� ������ ����
    	
    	System.out.println(thisClientInfo);
    	System.out.println(clientInfo);
    	
    	broadcast(data);
    	initBroadcast(nickname);
    }
    
    private void addWriter(PrintWriter writer) {
    	synchronized (listWriters) {
    		listWriters.add(writer);
    	}
    	thisWriter = writer;//���� �� �����尡 ������ ���������� �־���
    	System.out.println(listWriters.size());
    }
    
    private void addClient(String nickname, Socket socket, PrintWriter writer) {//Ŭ���̾�Ʈ��ü ����Ʈ�� Ŭ���̾�Ʈ���� ����
    	synchronized(clientInfo){
    		thisClientInfo = new ClientInfo(nickname, socket, writer);
    		clientInfo.add(thisClientInfo);
    		
    		for(int i=0;i<clientInfo.size();i++) {
    			System.out.println(clientInfo.get(i).getNickName()+clientInfo.get(i).getSocket()+clientInfo.get(i).getThisWriter()+clientInfo.get(i));
    		}
    	}
    }
    
    private void doQuit() {
        removeWriter();
        removeClient();

        String data = "quit:"+this.nickname + "���� �����߽��ϴ�.";
        broadcast(data);
    }

    private void removeWriter() {
    	int i=0;
        synchronized (listWriters) {
        		listWriters.remove(thisWriter);
        		for(PrintWriter writer : listWriters) {
        			if(!thisWriter.equals(writer)) {
        				i++;
        			}else{
        				break;
        			}
        			System.out.println(i);
                }
        		for(PrintWriter writer : listWriters) {
        			writer.println("delete:"+nickname);
                    writer.flush(); 
        		}
        }
    }
    
    private void removeClient() {
    	synchronized (clientInfo) {
            clientInfo.remove(thisClientInfo);
        }
    }
    
    private void removeClient(ClientInfo clientInfo) {//removeClient override
    	synchronized(this.clientInfo) {
    		this.clientInfo.remove(clientInfo);
    	}
    }

    private void doMessage(String data) {
        broadcast(this.nickname + ":" + data);
    }

    private void invite(String invitee) {
    		System.out.println(thisClientInfo.getRoom());
    		if(thisClientInfo.getRoom()==null) {
    			gameRoom = roomManager.createRoom(thisClientInfo);//���� �� �����带 ����ϴ� Ŭ���̾�Ʈ�� room�� ���̶�� �׻�� �������� ���� �ϳ� �������
    			listWriters.remove(thisWriter);//�ٸ��濡 �� �÷��̾��� thiswriter�������� ���������ν� �� ����� ������ ����鿡�Ը� ������
    			//buffereedReader = null;//������ �ʴ´�
    			thisClientInfo.getThisWriter().println("invite:");
    			thisClientInfo.getThisWriter().flush();
    		}
    		for(int i=0;i<clientInfo.size();i++) {
    			if(clientInfo.get(i).getNickName().equals(invitee)) {
    				gameRoom.enterUser(clientInfo);//�ش� �濡 ������ ����
    				listWriters.remove(clientInfo.get(i).getThisWriter());////�ٸ��濡 �� �÷��̾��� thiswriter�������� ���������ν� �� ����� ������ ����鿡�Ը� ������
    				clientInfo.get(i).getThisWriter().println("invite:");//�ش� �濡 �� �������� invite��ȣ������ invite��ȣ�� ���� ChatGUI�� ���ο� ��Ƽ RoomGUI�� ����.
    				clientInfo.get(i).getThisWriter().flush();
    				gameRoom.run();
    			}
    		}
    }

    private void broadcast(String data) {
        synchronized (listWriters) {
            for(PrintWriter writer : listWriters) {
                writer.println(data);
                writer.flush();
            }
        }
    }
    
    private void initBroadcast(String nickname) {//inin Broadcast�� GUI�� �÷��̾� ����Ʈ�� �ʱ⿡ ������� �߰���Ű�� ���� �޼ҵ��
    	synchronized (listWriters) {
            for(PrintWriter writer : listWriters) {//��� printwirter�� �ִ� ����Ʈ�� ��ε�ĳ�����Ѵ�.
            	if(thisWriter.equals(writer)) {//���Ӱ� ���� ������״� ���� ������ �ִ� ��� ����� ������ ������
            		for(int i=0;i<clientInfo.size();i++) {//Ŭ���̾�Ʈ ������ �ִ� ��������� ������.
            			writer.println("nickname:"+clientInfo.get(i).getNickName());//Ŭ���̾�Ʈ ������ �ִ� ������� �̸��� ������.
            			writer.flush();
            		}
            	}else {//���� ���� ����� �ƴѰ��
            		writer.println("nickname:"+clientInfo.get(clientInfo.size()-1).getNickName());//�׳� ���Ӱ� ���»���� �߰��� �ش�.
            		writer.flush();
            	}
            }
        }
    }

    private void consoleLog(String log) {
        System.out.println(log);
    }
    
    public List<PrintWriter> getWriterList() {
		return listWriters;
	}
    
    public List<ClientInfo> getClientList() {
		return clientInfo;
	}
}