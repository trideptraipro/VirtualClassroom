package Server;

import Comm.RegisterPacket;
import Model.Account;
import Model.UserInfo;
import Model.ModifyAccount;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RegisterServer {
    public static void main(String[] args) {
        new RegisterServer();
    }
    public RegisterServer(){
        try{
            ServerSocket serverSocket=new ServerSocket(5000);
            while (true){
                Socket socket=serverSocket.accept();
                new XuLyRegister(this, socket ).start();
            }
        }catch (Exception e){

        }
    }
}
class XuLyRegister extends Thread{
    RegisterServer rs;
    Socket socket;
    public XuLyRegister(RegisterServer rs, Socket socket){
        this.rs=rs;
        this.socket=socket;
    }
    @Override
    public void run(){
        try{
            while (true){
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                RegisterPacket rp=(RegisterPacket) ois.readObject();
                Account ac= rp.getAccount();
                UserInfo ui= rp.getUserInfo();
                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
                if(ModifyAccount.addAccount(ac,ui)!=0){
                    dos.writeUTF("Accept");
                }else dos.writeUTF("Deny");
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(new Frame(),"Lỗi Sv");
        }
    }
}
