package _3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChuoiClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6996);
        System.out.println("Connected Successfully");

        System.out.print("Nhap chuoi bat ki: ");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine(); // Chuoi bat ki

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream din = new DataInputStream(socket.getInputStream());
        dos.writeUTF(s); // Truyenef chuoi ve cho server xu li;
        dos.flush();

        for (int i = 0; i < 3; i++) {
            System.out.println(din.readUTF());
            System.out.print("Press Enter to continue ");
            //sc.nextLine();
        }
        sc.close();
        socket.close();
    }
}
