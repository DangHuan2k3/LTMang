
package _3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ChatServer {
    public ChatServer() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(6996);

            System.out.println("Server is started. Waiting for connecting from Client");

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            // Tạo packet gói rỗng để nhận dữ liệu từ Client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            // Nhận dữ liệu từ client
            serverSocket.receive(receivePacket);
            // Lấy địa chỉ IP của máy Client
            InetAddress IPAddress = receivePacket.getAddress();

            // Lấy port của chương trình Client
            int port = receivePacket.getPort();

            System.out.println("Connecting Sucessfully from " + IPAddress + ": " + port);

            while (true) {
                receiveData = new byte[1024];
                sendData = new byte[1024];
                Scanner sc = new Scanner(System.in);
                System.out.print("YOU: ");
                String s = sc.nextLine();
                s = s.trim();

                if (!s.trim().equals("")) {
                    sendData = s.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    // Gửi dữ liệu lại cho client
                    serverSocket.send(sendPacket);
                }

                // Tạo packet gói rỗng để nhận dữ liệu từ Client
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                // Nhận dữ liệu từ client
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData());
                message = message.trim();
                System.out.println("CLIENT: " + message);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
