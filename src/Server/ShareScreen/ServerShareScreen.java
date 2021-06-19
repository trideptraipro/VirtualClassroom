package Server.ShareScreen;

import Comm.Utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerShareScreen {
    public static void main(String[] args) {
        new ServerShareScreen(4001);
    }
    /*List<ClientConnect> list;//tạo hashmap*/
    Map<String, ServerBroadcast> map=new HashMap<>();//1 map với key la tên phòng, value là list các luồng xử lý socket
    int port;
    public Map<String, ServerBroadcast> getMap() {
        return map;
    }
    public void addToClients(String in, ClientConnection clientConnectionConnect){
        map.get(in).addToClient(clientConnectionConnect);
    }
    public void  addToServerBroadcast(String in, ServerBroadcast c){
        try {
            map.put(in,c);
        }catch (Throwable t){
            Utils.sleep(1);
            addToServerBroadcast(in,c);
        }
    }

    /*public List<ClientConnect> getList() {
        return list;
    }*/

    ServerShareScreen(int port){
        try {
            this.port=port;
            ServerSocket serverSocket=new ServerSocket(port);
            while (true){
                Socket socket=serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataInputStream dis= null;
                        try {
                            dis = new DataInputStream(socket.getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String in= null;
                        try {
                            in = dis.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ClientConnection clientConnectionConnect =null;
                        if(map.containsKey(in)){
                            clientConnectionConnect =new ClientConnection(map.get(in),socket,in);
                            addToClients(in, clientConnectionConnect);
                        }else {
                            ServerBroadcast serverBroadcast=new ServerBroadcast();
                            clientConnectionConnect =new ClientConnection(serverBroadcast,socket,in);
                            serverBroadcast.addToClient(clientConnectionConnect);
                            addToServerBroadcast(in,serverBroadcast);
                        }
                        /*list.add(clientConnectionConnect);*/
                        clientConnectionConnect.start();
                    }
                }).start();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
