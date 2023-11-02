package _4_lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DBServer extends JFrame {
    private Socket s;
    private Integer defaultPort = 6996;

    private JTextField txtfPort = new JTextField(defaultPort);
    private JButton btnOpenPort = new JButton("Open Port");
    private JTextArea txtaStatus = new JTextArea();

    private String DB_URL = "jdbc:mysql://localhost:3306/baitapapdung";
    private String USER_NAME = "root";
    private String PASSWORD = "";

    public void importGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Database Server");
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

                ExecutorService pool = Executors.newFixedThreadPool(10);

                pool.execute(() -> {
                    // handle client
                    try {
                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                        txtaStatus.setText(
                                txtaStatus.getText() + "\n" + "...Server is connected from client socket = ..."
                                        + s);
                        this.handleClient(dos);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                });
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public DBServer() throws Exception {
        importGUI();
    }

    public void handleClient(DataOutputStream dos) {
        while (true) {
            try {
                String resultString = "";

                // connnect to database 'baitapapdung'
                Connection conn = getConnection(DB_URL, USER_NAME, PASSWORD);
                // crate statement
                Statement stmt = conn.createStatement();
                // get data from table 'student'
                ResultSet rs = stmt.executeQuery("SELECT * FROM phongban");

                // show data
                while (rs.next()) {
                    resultString += rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + "\n";
                }

                dos.writeUTF(resultString);
                // close connection
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Connection getConnection(String dbURL, String userName,
            String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, userName, password);
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }

    public void run() {

    }

    public static void main(String[] args) throws Exception {
        new DBServer();
    }
}
