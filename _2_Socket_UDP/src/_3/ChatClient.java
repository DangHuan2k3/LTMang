
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

import _1.DateTimeServer;

public class ChatClient {
    public ChatClient() {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            int port = 6996;

            System.out.println("Client is started");

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            sendData = "connecting".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6996);
            // Gửi dữ liệu lại cho client
            clientSocket.send(sendPacket);

            while (true) {
                receiveData = new byte[1024];
                sendData = new byte[1024];
                // Tạo packet gói rỗng để nhận dữ liệu từ Client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                // Nhận dữ liệu từ client
                clientSocket.receive(receivePacket);

                String message = new String(receivePacket.getData());
                message = message.trim();

                System.out.println("SERVER: " + message);

                Scanner sc = new Scanner(System.in);
                System.out.print("YOU: ");
                String s = sc.nextLine();
                s = s.trim();

                if (!s.trim().equals("")) {
                    sendData = s.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    // Gửi dữ liệu lại cho client
                    clientSocket.send(sendPacket);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new ChatClient();
    }
}
