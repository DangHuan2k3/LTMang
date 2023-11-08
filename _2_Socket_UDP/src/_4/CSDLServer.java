package _4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class CSDLServer {
    private static String DB_URL = "jdbc:mysql://localhost:3306/dulieu";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";

    public static void main(String[] args) throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(6996);
        System.out.println("Server is started");

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        String resultString = "";

        try {
            // connnect to database 'baitapapdung'
            Connection conn = getConnection(DB_URL, USER_NAME, PASSWORD);
            // crate statement
            Statement stmt = conn.createStatement();
            // get data from table 'student'
            ResultSet rs = stmt.executeQuery("select * from sinhvien");

            // show data
            while (rs.next()) {
                resultString += rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + "\n";
            }
            // close connection
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
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

            if (request.trim().equals("getData")) {
                sendData = resultString.getBytes();
            } else {
                sendData = "Server not know what you want".getBytes();
            }

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            // Gửi dữ liệu lại cho client
            serverSocket.send(sendPacket);
        }
    }

    public static Connection getConnection(String dbURL, String userName,
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
}
