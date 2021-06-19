package Server.ServerVoiceChat;

import Comm.Utils;
import Comm.VoiceChat.MessageMic;
import Comm.VoiceChat.SoundPacket;


import java.net.ServerSocket;
import java.util.ArrayList;


/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 * opens a socket, listens for incoming connections, and creates a
 * ClientConnectionMic for each client. also creates a BroadcastThread that passes
 * messages from the broadcastQueue to all the instances of ClientConnectionMic
 *
 * @author dosse
 */
public class ServerBroadcastMic {
    
    private ArrayList<MessageMic> broadCastQueue = new ArrayList<MessageMic>();
    private ArrayList<ClientConnectionMic> clients = new ArrayList<ClientConnectionMic>();
    private int port;
    

    
    public void addToBroadcastQueue(MessageMic m) { //add a message to the broadcast queue. this method is used by all ClientConnectionMic instances
        try {
            broadCastQueue.add(m);
        } catch (Throwable t) {
            //mutex error, try again
            Utils.sleep(1);
            addToBroadcastQueue(m);
        }
    }
    private ServerSocket s;
    
    public ServerBroadcastMic() {
        new BroadcastThread().start(); //create a BroadcastThread and start it

    }

    public void addToClients(ClientConnectionMic cc) {
        try {
            clients.add(cc); //add the new connection to the list of connections
        } catch (Throwable t) {
            //mutex error, try again
            Utils.sleep(1);
            addToClients(cc);
        }
    }

    /**
     * broadcasts messages to each ClientConnectionMic, and removes dead ones
     */
    private class BroadcastThread extends Thread {
        
        public BroadcastThread() {
        }
        
        @Override
        public void run() {
            for (;;) {
                try {
                    ArrayList<ClientConnectionMic> toRemove = new ArrayList<ClientConnectionMic>(); //create a list of dead connections
                    for (ClientConnectionMic cc : clients) {
                        if (!cc.isAlive()) { //connection is dead, need to be removed
                            SoundPacket.Log.add("dead connection closed: " + cc.getInetAddress() + ":" + cc.getPort() + " on port " + port);
                            toRemove.add(cc);
                        }
                    }
                    clients.removeAll(toRemove); //delete all dead connections
                    if (broadCastQueue.isEmpty()) { //nothing to send
                        Utils.sleep(10); //avoid busy wait
                        continue;
                    } else { //we got something to broadcast
                        MessageMic m = broadCastQueue.get(0);
                        for (ClientConnectionMic cc : clients) { //broadcast the message
                            if (cc.getChId() != m.getChId()) {
                                cc.addToQueue(m);
                            }
                        }
                        broadCastQueue.remove(m); //remove it from the broadcast queue
                    }
                } catch (Throwable t) {
                    //mutex error, try again
                }
            }
        }
    }
}
