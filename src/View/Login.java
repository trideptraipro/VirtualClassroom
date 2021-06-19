package View;

import Comm.UI.UILib;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Login extends JFrame implements Runnable{

	private JPanel contentPane;
	private JTextField tFusername;
	private JTextField tFpass;
	private Socket socket;
	private Register register;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		try{
			socket=new Socket("localhost",6000);
		}catch (Exception e){

		}
		new Thread(this).start();
		setTitle("VT");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 347, 199);

		contentPane = new JPanel();
		contentPane.setBackground(/*new Color(255, 255, 204)*/UILib.colorBackground);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(UILib.fontText);
		lblNewLabel.setBounds(34, 24, 78, 14);
		contentPane.add(lblNewLabel);
		
		tFusername = new JTextField();
		tFusername.setBounds(122, 21, 157, 20);
		tFusername.setFont(UILib.fontText);
		contentPane.add(tFusername);
		tFusername.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(UILib.fontText);
		lblNewLabel_1.setBounds(34, 62, 78, 14);
		contentPane.add(lblNewLabel_1);
		
		tFpass = new JPasswordField();
		tFpass.setBounds(122, 59, 157, 20);
		contentPane.add(tFpass);
		tFpass.setColumns(10);
		tFpass.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
						dos.writeUTF(tFusername.getText()+"");
						dos.writeUTF(tFpass.getText()+"");
					}catch (Exception e1){

					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		this.register = new Register(this);
		JButton btnLogin = new JButton("Login");
		btnLogin.setForeground(UILib.colorButtonForeground);
		btnLogin.setBackground(UILib.colorButtonBackground);
		btnLogin.setFont(UILib.fontButton);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					dos.writeUTF(tFusername.getText()+"");
					dos.writeUTF(tFpass.getText()+"");


				}catch (Exception e1){

				}
			}
		});
		btnLogin.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
						dos.writeUTF(tFusername.getText()+"");
						dos.writeUTF(tFpass.getText()+"");
					}catch (Exception e1){

					}
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		btnLogin.setBounds(190, 90, 89, 23);
		contentPane.add(btnLogin);
		
		JLabel lblRegister = new JLabel("Create a new account");
		lblRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblRegisterMouseListener(e);
			}
		});
		lblRegister.setForeground(new Color(255, 0, 0));
		lblRegister.setBackground(Color.GRAY);
		lblRegister.setFont(new Font("Forte", Font.PLAIN, 14));
		lblRegister.setBounds(89, 123, 140, 14);
		contentPane.add(lblRegister);
	}

	public JTextField gettFusername() {
		return tFusername;
	}

	@Override
	public void run() {
		while (true){
			try{
				DataInputStream dis=new DataInputStream(socket.getInputStream());
				String rep=dis.readUTF();
				System.out.println(rep);
				if(rep.equals("Accept")) {
					String us=this.tFusername.getText();
					this.setVisible(false);
					Main frame = new Main(this);
					frame.setVisible(true);
				}
				if(rep.equals("Deny")) JOptionPane.showMessageDialog(new Frame(),"Thất Bại");
			}catch (Exception e){

			}
		}

	}

	private void lblRegisterMouseListener(MouseEvent e) {
		this.setVisible(false);
		register.setVisible(true);

	}
}
