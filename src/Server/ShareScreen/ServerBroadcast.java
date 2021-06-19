package Server.ShareScreen;

import Comm.Message;
import Comm.Utils;

import java.util.ArrayList;
import java.util.List;

public class ServerBroadcast {
    List<ClientConnection> clientConnectionList =new ArrayList<ClientConnection>();
    List<Message> broadCastQueue= new ArrayList<>();

    public void addToBroadCastQueue(Message data){
        try {
            broadCastQueue.add(data);
        }catch (Throwable t){
            Utils.sleep(1);
            addToBroadCastQueue(data);
        }
    }
    public void addToClient(ClientConnection clientConnection){
        try{
            clientConnectionList.add(clientConnection);
        }catch (Throwable t){
            Utils.sleep(1);
            addToClient(clientConnection);
        }
    }
    public ServerBroadcast(){
        new BroadcastThread().start();
    }
    private class BroadcastThread extends Thread{
        @Override
        public void run(){
            try{
                while (true) {
                    ArrayList<ClientConnection> toRemove = new ArrayList<ClientConnection>(); //create a list of dead connections
                    for (ClientConnection c : clientConnectionList
                    ) {
                        if (!c.isAlive()) { //connection is dead, need to be removed
                                toRemove.add(c);
                        }
                    }
                    clientConnectionList.removeAll(toRemove);
                    if(broadCastQueue.isEmpty()){//Nothing to send
                        Utils.sleep(10); //avoid busy wait
                        continue;
                    }else {//we got something to broadcast
                        Message data=broadCastQueue.get(0);
                        for (ClientConnection c: clientConnectionList //broadcast the data
                             ) {
                            c.addToQueue(data);
                        }
                        broadCastQueue.remove(data);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
