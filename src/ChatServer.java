

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    public static final int PORT = 5000;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        List<PrintWriter> listWriters = new ArrayList<PrintWriter>();

        try {
// 1. 서버 소켓 생성
            serverSocket = new ServerSocket();

// 2. 바인딩
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind( new InetSocketAddress("127.0.0.1", PORT) );
            consoleLog("연결 기다림 - " + "127.0.0.1" + ":" + PORT);

// 3. 요청 대기
            while(true) {
                Socket socket = serverSocket.accept();	//클라이언트와 연결되면
                new ChatServerProcessThread(socket, listWriters).start();	//챗 서버 프로세스 스레드를 실행 매개변수로는 해당 클라이언트의 소켓과 클라이언트 정보가 저장될 listWriters를 넘겨준다.
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if( serverSocket != null && !serverSocket.isClosed() ) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void consoleLog(String log) {
        System.out.println("[server " + Thread.currentThread().getId() + "] " + log);
    }
}