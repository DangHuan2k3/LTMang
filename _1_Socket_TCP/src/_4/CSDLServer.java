package _4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CSDLServer {
    private static String DB_URL = "jdbc:mysql://localhost:3306/baitapapdung";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";

    public static void main(String[] args) throws Exception {

        ServerSocket socket = new ServerSocket(9000);
        System.out.println("Server is started");

        Socket server = socket.accept();

        DataOutputStream dos = new DataOutputStream(server.getOutputStream());
        DataInputStream din = new DataInputStream(server.getInputStream());
        String queryString = din.readUTF(); // Get quertString from client
        String resultString = "";
        try {
            // connnect to database 'baitapapdung'
            Connection conn = getConnection(DB_URL, USER_NAME, PASSWORD);
            // crate statement
            Statement stmt = conn.createStatement();
            // get data from table 'student'
            ResultSet rs = stmt.executeQuery(queryString);

            // show data
            while (rs.next()) {
                resultString += rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + "\n";
            }

            dos.writeUTF(resultString);
            // close connection
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            dos.writeUTF(resultString + " unaccepted query String");

        }
        socket.close();
        server.close();
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
