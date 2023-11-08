package _1_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChuoiServer extends JFrame {
    DatagramSocket serverSocket;
    private Integer defaultPort = 7000;

    private JTextField txtfPort = new JTextField(defaultPort);
    private JButton btnOpenPort = new JButton("Open Port");
    private JTextArea txtaStatus = new JTextArea();

    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    public void importGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500, 800);

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
                handleBtnOpenPort();
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

    public ChuoiServer() throws Exception {
        makeVowelsList();
        importGUI();
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
                request = request.trim();
                txtaStatus.append("Recived from " + IPAddress.toString() + ": " + request + "\n");

                String s = reverseString(request) + "\n";
                s += myLowercase(request) + "\n";
                s += myUppercase(request) + "\n";
                s += myLowerAndUpper(request) + "\n";
                s += "So tu cua chuoi la: " + countWords(request) + "\n";
                s += "Cac nguyen am trong chuoi la: \n";
                int[] res = numberOfVowelInString(request);

                for (int i = 0; i < vowels.size(); i++) {
                    s += "\tNguyen am '" + vowels.get(i) + "': " + res[i] + "\n";
                }

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

    public static void main(String[] args) throws Exception {
        new ChuoiServer();
    }

    private static String reverseString(String s) {
        String res = "";

        for (int i = s.length() - 1; i >= 0; i--) {
            res += s.charAt(i);
        }

        return res;
    }

    private static String myUppercase(String s) {
        String res = "";

        for (char c : s.toCharArray()) {
            if (c <= 'z' && c >= 'a') {
                res += (char) ((int) c - 32);
            } else {
                res += (char) ((int) c);
            }
        }

        return res.toString();
    }

    private static String myLowercase(String s) {
        String res = "";

        for (char c : s.toCharArray()) {
            if (c <= 'Z' && c >= 'A') {
                res += (char) ((int) c + 32);
            } else {
                res += (char) ((int) c);
            }
        }

        return res;
    }

    private static String myLowerAndUpper(String s) {
        String res = "";

        for (char c : s.toCharArray()) {
            if (c <= 'Z' && c >= 'A') {
                res += (char) ((int) c + 32);
            } else if (c <= 'z' && c >= 'a') {
                res += (char) ((int) c - 32);
            } else {
                res += (char) ((int) c);
            }
        }

        return res;
    }

    private static int countWords(String s) {
        boolean isSpace = true;
        int cntWord = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                isSpace = true;
            } else if (((s.charAt(i) >= 'a' && s.charAt(i) <= 'z')
                    || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')) && isSpace) {
                cntWord++;
                isSpace = false;
            }
        }

        return cntWord;
    }

    private static ArrayList<Character> vowels = new ArrayList<>();

    private static void makeVowelsList() {
        vowels.add('a');
        vowels.add('e');
        vowels.add('i');
        vowels.add('o');
        vowels.add('u');
    }

    private static int[] numberOfVowelInString(String s) {
        int[] vowelsCount = new int[5];

        // duyệt qua các ký tự trong chuỗi
        for (int i = 0; i < s.length(); i++) {

            char ch = s.charAt(i);

            // kiểm tra và tăng số lần xuất hiện tương ứng
            if (ch == 'a' || ch == 'A') {
                vowelsCount[0]++;
            } else if (ch == 'e' || ch == 'E') {
                vowelsCount[1]++;
            } else if (ch == 'i' || ch == 'I') {
                vowelsCount[2]++;
            } else if (ch == 'o' || ch == 'O') {
                vowelsCount[3]++;
            } else if (ch == 'u' || ch == 'U') {
                vowelsCount[4]++;
            }
        }

        return vowelsCount;
    }
}
