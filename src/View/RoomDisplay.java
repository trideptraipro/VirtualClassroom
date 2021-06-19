package View;


import Comm.*;
import Comm.ShareScreen.TakeScreen;
import Comm.ShareScreen.VideoPacket;
import Comm.UI.UILib;
import Comm.VoiceChat.ClientMic;
import Comm.VoiceChat.MicThread;
import Comm.VoiceChat.SoundPacket;
import Model.Room;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class RoomDisplay extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private Socket socketSS;
	private Socket socketChat;
	private Socket socketVoice;
	private Room room;
	private JPanel panel;
	private MicTester micTester;
	private JProgressBar micLev;
	private JSlider micVol;
	public static JMenu mnNewMenu;
	private DataOutputStream dosChat;
	private DataInputStream disChat;
	private Main main;
	private JTextArea textArea;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private MicThread micThread;
	private ClientMic clientMic;
	private ImageIcon imageIc;
	/**
	 *
	 */
	public void setMain(Main main){
		this.main=main;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoomDisplay frame = new RoomDisplay(new Room());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RoomDisplay(Room room) {
		this.room=room;
		try {
			socketVoice= new Socket("localhost", 4007);
			DataOutputStream dos= new DataOutputStream(socketVoice.getOutputStream());
			dos.writeUTF(Integer.toString(room.getId()));
			toServer= new ObjectOutputStream(socketVoice.getOutputStream());
			fromServer= new ObjectInputStream(socketVoice.getInputStream());
			clientMic=new ClientMic(socketVoice,fromServer);
			clientMic.start();
		}catch (IOException e){
			e.printStackTrace();
		}
		try{
			socketSS=new Socket("localhost", 4001);
			Integer in=room.getId();
			DataOutputStream dos=new DataOutputStream(socketSS.getOutputStream());
			dos.writeUTF(in.toString());
			oos=new ObjectOutputStream(socketSS.getOutputStream());
			ois=new ObjectInputStream(socketSS.getInputStream());
		}catch (IOException e){
			e.printStackTrace();
		}
		try {
			socketChat= new Socket("localhost", 4000);
			disChat= new DataInputStream(socketChat.getInputStream());
			dosChat= new DataOutputStream(socketChat.getOutputStream());
			Integer idroom=room.getId();
			dosChat.writeUTF(idroom.toString());
		}catch (IOException e){
			e.printStackTrace();
		}
		initComponents();
		micTester=new MicTester();
		micTester.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					while (true) {
						Message object=(Message)ois.readObject();
						if(object.getObject() instanceof VideoPacket){
							VideoPacket videoPacket=(VideoPacket) object.getObject();
							ByteArrayInputStream bais = new ByteArrayInputStream(videoPacket.getData());
							BufferedImage img = ImageIO.read(bais);
							System.out.println("nhận"+img);
							panel.getGraphics().drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), null);
						}
						Utils.sleep(10);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						String name=disChat.readUTF();
						String chat=disChat.readUTF();
						if(chat.equals("RaiseHand")){
							textArea.append(name+" MUỐN PHÁT BIỂU\n");
						}else {
							textArea.append(name+":"+chat+"\n");
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();
	}
	private class MicTester extends Thread{//Luong Test mic, the hien qua micLev
		private TargetDataLine mic = null;
		public MicTester() {

		}
		@Override
		public void run() {

			try {
				AudioFormat af = SoundPacket.defaultFormat;
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, null);
				mic = (TargetDataLine) (AudioSystem.getLine(info));
				mic.open(af);
				mic.start();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(rootPane,"Microphone not detected.\nPress OK to close this program", "Error",JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			for (;;) {
				Utils.sleep(10);
				if(mic.available()>0){
					byte[] buff=new byte[SoundPacket.defaultDataLenght];
					mic.read(buff,0,buff.length);
					long tot=0;
					for(int i=0;i<buff.length;i++) tot+= MicThread.amplification*Math.abs(buff[i]);
					tot*=2.5;
					tot/=buff.length;
					micLev.setValue((int)tot);
				}
			}
		}
		private void close(){
			if(mic!=null) mic.close();
			stop();
		}
	}
	private void initComponents(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(micTester.isAlive()){
					micTester.close();
				}
				if (micThread.isAlive()){
					micThread.close();
				}
				Utils.sleep(100);
				clientMic.stop();

			}
		});
		setForeground(new Color(255, 255, 204));
		setTitle(room.getRoomname());
		setAlwaysOnTop(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(	50, 50, 1512, 785);
		setResizable(false);
		getContentPane().setLayout(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		mnNewMenu = new JMenu("Admin setting");
		mnNewMenu.setFont(UILib.fontText);
		menuBar.add(mnNewMenu);


		JMenuItem mntmNewMenuItem = new JMenuItem("Start/Stop share screen");
		mntmNewMenuItem.setFont(UILib.fontText);
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jMenuItemActionPerformed(e);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Add member");
		mntmNewMenuItem_1.setFont(UILib.fontText);
		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnNewMenu.add(mntmNewMenuItem_1);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jMenuItem1ActionPerformed(e);
			}
		});


		JMenu mnNewMenu_1 = new JMenu("Setting");
		mnNewMenu_1.setFont(UILib.fontText);
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("On/Off Microphone");
		mntmNewMenuItem_2.setFont(UILib.fontText);
		mntmNewMenuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jMenuItem2ActionPerformed(e);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setBackground(UILib.colorBackground);
		setContentPane(contentPane);

		panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 10, 1111, 646);
		contentPane.add(panel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(1131, 10, 348, 646);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		textField.setBounds(1131, 662, 271, 52);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						if(!textField.getText().equals("")){
							String username= main.getLogin().gettFusername().getText();
							String mess=textField.getText();
							dosChat.writeUTF(username);
							dosChat.writeUTF(mess);
							textField.setText("");
						}
					}catch (Exception e1){
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBackground(UILib.colorButtonBackground);
		btnNewButton.setForeground(UILib.colorButtonForeground);
		btnNewButton.setFont(UILib.fontButton);
		btnNewButton.setBounds(1401, 662, 78, 52);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					clickSendButton(e);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		contentPane.add(btnNewButton);

		micLev = new JProgressBar();
		micLev.setBounds(130, 696, 248, 21);
		contentPane.add(micLev);

		micVol = new JSlider();
		micVol.setBounds(130, 675, 248, 22);
		contentPane.add(micVol);
		micVol.setMaximum(300);
		micVol.setMinimum(50);
		micVol.setValue(100);
		micVol.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				micVolStateChanged(evt);
			}
		});

		JLabel lblNewLabel = new JLabel("Mic Volume");
		lblNewLabel.setFont(UILib.fontText);
		lblNewLabel.setBounds(61, 681, 59, 13);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Mic Test");
		lblNewLabel_1.setFont(UILib.fontText);
		lblNewLabel_1.setBounds(61, 707, 59, 13);
		contentPane.add(lblNewLabel_1);

		JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setBounds(1078, 662, 43, 52);
		BufferedImage img = null;
		File file=new File("C:\\Users\\XV\\Downloads\\eclipse-workspace\\VT\\src\\Image\\Raised_Hand_Emoji_Icon_ios10_grande.png");
		//To get path go Image/Raised... Click right mouse choose File Path
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(btnNewButton_1.getWidth(), btnNewButton_1.getHeight(),
				Image.SCALE_SMOOTH);
		imageIc=new ImageIcon(dimg);
		btnNewButton_1.setIcon(imageIc);
		/*btnNewButton_1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Raised_Hand_Emoji_Icon_ios10_grande.png"))
				.getImage().getScaledInstance(btnNewButton_1.getWidth(), btnNewButton_1.getHeight(), Image.SCALE_SMOOTH)));*/
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username= main.getLogin().gettFusername().getText();
				String mess="RaiseHand";
				try {
					dosChat.writeUTF(username);
					dosChat.writeUTF(mess);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		contentPane.add(btnNewButton_1);
	}

	private void clickSendButton(ActionEvent e) throws IOException {//click to Gửi
		if(!textField.getText().equals("")){
			String username= main.getLogin().gettFusername().getText();
			String mess=textField.getText();
			dosChat.writeUTF(username);
			dosChat.writeUTF(mess);
			textField.setText("");
		}
	}

	private void jMenuItem1ActionPerformed(ActionEvent e) {
		AddMember am=new AddMember();
		am.setRoom(room);
		am.setVisible(true);
	}

	boolean isMicStart=false;
	private void jMenuItem2ActionPerformed(ActionEvent e) throws IOException {
		//On/Off mic
		micTester.close();
		try {
			Utils.sleep(100); //wait for the GUI microphone Main to release the microphone
			micThread = new MicThread(toServer);  //creates a MicThread that sends microphone data to the server
			micThread.start(); //starts the MicThread
		} catch (Exception ie) { //error acquiring microphone. causes: no microphone or microphone busy
			System.out.println("mic unavailable " + ie);
		}
	}

	boolean isStart=false;
	private void jMenuItemActionPerformed(ActionEvent e) throws IOException {
		if (!isStart) {
			isStart = true;
			try{
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							while (isStart) {
								VideoPacket videoPacket=TakeScreen.getScreen();
								Message message= new Message("",videoPacket);
								oos.writeObject(message);
								Utils.sleep(20);
							}
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e);
						}
						isStart = false;
					}
				}).start();
			}catch (Exception ie){
				ie.printStackTrace();
			}

			JOptionPane.showMessageDialog(this, "Started.");
		} else {
			isStart = false;
		}
	}

	private void micVolStateChanged(ChangeEvent evt) {//Event tang giam am luong
		micThread.amplification=((double)(micVol.getValue()))/100.0;
	}
}
