package Server;

import Comm.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerChatTCP {
	Map<String,Vector<XuLy>> map; //map quản lí luồng chat với key là tên room, value là các luồng trong room
	/*Vector<XuLy> abc = new Vector<>();//Vector quản lí cách luồng kết nối server*/
	public void addNewRoom(String in,XuLy xuLy){
		try {
			Vector<XuLy> vector= new Vector<>();
			vector.add(xuLy);
			map.put(in, vector);
		}catch (Throwable t){
			addNewRoom(in, xuLy);
			Utils.sleep(10);
		}
	}
	public Map<String,Vector<XuLy>> getMap(){
		return map;
	}
	public void addToMap(String in, XuLy xl){
		try{
			map.get(in).add(xl);
		}catch (Throwable t){
			addToMap(in,xl);
			Utils.sleep(10);
		}
	}
	public static void main(String[] args) {
		new ServerChatTCP();
	}
	public ServerChatTCP() {
		this.map=new HashMap<>();
		try {
			ServerSocket serverSocket=new ServerSocket(4000);
			while(true) {
			    Socket socket=serverSocket.accept();
			    ServerChatTCP serverChatTCP=this;
			    new Thread(new Runnable() {
					@Override
					public void run() {
						String in=null;
						try {
							DataInputStream dis=new DataInputStream(socket.getInputStream());
							in=dis.readUTF();
						} catch (IOException e) {
							e.printStackTrace();
						}

						XuLy handler= null;
						try {
							handler = new XuLy(socket,serverChatTCP,in);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(map.containsKey(in)){
							addToMap(in,handler);
						}else {
							addNewRoom(in,handler);
						}
						handler.start();
					}
				}).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


}

class XuLy extends Thread{
	Socket socket;
	ServerChatTCP server;
	String room;
	DataInputStream dis;
	DataOutputStream dos;
	public XuLy(Socket socket,ServerChatTCP ser, String room) throws IOException {
		this.socket=socket;
		this.server=ser;
		this.room=room;
		this.dis=new DataInputStream(socket.getInputStream());
		this.dos=new DataOutputStream(socket.getOutputStream());
	}
	public void run() {
		try {
			while(true) {
				String name=dis.readUTF();
				String chat=dis.readUTF();
				System.out.println(name+","+chat);
				for (XuLy xl: server.getMap().get(room)
					 ) {
					xl.SendMess(name,chat);
				}
				
			}
				
		} catch (Exception e) {
			server.getMap().get(room).remove(this);
		}
	}
	void SendMess(String name,String chat){
		try {
			DataOutputStream dos=new DataOutputStream(this.socket.getOutputStream());
			dos.writeUTF(name);
			dos.writeUTF(chat);
		}catch (Exception e){
		}
	}
}
