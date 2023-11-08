package _1_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChuoiClient extends JFrame {
    private Integer defaultPort = 7000;
    private Integer port;

    DatagramSocket clientSocket;
    InetAddress IPAddress;

    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    private JTextField txtfPort = new JTextField();
    private JButton btnConnect = new JButton("Connect");

    private JTextField txtfInput = new JTextField();
    private JButton btnSend = new JButton("Send");
    private JTextArea txtaOutput = new JTextArea();

    private int WIDTH, HEIGHT;

    public void importGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);
        setSize(500, 800);

        add(txtfPort);
        txtfPort.setBounds(50, 30, 300, 30);
        txtfPort.setText(defaultPort.toString());

        add(btnConnect);
        btnConnect.setBounds(350, 30, 100, 30);
        btnConnect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                handleEventBtnConnect();
            }

        });

        // add textfInput;
        add(txtfInput);
        txtfInput.setBounds(50, 75, 400, 50);

        // add btnSend
        add(btnSend);
        btnSend.setEnabled(false);
        btnSend.setBounds(200, 150, 100, 50);

        // add Event
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEventBtnSend();
            }

        });

        // add txtaOutput
        JScrollPane scrollPane = new JScrollPane(txtaOutput);
        scrollPane.setBounds(50, 250, 400, 300);
        add(scrollPane);
        txtaOutput.setEditable(false);

        setVisible(true);
    }

    private void handleEventBtnConnect() {
        try {
            clientSocket = new DatagramSocket();
            IPAddress = InetAddress.getByName("localhost");
            port = Integer.parseInt(txtfPort.getText());
            txtaOutput.append("Connected succesfully to localhost:" + port + "\n");
            btnSend.setEnabled(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public ChuoiClient() throws Exception {
        importGUI();
    }

    public void handleEventBtnSend() {
        try {
            System.out.println(txtfInput.getText());
            sendData = txtfInput.getText().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,
                    port);
            // Gửi dữ liệu lại cho client
            clientSocket.send(sendPacket);

            // Handle Server
            Thread t = new Thread(() -> {
                handleServer();
            });
            t.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void handleServer() {
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String str = new String(receivePacket.getData());
            txtaOutput.append(str + "\n");
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void main(String[] args) throws Exception {
        new ChuoiClient();
    }
}
