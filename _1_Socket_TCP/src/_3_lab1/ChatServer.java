package _3_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatServer extends JFrame implements Runnable {
    private String NameServer;
    private ServerSocket server;
    private LinkedList<Socket> sockets = new LinkedList<>();

    private JTextArea txtaChat;
    private JTextField txtfInput;
    private JButton btnSend;

    private void importGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(NameServer);
        setSize(410, 400);
        setLayout(null);
        txtaChat = new JTextArea();
        txtaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtaChat);
        add(scrollPane);
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

    private void echoServer() {
        try {
            server = new ServerSocket(9999);
            System.out.println("Server is open in port " + server.getLocalPort());
            while (true) {
                Socket s = server.accept();
                sockets.add(s);
                ExecutorService pool = Executors.newFixedThreadPool(10);

                pool.execute(() -> {
                    // handle client
                    try {
                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        handleClient(dis);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public ChatServer(String nameServer) {
        this.NameServer = nameServer;
        importGUI();
        echoServer();
    }

    private void handleClient(DataInputStream dis) {
        try {
            while (true) {
                String s = dis.readUTF();
                txtaChat.append("\n" + s);
                for (Socket socket : sockets) {
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(s);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        enterUserName();
    }

    private void handleBtnSend() {
        try {
            String sendText = NameServer + ": " + txtfInput.getText();
            txtaChat.setText(txtaChat.getText() + "\n" + sendText);
            for (Socket socket : sockets) {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(sendText);
            }
            txtfInput.setText("");
        } catch (Exception e) {
            // TODO: handle exception
        }

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
                        new ChatServer(txtfName.getText());
                    });

                    t.start();
                }
            }
        });
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
}
