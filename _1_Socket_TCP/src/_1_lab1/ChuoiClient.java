package _1_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChuoiClient extends JFrame {
    private Integer defaultPort = 7000;
    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;

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
                // TODO Auto-generated method stub
                // Thread t = new Thread(() -> {
                handleEventBtnSend();
                // });
                // t.start();
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
            client = new Socket("localhost", Integer.parseInt(txtfPort.getText()));
            txtaOutput.setText(txtaOutput.getText() + "Client socket = " + client +
                    "\n");
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

            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            String s = txtfInput.getText();
            dos.writeUTF(s);

            txtaOutput.setText(txtaOutput.getText() + dis.readUTF() + "\n");

            txtfInput.setText("");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChuoiClient();
    }
}
