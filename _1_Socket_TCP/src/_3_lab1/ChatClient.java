package _3_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame {
    private String NameClient;

    private Socket client;
    private DataOutputStream dos;
    private DataInputStream dis;

    private JTextArea txtaChat;
    private JTextField txtfInput;
    private JButton btnSend;

    private void importGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(NameClient);
        setSize(410, 400);
        setLayout(null);
        txtaChat = new JTextArea();
        txtaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtaChat);
        this.add(scrollPane);
        scrollPane.setBounds(0, 0, 400, 300);

        // Create a JTextField for user input
        txtfInput = new JTextField();
        add(txtfInput);
        txtfInput.setBounds(0, 300, 300, 50);

        // Create a JButton for sending messages
        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(() -> {
                    handleBtnSend();
                });

                t.start();
            }
        });
        add(btnSend);
        btnSend.setBounds(300, 300, 100, 50);

        setVisible(true);
    }

    private void handleBtnSend() {
        try {
            dos.writeUTF(NameClient + ": " + txtfInput.getText());
            txtfInput.setText("");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void echoClient() {
        try {
            client = new Socket("localhost", 9999);
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            Thread t = new Thread(() -> {
                handleServer(dis, dos);
            });
            t.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void handleServer(DataInputStream dis, DataOutputStream dos) {
        try {
            while (true) {
                String s = dis.readUTF();
                txtaChat.append("\n" + s);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public ChatClient(String nameClient) {
        this.NameClient = nameClient;
        importGUI();
        echoClient();
    }

    public static void main(String[] args) {
        // new ChatClient();
        enterUserName();
    }

    public static void enterUserName() {
        JFrame frameEntername = new JFrame("Enter your name");

        frameEntername.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameEntername.setSize(400, 150);
        frameEntername.setLayout(null);
        frameEntername.setVisible(true);
        JTextField txtfName = new JTextField();
        frameEntername.add(txtfName);
        txtfName.setBounds(20, 20, 200, 50);

        JButton btnOK = new JButton("OK");
        frameEntername.add(btnOK);
        btnOK.setBounds(230, 20, 100, 50);
        btnOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (!txtfName.getText().isEmpty()) {
                    Thread t = new Thread(() -> {
                        frameEntername.dispose();
                        new ChatClient(txtfName.getText());
                    });
                    t.start();
                }
            }
        });
    }

}
