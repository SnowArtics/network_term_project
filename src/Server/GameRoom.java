import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GameRoom {

    private int id; // �� ID
    private List<ClientInfo> userList = new ArrayList<ClientInfo>();
    private List<PrintWriter> printWriter = new ArrayList<PrintWriter>();
    private ClientInfo roomOwner; // ����
    private ClientInfo recentClient;//���� �ֱٿ� ���� user
    private String roomName; // �� �̸�
    private int i=0;

    public GameRoom(int roomId) { // �ƹ��� ���� ���� ������ ��
        this.id = roomId;
        userList = new ArrayList<ClientInfo>();
    }

    public GameRoom(ClientInfo user) { // ������ ���� ���鶧
        user.enterRoom(this);
        userList.add(user); // ������ �߰���Ų ��
        this.roomOwner = user; // ������ ������ �����.
        printWriter.add(userList.get(i).getThisWriter());//printWriter�� ���ʴ�� �߰�����
        i++;
    }

    public GameRoom(List<ClientInfo> users) { // ���� ����Ʈ�� ���� ������
        this.userList = users; // ��������Ʈ ����

        // �� ����
        for(ClientInfo user : users){
            user.enterRoom(this);
        }

        this.roomOwner = userList.get(0); // ù��° ������ �������� ����
    }
    
    public void run() {
    	ChatServerProcessPartyThread thread1 = new ChatServerProcessPartyThread(roomOwner.getSocket(), printWriter, userList);
    	ChatServerProcessPartyThread thread2 = new ChatServerProcessPartyThread(recentClient.getSocket(), printWriter, userList);
    	thread1.start();
    	thread2.start();
    }

    public void enterUser(ClientInfo user) {
        user.enterRoom(this);
        recentClient = user;
        userList.add(user);
        printWriter.add(user.getThisWriter());
    }

    public void enterUser(List<ClientInfo> users) {
        for(ClientInfo gameUser : users){
            gameUser.enterRoom(this);
        }
        userList.addAll(users);
    }

    /**
     * �ش� ������ �濡�� ������
     * @param user ������ ����
     */
    public void exitUser(ClientInfo user) {
        user.exitRoom(this);
        userList.remove(user); // �ش� ������ �濡�� ������

        if (userList.size() < 1) { // ��� �ο��� �� ���� �����ٸ�
            RoomManager.removeRoom(this); // �� ���� �����Ѵ�.
            return;
        }

        if (userList.size() < 2) { // �濡 ���� �ο��� 1�� ���϶��
            this.roomOwner = userList.get(0); // ����Ʈ�� ù��° ������ ������ �ȴ�.
            return;
        }
    }

    /**
     * �ش� ���� ������ �� �����Ű�� ������
     */
    public void close() {
        for (ClientInfo user : userList) {
            user.exitRoom(this);
        }
        this.userList.clear();
        this.userList = null;
    }

    // ���� ����

    /**
     * �ش� byte �迭�� ���� ��� �������� ����
     * @param data ���� data
     */
//   public void broadcast(byte[] data) {
//        for (ClientInfo user : userList) { // �濡 ���� ������ ����ŭ �ݺ�
            // �� �������� �����͸� �����ϴ� �޼��� ȣ��~
            // ex) user.SendData(data);

//         try {
//            user.sock.getOutputStream().write(data); // �̷������� ����Ʈ�迭�� ������.
//         } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//         }
//        }
//    }
    
			/*
			 * private void broadcast(String data) { synchronized (userList) {
			 * for(ClientInfo writer : userList) { writer.getThisWriter().println(data);
			 * writer.getThisWriter().flush(); } } }
			 * 
			 * private void initBroadcast(String nickname) {//inin Broadcast�� GUI�� �÷��̾� ����Ʈ��
			 * �ʱ⿡ ������� �߰���Ű�� ���� �޼ҵ�� synchronized (userList) { for(ClientInfo writer :
			 * userList) {//��� printwirter�� �ִ� ����Ʈ�� ��ε�ĳ�����Ѵ�.
			 * if(recentClient.equals(writer)) {//���Ӱ� ���� ������״� ���� ������ �ִ� ��� ����� ������ ������
			 * for(int i=0;i<userList.size();i++) {//Ŭ���̾�Ʈ ������ �ִ� ��������� ������.
			 * writer.getThisWriter().println("nickname:"+userList.get(i).getNickName());//
			 * Ŭ���̾�Ʈ ������ �ִ� ������� �̸��� ������. writer.getThisWriter().flush(); } }else {//���� ����
			 * ����� �ƴѰ��
			 * writer.getThisWriter().println("nickname:"+userList.get(userList.size()-1).
			 * getNickName());//�׳� ���Ӱ� ���»���� �߰��� �ش�. writer.getThisWriter().flush(); } } }
			 * }
			 */

    public void setOwner(ClientInfo gameUser) {
        this.roomOwner = gameUser; // Ư�� ����ڸ� �������� �����Ѵ�.
    }

    public void setRoomName(String name) { // �� �̸��� ����
        this.roomName = name;
    }

    public ClientInfo getUserByNickName(String nickName) { // �г����� ���ؼ� �濡 ���� ������ ������

        for (ClientInfo user : userList) {
            if (user.getNickName().equals(nickName)) {
                return user; // ������ ã�Ҵٸ�
            }
        }
        return null; // ã�� ������ ���ٸ�
    }

    public ClientInfo getUser(ClientInfo gameUser) { // GameUser ��ü�� get

        int idx = userList.indexOf(gameUser);

        // ������ �����Ѵٸ�(gameUser�� equals�� ��)
        if(idx > 0){
            return userList.get(idx);
        }
        else{
            // ������ ���ٸ�
            return null;
        }
    }

    public String getRoomName() { // �� �̸��� ������
        return roomName;
    }

    public int getUserSize() { // ������ ���� ����
        return userList.size();
    }

    public ClientInfo getOwner() { // ������ ����
        return roomOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public ClientInfo getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(ClientInfo roomOwner) {
        this.roomOwner = roomOwner;
    }
    
    public List getWriterList() {
    	return printWriter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRoom gameRoom = (GameRoom) o;

        return id == gameRoom.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}