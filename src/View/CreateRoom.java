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
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CreateRoom extends JFrame  {
    private Main main;
    private JButton btnOK;
    private JLabel label;
    private JTextField textField;
    private Socket socket;
    private JPanel contentPane;

    public Main getMain() {
        return main;
    }

    public static void main(String[] args) {
        new CreateRoom(new Main(new Login())).setVisible(true);
    }
    public CreateRoom(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getMain().setVisible(true);
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setTitle("Tạo room");
        setLayout(null);
        Font font = new Font("Bookman Old Style",Font.PLAIN, 14);

        label=new JLabel("Tên phòng");
        label.setBounds(50,50,70,30);
        label.setFont(font);
        this.add(label);

        textField=new JTextField();
        textField.setFont(font);
        textField.setBounds(150, 50, 200, 30);
        this.add(textField);
        btnOK=new JButton("OK");
        btnOK.setBackground(UILib.colorButtonBackground);
        btnOK.setForeground(UILib.colorButtonForeground);
        btnOK.setBounds(150,150, 70, 30);
        btnOK.setFont(font);
        this.add(btnOK);
    }
    public CreateRoom(Main main){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getMain().setVisible(true);
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        this.main=main;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 200);
        setTitle("Tạo room");
        setLayout(null);
        Font font = new Font("Bookman Old Style",Font.PLAIN, 14);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        contentPane.setBackground(UILib.colorBackground);
        setContentPane(contentPane);
        label=new JLabel("TÊN PHÒNG");
        label.setBounds(40,60,100,30);
        label.setFont(font);
        contentPane.add(label);

        textField=new JTextField();
        textField.setFont(font);
        textField.setBounds(160, 60, 200, 30);
        contentPane.add(textField);
        btnOK=new JButton("OK");
        btnOK.setBackground(UILib.colorButtonBackground);
        btnOK.setForeground(UILib.colorButtonForeground);
        btnOK.setBounds(165,110, 70, 30);
        btnOK.setFont(font);
        contentPane.add(btnOK);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoom(main);
            }
        });
        this.add(btnOK);
    }
    private void addRoom(Main main){
        try {
            socket= new Socket("localhost",3001);
            Room room= new Room(this.textField.getText(),main.getLogin().gettFusername().getText());
            ObjectOutputStream oos= new ObjectOutputStream(socket.getOutputStream());
            Message message= new Message("create",room);
            oos.writeObject(message);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataInputStream dis=new DataInputStream(socket.getInputStream());
                        String rep=dis.readUTF();
                        System.out.println(rep);
                        socket.close();
                        if(rep.equals("true")){
                            JOptionPane.showMessageDialog(new JFrame(),"Thêm thành công");
                        }else {
                            JOptionPane.showMessageDialog(new JFrame(), "thêm thất bại");
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
