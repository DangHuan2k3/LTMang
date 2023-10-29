package _3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChuoiServer {
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

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(6996);
        System.out.println("Server is started");

        Socket socket = server.accept();

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream din = new DataInputStream(socket.getInputStream());

        String s = din.readUTF(); // Chuoi tu client;

        dos.writeUTF("Chuoi hoa: " + myUppercase(s));

        dos.writeUTF("Chuoi thuong: " + myLowercase(s));

        dos.writeUTF("Chuoi vua hoa vua thuong: " + myLowerAndUpper(s));
        dos.writeUTF("");
        dos.close();
        din.close();
        server.close();
    }

}
