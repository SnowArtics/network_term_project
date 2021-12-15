

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientApp {
    private static final String SERVER_IP = "127.0.0.1";//서버의 IP 설정
    private static final int SERVER_PORT = 5000;	//서버의 port 설정

    public static void main(String[] args) {
        String name = null;	//닉네임 설정
        Scanner scanner = new Scanner(System.in);//키보드 입력값 받음

        while( true ) {

            System.out.println("대화명을 입력하세요.");//닉네임 설정
            System.out.print(">>> ");
            name = scanner.nextLine();//닉네임 부여

            if (name.isEmpty() == false ) {
                break;
            }

            System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
        }

        scanner.close();//

        Socket socket = new Socket();
        try {
            socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT) );//해당하는 IP와 PORT로 서버에 접속
            consoleLog("채팅방에 입장하였습니다.");
            new ChatGUI(name, socket).show(); //ChatGUI를 불러옴

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            String request = "join:" + name + "\r\n";
            pw.println(request);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void consoleLog(String log) {
        System.out.println(log);
    }
}