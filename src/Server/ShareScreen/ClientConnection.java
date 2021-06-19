package Server.ShareScreen;

import Comm.Message;
import Comm.Utils;
import Comm.ShareScreen.VideoPacket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection extends Thread {
    ServerBroadcast serverBroadcast;
    Socket socket;
    String room;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    List<Message> toSend = new ArrayList<>();

    public ClientConnection(ServerBroadcast serverBroadcast, Socket socket, String room) {
        this.serverBroadcast = serverBroadcast;
        this.socket = socket;
        this.room = room;
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            stop();
            e.printStackTrace();
        }
        while (true) {
            try {
                if (socket.getInputStream().available() > 0) {
                    /*BufferedImage image= ImageIO.read(socket.getInputStream());
                     *//*Utils.sleep(20);*//*
                    System.out.println(image);
                    try{
                        ByteArrayOutputStream ous=new ByteArrayOutputStream();
                        ImageIO.write(image,"png",ous);
                        serverBroadcast.addToBroadCastQueue(ous.toByteArray());
                    }catch (IOException e){
                        try {
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        e.printStackTrace();
                    }*/
                    Object obj = ois.readObject();
                    if (obj instanceof Message) {
                        Message message = (Message) obj;
                        if (message.getObject() instanceof VideoPacket) {
                            if (message.getObject() != null) {
                                serverBroadcast.addToBroadCastQueue(message);
                            }
                        }
                    }

                }

                try {
                    if (!toSend.isEmpty()) {
                        Message toClient = toSend.get(0);
                        oos.writeObject(toClient);
                        toSend.remove(toClient);
                    }
                } catch (Throwable t) {
                    if (t instanceof IOException) {//connection closed or connection error
                        throw (Exception) t;
                    } else {//mutex error, try again
                        System.out.println("cc fixmutex");
                        continue;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addToQueue(Message data) {
        try {
            toSend.add(data);
        } catch (Throwable t) {
            Utils.sleep(1);
            addToQueue(data);
        }
    }
}
