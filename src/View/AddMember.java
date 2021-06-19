package View;

import Comm.Message;
import Comm.UI.UILib;
import Model.Room;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AddMember extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JRadioButton rdbtnID;
    private JRadioButton rdbtnUsername;
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddMember frame = new AddMember();
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
    public AddMember() {
        setTitle("Th\u00EAm th\u00E0nh vi\u00EAn");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 400, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        contentPane.setBackground(UILib.colorBackground);
        setContentPane(contentPane);

        JLabel lblNewLabel = new JLabel("ID");
        lblNewLabel.setFont(UILib.fontLabel);
        lblNewLabel.setBounds(40, 50, 100, 30);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        textField.setBounds(150, 50, 200, 30);
        contentPane.add(textField);
        textField.setColumns(30);

        /*rdbtnID = new JRadioButton("ID");
        rdbtnID.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        rdbtnID.setBounds(67, 70, 103, 21);
        contentPane.add(rdbtnID);

        rdbtnUsername = new JRadioButton("Username");
        rdbtnUsername.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        rdbtnUsername.setBounds(248, 70, 103, 21);
        contentPane.add(rdbtnUsername);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rdbtnID);
        buttonGroup.add(rdbtnUsername);*/

        JButton btnNewButton = new JButton("ADD");
        btnNewButton.setFont(UILib.fontButton);
        btnNewButton.setForeground(UILib.colorButtonForeground);
        btnNewButton.setBackground(UILib.colorButtonBackground);
        btnNewButton.setBounds(260, 110, 90, 30);
        btnNewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buttonClick(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        contentPane.add(btnNewButton);
    }

    private void buttonClick(ActionEvent e) throws IOException {
        Socket socket = new Socket("localhost", 3001);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());
        Message messageInfo = new Message("addtoRoom", room,Integer.parseInt(textField.getText()));
        /*Message message = null;
        if (rdbtnID.isSelected()) {
            message = new Message("addtoRoom", messageInfo, 1);
        } else if (rdbtnUsername.isSelected()) {
            message = new Message("addtoRoom", messageInfo, 0);
        }*/
        out.writeObject(messageInfo);
        String rep = in.readUTF();
        if (rep.equals("true")) {
            JOptionPane.showMessageDialog(new Frame(), "Thêm thành công");
        } else {
            JOptionPane.showMessageDialog(new Frame(), "Thêm thất bại");
        }


        socket.close();
    }
}
