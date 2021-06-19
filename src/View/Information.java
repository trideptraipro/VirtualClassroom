package View;

import Comm.Message;
import Comm.UI.UILib;
import Model.UserInfo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Information extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldID;
	private JTextField textFieldName;
	private JTextField textFieldPhone;
	private JTextField textFieldAddress;
	private Login login;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	//Method
	public void setLogin(Login login){
		this.login=login;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Information frame = new Information(new Login());
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
	public Information(Login login) {
		this.login=login;
		try{
			socket=new Socket("localhost",3001);
		}catch (IOException e){
			e.printStackTrace();
		}

		setTitle("Information");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 368, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setBackground(UILib.colorBackground);
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setFont(UILib.fontLabel);
		lblNewLabel.setBounds(33, 27, 45, 13);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setFont(UILib.fontLabel);
		lblNewLabel_1.setBounds(33, 53, 45, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("PhoneNumber");
		lblNewLabel_2.setFont(UILib.fontLabel);
		lblNewLabel_2.setBounds(33, 76, 76, 13);
		contentPane.add(lblNewLabel_2);
		
		textFieldID = new JTextField();
		textFieldID.setEditable(false);
		textFieldID.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldID.setBounds(106, 24, 204, 19);
		contentPane.add(textFieldID);
		textFieldID.setColumns(30);
		
		textFieldName = new JTextField();
		textFieldName.setEditable(false);
		textFieldName.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldName.setBounds(106, 50, 204, 19);
		contentPane.add(textFieldName);
		textFieldName.setColumns(30);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setEditable(false);
		textFieldPhone.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldPhone.setBounds(106, 73, 204, 19);
		contentPane.add(textFieldPhone);
		textFieldPhone.setColumns(30);
		
		JLabel lblNewLabel_3 = new JLabel("Address");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblNewLabel_3.setBounds(33, 99, 63, 13);
		contentPane.add(lblNewLabel_3);
		
		textFieldAddress = new JTextField();
		textFieldAddress.setEditable(false);
		textFieldAddress.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldAddress.setBounds(106, 99, 204, 19);
		contentPane.add(textFieldAddress);
		textFieldAddress.setColumns(30);
		loadUI();
	}

	private void loadUI() {
		Message message= new Message();
		message.setMess("getUI");
		message.setObject(login.gettFusername().getText());
		try {
			oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			ois=new ObjectInputStream(socket.getInputStream());
			UserInfo ui=(UserInfo) ois.readObject();
			this.textFieldID.setText(Integer.toString(ui.getId()));
			this.textFieldName.setText(ui.getName());
			this.textFieldAddress.setText(ui.getAddress());
			this.textFieldPhone.setText(Integer.toString(ui.getPhonenumber()));
		}catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
}
