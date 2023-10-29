package _2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("192.168.231.222", 7788);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream din = new DataInputStream(socket.getInputStream());

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Client: ");
            String msg = sc.nextLine();

            dos.writeUTF("Client: " + msg);
            dos.flush();

            String st = din.readUTF();
            System.out.println(st);
            sc = sc.reset();
        }
    }
}