

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
// 1. ���� ���� ����
            serverSocket = new ServerSocket();

// 2. ���ε�
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind( new InetSocketAddress("127.0.0.1", PORT) );
            consoleLog("���� ��ٸ� - " + "127.0.0.1" + ":" + PORT);

// 3. ��û ���
            while(true) {
                Socket socket = serverSocket.accept();	//Ŭ���̾�Ʈ�� ����Ǹ�
                new ChatServerProcessThread(socket, listWriters).start();	//ê ���� ���μ��� �����带 ���� �Ű������δ� �ش� Ŭ���̾�Ʈ�� ���ϰ� Ŭ���̾�Ʈ ������ ����� listWriters�� �Ѱ��ش�.
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