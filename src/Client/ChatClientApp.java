

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientApp {
    private static final String SERVER_IP = "127.0.0.1";//������ IP ����
    private static final int SERVER_PORT = 5000;	//������ port ����

    public static void main(String[] args) {
        String name = null;	//�г��� ����
        Scanner scanner = new Scanner(System.in);//Ű���� �Է°� ����

        while( true ) {

            System.out.println("��ȭ���� �Է��ϼ���.");//�г��� ����
            System.out.print(">>> ");
            name = scanner.nextLine();//�г��� �ο�

            if (name.isEmpty() == false ) {
                break;
            }

            System.out.println("��ȭ���� �ѱ��� �̻� �Է��ؾ� �մϴ�.\n");
        }

        scanner.close();//

        Socket socket = new Socket();
        try {
            socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT) );//�ش��ϴ� IP�� PORT�� ������ ����
            consoleLog("ä�ù濡 �����Ͽ����ϴ�.");
            new ChatGUI(name, socket).show(); //ChatGUI�� �ҷ���

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