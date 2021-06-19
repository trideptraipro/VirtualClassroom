package View;

import Comm.RegisterPacket;
import Comm.UI.UILib;
import Model.Account;
import Model.UserInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Register extends JFrame implements Runnable{

	private JPanel contentPane;
	private JTextField tFname;
	private JTextField tFphone;
	private JTextField tFaddress;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextField tFuser;
	private JTextField tFpass;
	private JButton btnRegister;
	private JPanel panel_1;
	private Socket socket;
	private Login login;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register(new Login());
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
	private void reset(){
		this.tFname.setText("");
		this.tFphone.setText("");
		this.tFaddress.setText("");
		this.tFuser.setText("");
		this.tFpass.setText("");
	}
	public Register(Login login) {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				reset();
				login.setVisible(true);
			}
		});

		try{
			socket=new Socket("localhost",5000);
		}catch (Exception e){

		}
		setTitle("Đăng kí");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 400, 320);
		contentPane = new JPanel();
		contentPane.setBackground(UILib.colorBackground);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		new Thread(this).start();
		
		JPanel panel = new JPanel();
		panel.setBackground(UILib.colorBackground);
		panel.setBounds(0, 150, 400, 80);
		panel.setLayout(null);
		contentPane.add(panel);
		
		lblNewLabel_3 = new JLabel("UserName");
		lblNewLabel_3.setFont(UILib.fontLabel);
		lblNewLabel_3.setBounds(50, 0, 90, 15);
		panel.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("Password");
		lblNewLabel_4.setFont(UILib.fontLabel);
		lblNewLabel_4.setBounds(50, 40, 150, 20);
		panel.add(lblNewLabel_4);
		
		tFuser = new JTextField();
		tFuser.setBounds(190, 0, 150, 20);
		panel.add(tFuser);
		tFuser.setColumns(10);
		
		tFpass = new JPasswordField();
		tFpass.setBounds(190, 40, 150, 20);
		panel.add(tFpass);
		tFpass.setColumns(10);
		
		btnRegister = new JButton("Register");
		btnRegister.setForeground(UILib.colorButtonForeground);
		btnRegister.setBackground(UILib.colorButtonBackground);
		btnRegister.setFont(UILib.fontButton);
		btnRegister.setBounds(210, 230, 130, 30);
		contentPane.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					oos=new ObjectOutputStream(socket.getOutputStream());
					Account ac=new Account(tFuser.getText(),tFpass.getText());
					UserInfo ui=new UserInfo(tFuser.getText(),tFname.getText(),Integer.parseInt(tFphone.getText()),tFaddress.getText());
					RegisterPacket rp=new RegisterPacket(ac,ui);
					oos.writeObject(rp);
				}catch (Exception e1){
					JOptionPane.showMessageDialog(new Frame(),"Lỗi Cl");
				}
			}
		});
		
		panel_1 = new JPanel();
		panel_1.setBackground(UILib.colorBackground);
		panel_1.setBounds(0, 0, 400, 150);
		panel_1.setLayout(null);
		contentPane.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setFont(UILib.fontLabel);
		lblNewLabel.setBounds(50, 30, 90, 20);
		panel_1.add(lblNewLabel);
		
		tFname = new JTextField();
		tFname.setBounds(190, 30, 150, 20);
		panel_1.add(tFname);
		tFname.setColumns(30);
		
		tFaddress = new JTextField();
		tFaddress.setBounds(190, 110, 150, 20);
		panel_1.add(tFaddress);
		tFaddress.setColumns(30);
		
		JLabel lblNewLabel_2 = new JLabel("Address");
		lblNewLabel_2.setFont(UILib.fontLabel);
		lblNewLabel_2.setBounds(50, 110, 90, 20);
		panel_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Phone Number");
		lblNewLabel_1.setFont(UILib.fontLabel);
		lblNewLabel_1.setBounds(50, 70, 130, 20);
		panel_1.add(lblNewLabel_1);
		
		tFphone = new JTextField();
		tFphone.setBounds(190, 70, 150, 20);
		panel_1.add(tFphone);
		tFphone.setColumns(30);
	}

	@Override
	public void run() {
		while (true){
			try{
				DataInputStream dis=new DataInputStream(socket.getInputStream());
				String rep=dis.readUTF();
				System.out.println(rep);
				if(rep.equals("Accept")) {
					JOptionPane.showMessageDialog(new Frame(),"Thêm thành công");
				}
				if(rep.equals("Deny")) JOptionPane.showMessageDialog(new Frame(),"Thêm thất bại");
			}catch (Exception e){

			}
		}
	}
}
