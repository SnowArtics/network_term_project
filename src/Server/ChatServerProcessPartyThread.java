

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
    private String nickname = null;	//현재 쓰레드의 닉네임 저장
    private Socket socket = null;	//현재 소켓의 소켓 저장
    private PrintWriter thisWriter = null;//현재 쓰레드 PrintWriter를 저장하기 위한 변수
    private ClientInfo thisClientInfo = null;
    private RoomManager roomManager = null;//쓰레드들이 공유하는 자원인 RoomManager
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
                    new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));	//클라이언트에게 받고

            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));//클라이언트에게 보낸다

            while(true) {
                String request = buffereedReader.readLine();//여기에 문자열이 저장됨

                
                if( request == null) {
                    consoleLog("클라이언트로부터 연결 끊김");
                    doQuit();
                    break;
                }

                String[] tokens = request.split(":");//클라이언트 측에서 신호를 3개를 보내는데
                String[] functionTokens;//초대나 귓속말과 같은 기능을 구현하기 위해 tokens를 한번더 토크나이저해야함 그 결과를 저장하기 위한 변수
                if("join".equals(tokens[0])) {//join이면 doJoin 실행
                    doJoin(tokens[1], printWriter);
                }
                else if("message".equals(tokens[0])) {//message 이면 doMessage 실행
                	functionTokens = tokens[1].split(" ");
                	if(functionTokens[0].equals("/invite")) {
                		invite(functionTokens[1]);
                	}else {
                		doMessage(tokens[1]);
                	}
                }
                else if("quit".equals(tokens[0])) {//quit이면 doQuit실행
                    doQuit();
                }
            }
        }
        catch(IOException e) {
        	doQuit();
            consoleLog(this.nickname + "님이 채팅방을 나갔습니다.");//나갈때 출력
        }
    }

    private void doJoin(String nickname, PrintWriter writer) {
    	this.nickname = nickname;
    	
    	String data = "join:"+nickname + "님이 입장하였습니다.";
    	
    	addWriter(writer);// writer pool에 저장
    	addClient(nickname, socket, writer); //클라이언트의 정보를 저장
    	
    	System.out.println(thisClientInfo);
    	System.out.println(clientInfo);
    	
    	broadcast(data);
    	initBroadcast(nickname);
    }
    
    private void addWriter(PrintWriter writer) {
    	synchronized (listWriters) {
    		listWriters.add(writer);
    	}
    	thisWriter = writer;//현재 이 쓰레드가 누구의 쓰레드인지 넣어줌
    	System.out.println(listWriters.size());
    }
    
    private void addClient(String nickname, Socket socket, PrintWriter writer) {//클라이언트객체 리스트에 클라이언트들을 저장
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

        String data = "quit:"+this.nickname + "님이 퇴장했습니다.";
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
    			gameRoom = roomManager.createRoom(thisClientInfo);//현재 이 쓰레드를 사용하는 클라이언트의 room이 널이라면 그사람 기준으로 방을 하나 만들어줌
    			listWriters.remove(thisWriter);//다른방에 들어간 플레이어의 thiswriter삭제해줌 삭제함으로써 이 사람을 제외한 사람들에게만 보낸다
    			//buffereedReader = null;//읽지도 않는다
    			thisClientInfo.getThisWriter().println("invite:");
    			thisClientInfo.getThisWriter().flush();
    		}
    		for(int i=0;i<clientInfo.size();i++) {
    			if(clientInfo.get(i).getNickName().equals(invitee)) {
    				gameRoom.enterUser(clientInfo);//해당 방에 유저가 들어옴
    				listWriters.remove(clientInfo.get(i).getThisWriter());////다른방에 들어간 플레이어의 thiswriter삭제해줌 삭제함으로써 이 사람을 제외한 사람들에게만 보낸다
    				clientInfo.get(i).getThisWriter().println("invite:");//해당 방에 들어간 유저한테 invite신호를보냄 invite신호를 받은 ChatGUI는 새로운 파티 RoomGUI를 띄운다.
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
    
    private void initBroadcast(String nickname) {//inin Broadcast는 GUI의 플레이어 리스트에 초기에 사람들을 추가시키기 위한 메소드다
    	synchronized (listWriters) {
            for(PrintWriter writer : listWriters) {//모든 printwirter에 있는 리스트에 브로드캐스팅한다.
            	if(thisWriter.equals(writer)) {//새롭게 들어온 사람한테는 현재 접속해 있는 모든 사람의 정보를 보낸다
            		for(int i=0;i<clientInfo.size();i++) {//클라이언트 정보에 있는 모든사람들을 보낸다.
            			writer.println("nickname:"+clientInfo.get(i).getNickName());//클라이언트 정보에 있는 사람들의 이름을 보낸다.
            			writer.flush();
            		}
            	}else {//새로 들어온 사람이 아닌경우
            		writer.println("nickname:"+clientInfo.get(clientInfo.size()-1).getNickName());//그냥 새롭게 들어온사람만 추가해 준다.
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