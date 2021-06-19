package Server;

import Model.Account;
import Model.ModifyAccount;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer {
    public static void main(String[] args) {
        new LoginServer();
    }
    LoginServer(){
        try{
            ServerSocket serverSocket=new ServerSocket(6000);
            while (true){
                Socket socket=serverSocket.accept();
                new XuLyLogin(this, socket ).start();
            }
        }catch (Exception e){

        }
    }
}

class XuLyLogin extends Thread{
    LoginServer server;
    Socket socket;
    XuLyLogin(LoginServer server, Socket soc){
        this.server=server;
        this.socket=soc;
    }

    @Override
    public void run() {
        try{
            while (true){
                DataInputStream dis=new DataInputStream(socket.getInputStream());
                String user=dis.readUTF();
                String pass=dis.readUTF();
                System.out.println(user+pass);
                Account ac= new Account(user,pass);
                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
                if(ModifyAccount.checkAccount(ac)){
                    dos.writeUTF("Accept");
                }else dos.writeUTF("Deny");
            }
        }catch (Exception e){

        }
    }
}
