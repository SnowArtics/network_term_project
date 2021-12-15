import java.io.PrintWriter;
import java.net.Socket;

public class ClientInfo {
	public String nickname;
	public Socket socket;
	public PrintWriter thisWriter;
	private GameRoom room=null; 		// 유저가 속한 룸이다.
	
	public ClientInfo() {
		
	}
	
	public ClientInfo(String nickname, Socket socket, PrintWriter thisWriter) {
		this.nickname = nickname;
		this.socket = socket;
		this.thisWriter = thisWriter;
	}
	
	
	
	 public void enterRoom(GameRoom gameRoom) {
			//gameRoom.enterUser(this); // 룸에 입장시킨 후
			this.room = gameRoom; // 유저가 속한 방을 룸으로 변경한다.(중요)
	}

	 /**
	  * 방에서 퇴장
	  * @param room 퇴장할 방
	  */
	 public void exitRoom(GameRoom room){
	     this.room = null;
	     // 퇴장처리(화면에 메세지를 준다는 등)
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
