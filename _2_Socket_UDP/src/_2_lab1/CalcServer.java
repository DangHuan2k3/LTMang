package _2_lab1;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * CalcServer
 */
public class CalcServer extends JFrame {
    private DatagramSocket serverSocket;
    private Integer defaultPort = 8888;

    private JTextField txtfPort = new JTextField();
    private JButton btnOpenPort = new JButton("Open port");
    private JTextArea txtaStatus = new JTextArea();

    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    public CalcServer() {
        importGUI();
    }

    public void importGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(null);
        this.setSize(500, 800);
        this.setTitle("Calculate Server");

        // add textfInput;
        this.add(txtfPort);
        txtfPort.setText(defaultPort.toString());
        txtfPort.setBounds(50, 75, 400, 50);

        // add btnSend
        this.add(btnOpenPort);
        btnOpenPort.setBounds(200, 150, 100, 50);

        // add Event
        btnOpenPort.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(() -> {
                    handleBtnOpenPort();
                });
                t.start();
            }
        });

        // add txtaOutput
        JScrollPane scrollPane = new JScrollPane(txtaStatus);
        scrollPane.setBounds(50, 250, 400, 300);
        this.add(scrollPane);
        txtaStatus.setEditable(false);

        this.setVisible(true);
    }

    public void handleBtnOpenPort() {
        try {
            serverSocket = new DatagramSocket(Integer.parseInt(txtfPort.getText()));
            txtaStatus.append("Open port use UDP method in port " + Integer.parseInt(txtfPort.getText()) + "\n");
            // Vô hiệu hoá txtfPort và btnOpenPort
            txtfPort.setEditable(false);
            btnOpenPort.setEnabled(false);

            Thread t = new Thread(() -> {
                handleClient();
            });
            t.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void handleClient() {
        try {
            while (true) {
                // Tạo packet gói rỗng để nhận dữ liệu từ Client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                // Nhận dữ liệu từ client
                serverSocket.receive(receivePacket);
                // Lấy địa chỉ IP của máy Client
                InetAddress IPAddress = receivePacket.getAddress();

                // Lấy port của chương trình Client
                int port = receivePacket.getPort();

                // Lấy ngày giờ để gửi ngược lại client
                String request = new String(receivePacket.getData());
                System.out.println(request);
                request = request.trim();
                System.out.println(request.trim().length());
                txtaStatus.append("Recived from " + IPAddress.toString() + ": " + request + "\n");

                String s = request + "=" + calculate(request) + "\n";

                sendData = s.trim().getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                // Gửi dữ liệu lại cho client
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // String s = "(123+2)*3-4";
        // System.out.println(calculate(s));
        new CalcServer();
    }

    private static Double calculate(String s) {
        Double res = 0.0;
        String[] s_postfix = convert(s);

        res = evaluate(s_postfix);

        return res;
    }

    public static Double evaluate(String[] tokens) {

        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            if (!token.isEmpty())
                if (token.equals("+")) {
                    Double a = stack.pop();
                    Double b = stack.pop();
                    stack.push(a + b);
                } else if (token.equals("-")) {
                    Double b = stack.pop();
                    Double a = stack.pop();
                    stack.push(a - b);
                } else if (token.equals("*")) {
                    Double a = stack.pop();
                    Double b = stack.pop();
                    stack.push(a * b);
                } else if (token.equals("/")) {
                    Double b = stack.pop();
                    Double a = stack.pop();
                    stack.push(a / b);
                } else if (token.equals("(")) {
                    // Do nothing
                } else if (token.equals(")")) {
                    // Do nothing
                } else {
                    stack.push(Double.parseDouble(token.toString()));
                }
        }

        return stack.pop();
    }

    private static String[] convert(String infix) {
        Stack<Character> stack = new Stack<>();
        String postfix = "";

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                String temp = "";
                temp += c;
                while ((i + 1) < infix.length() && Character.isLetterOrDigit(infix.charAt(i + 1))) {
                    temp += infix.charAt(i + 1);
                    i++;
                }

                postfix = postfix + " " + temp;
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix = postfix + " " + stack.pop();
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    postfix = postfix + " " + stack.pop();
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix = postfix + " " + stack.pop();
        }

        return postfix.split(" ");
    }

    private static int precedence(char c) {
        if (c == '+' || c == '-') {
            return 1;
        } else if (c == '*' || c == '/') {
            return 2;
        } else {
            return 0;
        }
    }
}