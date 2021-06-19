package Server.ServerVoiceChat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerMic {
    ServerSocket serverSocket;
    Map<String , ServerBroadcastMic> map=new HashMap<>();
    public static void main(String[] args) {
        new ServerMic();
    }
    public void addClient(String in, ClientConnectionMic c){
        map.get(in).addToClients(c);
    }
    public ServerMic(){
        try{
            serverSocket=new ServerSocket(4007);
            while (true){
                Socket socket=serverSocket.accept();
                System.out.println("Connected");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DataInputStream dis= new DataInputStream(socket.getInputStream());
                            String rep= dis.readUTF();
                            System.out.println(rep);
                            if(map.containsKey(rep)){
                                ClientConnectionMic cc=new ClientConnectionMic(map.get(rep),socket);
                                addClient(rep,cc);
                                cc.start();
                            }else {
                                ServerBroadcastMic serverBroadcastMic= new ServerBroadcastMic();
                                ClientConnectionMic cc= new ClientConnectionMic(serverBroadcastMic,socket);
                                serverBroadcastMic.addToClients(cc);
                                map.put(rep,serverBroadcastMic);
                                cc.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
