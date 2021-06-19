package View;

import Comm.Message;
import Comm.UI.UILib;
import Model.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Main extends JFrame {

	private JPanel contentPane;
	private Login login;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JPanel panel2;
	private JScrollPane scrollPane2;
	private JLabel lblNewLabel;
	private JLabel lblNewlabel2;
	private JButton buttonRefresh;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main(new Login());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login getLogin() {
		return login;
	}

	/**
	 * Create the frame.
	 */
	public Main(Login login) {
		this.login=login;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.setVisible(true);
			}
		});
		setTitle("VT");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 770, 620);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setBackground(UILib.colorBackground);
		setContentPane(contentPane);
		
		JButton btnInfo = new JButton("Th\u00F4ng tin c\u00E1 nh\u00E2n");
		btnInfo.setFont(UILib.fontButton);
		btnInfo.setBounds(25, 20, 150, 30);
		btnInfo.setForeground(UILib.colorButtonForeground);
		btnInfo.setBackground(UILib.colorButtonBackground);
		contentPane.add(btnInfo);
		btnInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Information information= new Information(login);
				information.setVisible(true);
			}
		});

		buttonRefresh= new JButton("REFESH");
		buttonRefresh.setFont(UILib.fontButton);
		buttonRefresh.setBounds(575, 20, 150, 30);;
		buttonRefresh.setForeground(UILib.colorButtonForeground);
		buttonRefresh.setBackground(UILib.colorButtonBackground);
		contentPane.add(buttonRefresh);
		buttonRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadMyRoom();
				loadRoom();
			}
		});
		
		JButton btnNewButton_1 = new JButton("Thêm phòng");
		btnNewButton_1.setFont(UILib.fontButton);
		btnNewButton_1.setBounds(300, 20, 150, 30);
		btnNewButton_1.setForeground(UILib.colorButtonForeground);
		btnNewButton_1.setBackground(UILib.colorButtonBackground);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRoom();
			}
		});
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Tho\u00E1t");
		btnNewButton_2.setFont(UILib.fontButton);
		btnNewButton_2.setForeground(UILib.colorButtonForeground);
		btnNewButton_2.setBackground(UILib.colorButtonBackground);
		btnNewButton_2.setBounds(625, 530, 100, 30);
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				login.setVisible(true);
			}
		});
		contentPane.add(btnNewButton_2);
		//My room
		scrollPane = new JScrollPane();
		scrollPane.setBounds(400, 120, 325, 400);
		contentPane.add(scrollPane);
		lblNewLabel = new JLabel("MY CLASSROOM");
		lblNewLabel.setFont(UILib.fontLabel);
		lblNewLabel.setBounds(400, 80, 200, 30);
		contentPane.add(lblNewLabel);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(0,3,10,10));
		panel.setBackground(new Color(255,255,255));
		scrollPane.setViewportView(panel);
		//The room that we joined
		scrollPane2=new JScrollPane();
		scrollPane2.setBounds(25,120,325,400);
		contentPane.add(scrollPane2);
		panel2=new JPanel();
		panel2.setLayout(new GridLayout(0,3,10,10));
		panel2.setBackground(new Color(255,255,255));
		scrollPane2.setViewportView(panel2);
		lblNewlabel2 = new JLabel("THE JOINED ROOMS");
		lblNewlabel2.setFont(UILib.fontLabel);
		lblNewlabel2.setBounds(25, 80, 200, 30);
		contentPane.add(lblNewlabel2);
		loadRoom();
		loadMyRoom();
		
	}


	public void loadMyRoom(){
		try{
			this.panel.removeAll();
			Socket socket=new Socket("localhost", 3001);
			Message message= new Message("getRoombyAdminName",login.gettFusername().getText());
			ObjectOutputStream oos= new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			List<Room> list=(List<Room>)ois.readObject();
			for (Room r:list
				 ) {
				Button button=new Button(r.getRoomname());
				button.setBackground(UILib.colorButtonBackground);
				button.setForeground(UILib.colorButtonForeground);
				button.setFont(UILib.fontButton);
				this.panel.add(button);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						RoomDisplay rd=new RoomDisplay(r);
						rd.setVisible(true);
						Component component = (Component) e.getSource();
						Main frame = (Main) SwingUtilities.getRoot(component);
						rd.setMain(frame);
					}
				});
			}
			socket.close();
		}catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	private void loadRoom(){
		try {
			Socket socket=new Socket("localhost",3001);
			Message message = new Message("getRoom",login.gettFusername().getText());
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
		List<Room> lr= (List<Room>) ois.readObject();
		for (Room r:lr
			 ) {
			Button button= new Button(r.getRoomname());
			button.setSize(40,40);
			button.setBackground(UILib.colorButtonBackground);
			button.setForeground(UILib.colorButtonForeground);
			button.setFont(UILib.fontButton);
			this.panel2.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					RoomDisplay rd=new RoomDisplay(r);
					rd.setVisible(true);
					rd.mnNewMenu.setVisible(false);
					Component component = (Component) e.getSource();
					Main frame = (Main) SwingUtilities.getRoot(component);
					rd.setMain(frame);
				}
			});
		}
		}catch (IOException | ClassNotFoundException e){
			e.printStackTrace();


		}
	}
	private void addRoom(){
		this.setVisible(false);
		new CreateRoom(this).setVisible(true);
	}

}
