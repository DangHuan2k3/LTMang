package _3_lab1;

import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatServer extends JFrame implements Runnable {
    private Integer defaultPort = 9996;
    private String NameServer;

    DatagramSocket serverSocket;

    private LinkedList<InetAddress> clientsAddress = new LinkedList();
    private LinkedList<Integer> clientsPort = new LinkedList();

    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

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
            serverSocket = new DatagramSocket(defaultPort);

            Thread t = new Thread(() -> {
                handleClient();
            });

            t.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public ChatServer(String nameServer) {
        this.NameServer = nameServer;
        importGUI();
        echoServer();
    }

    private void handleClient() {
        try {
            while (true) {
                // Tạo packet gói rỗng để nhận dữ liệu từ Client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Lấy địa chỉ IP của máy Client
                InetAddress IPAddress = receivePacket.getAddress();

                // Lấy port của chương trình Client
                int port = receivePacket.getPort();

                boolean addNew = true;

                for (int i = 0; i < clientsAddress.size(); i++) {
                    if (clientsAddress.get(i).equals(IPAddress) && clientsPort.get(i).equals(port)) {
                        addNew = false;
                        break;
                    }
                }

                if (addNew) {
                    clientsAddress.add(IPAddress);
                    clientsPort.add(Integer.valueOf(port));
                }

                // Nhận dữ liệu từ client
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData());
                message = message.trim();

                txtaChat.append(message + "\n");

                // Gửi dữ liệu lại cho các Client khác

                for (int i = 0; i < clientsAddress.size(); i++) {
                    if (clientsAddress.get(i).equals(IPAddress) && clientsPort.get(i).equals(port)) {
                        continue;
                    }

                    sendData = message.trim().getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientsAddress.get(i),
                            clientsPort.get(i));
                    // Gửi dữ liệu lại cho client
                    serverSocket.send(sendPacket);
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
            if (!txtfInput.getText().isEmpty()) {
                txtaChat.append("YOU" + ": " + txtfInput.getText() + "\n");
            }

            String s = NameServer + ": " + txtfInput.getText() + "\n";
            for (int i = 0; i < clientsAddress.size(); i++) {
                sendData = s.trim().getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientsAddress.get(i),
                        clientsPort.get(i));
                // Gửi dữ liệu lại cho client
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
