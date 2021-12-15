import java.io.PrintWriter;
import java.net.Socket;

public class ClientInfo {
	public String nickname;
	public Socket socket;
	public PrintWriter thisWriter;
	private GameRoom room=null; 		// ������ ���� ���̴�.
	
	public ClientInfo() {
		
	}
	
	public ClientInfo(String nickname, Socket socket, PrintWriter thisWriter) {
		this.nickname = nickname;
		this.socket = socket;
		this.thisWriter = thisWriter;
	}
	
	
	
	 public void enterRoom(GameRoom gameRoom) {
			//gameRoom.enterUser(this); // �뿡 �����Ų ��
			this.room = gameRoom; // ������ ���� ���� ������ �����Ѵ�.(�߿�)
	}

	 /**
	  * �濡�� ����
	  * @param room ������ ��
	  */
	 public void exitRoom(GameRoom room){
	     this.room = null;
	     // ����ó��(ȭ�鿡 �޼����� �شٴ� ��)
	     // ...
	 }
	 
	 
	
	public String getNickName() {
		return nickname;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public PrintWriter getThisWriter() {
		return thisWriter;
	}
	
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void setThisWriter(PrintWriter thisWriter) {
		this.nickname = nickname;
	}
	
	 public GameRoom getRoom() {
	     return room;
	 }

	 public void setRoom(GameRoom room) {
	     this.room = room;
	 }
	
}
