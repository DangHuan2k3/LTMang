package _4;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class CSDLClient {
    public static void main(String[] args) throws Exception {
        Socket client = new Socket("localhost", 6969);

        System.out.println("Nhap cau lenh truy van: ");
        Scanner sc = new Scanner(System.in);
        String queryString = sc.nextLine();

        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        DataInputStream din = new DataInputStream(client.getInputStream());
        dos.writeUTF(queryString);
        System.out.println(din.readUTF());

        dos.writeUTF(queryString);

    }
}
