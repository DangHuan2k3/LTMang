package _1_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChuoiServer extends JFrame {
    private Socket s;
    private Integer defaultPort = 7000;

    private JTextField txtfPort = new JTextField(defaultPort);
    private JButton btnOpenPort = new JButton("Open Port");
    private JTextArea txtaStatus = new JTextArea();

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
            ServerSocket server = new ServerSocket(Integer.parseInt(txtfPort.getText().toString()));
            txtaStatus.setText(txtaStatus.getText() +
                    "TCP/Server running on : " + InetAddress.getLocalHost() + " ,Port " +
                    server.getLocalPort());
            btnOpenPort.setEnabled(false);
            while (true) {
                s = server.accept();

                Thread t_sockets = new Thread(() -> {
                    try {
                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                        txtaStatus.setText(
                                txtaStatus.getText() + "\n" + "...Server is waiting input from client socket = ..."
                                        + s);
                        this.handleClient(dis, dos);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                });
                t_sockets.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public ChuoiServer() throws Exception {
        makeVowelsList();
        importGUI();
    }

    public void handleClient(DataInputStream dis, DataOutputStream dos) {
        try {
            while (true) {
                String str = dis.readUTF();

                System.out.println("Recived: " + str);

                String strRes = "";
                strRes += reverseString(str) + "\n";
                strRes += myLowercase(str) + "\n";
                strRes += myUppercase(str) + "\n";
                strRes += myLowerAndUpper(str) + "\n";
                strRes += "So tu cua chuoi la: " + countWords(str) + "\n";
                strRes += "Cac nguyen am trong chuoi la: \n";
                int[] res = numberOfVowelInString(str);

                for (int i = 0; i < vowels.size(); i++) {
                    strRes += "\tNguyen am '" + vowels.get(i) + " ': " + res[i] + "\n";
                }

                dos.writeUTF(strRes);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void run() {

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
