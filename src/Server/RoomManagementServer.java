package Server;

import Comm.Message;
import Model.ModifyAccount;
import Model.ModifyRoom;
import Model.Room;
import Model.UserInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class RoomManagementServer {
    List<Room> list = null;
    List<Room> mylist = null;

    public static void main(String[] args) {
        new RoomManagementServer();
    }

    public RoomManagementServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(3001);
            while (true) {
                Socket socket = serverSocket.accept();
                new XuLyRoom(this, socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class XuLyRoom extends Thread {
    RoomManagementServer server;
    Socket socket;

    XuLyRoom(RoomManagementServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            String mess = message.getMess();
            switch (mess) {
                case "create":
                    Room room = (Room) message.getObject();
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    if (ModifyRoom.addRoom(room) != 0) {
                        dos.writeUTF("true");
                    } else {
                        dos.writeUTF("false");
                    }
                    break;
                case "getRoombyAdminName":
                    String username = (String) message.getObject();
                    List<Room> list = ModifyRoom.getListRoomByUsernameAdmin(username);
                    if (list.isEmpty()) {
                        System.out.println("xxx");
                    }
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(list);
                    break;
                case "getRoom":
                    String username1 = (String) message.getObject();
                    List<Room> l = ModifyRoom.getListRoomByUser(username1);
                    ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
                    oos1.writeObject(l);
                    break;
                case "addtoRoom":
                    int id = message.getcID();
                    System.out.println(id);
                    Room room1 = (Room) message.getObject();
                    System.out.println(room1.getId());
                    DataOutputStream dos1 = new DataOutputStream(socket.getOutputStream());
                    if (ModifyRoom.AddMembertoRoombyID(id, room1) != 0) {
                        dos1.writeUTF("true");
                    } else {
                        dos1.writeUTF("false");
                    }
                    break;
                case "getUI":
                    String user=(String)message.getObject();
                    UserInfo userInfo= ModifyAccount.getUserInfoByUserName(user);
                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(userInfo);
                    break;
                default:
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}