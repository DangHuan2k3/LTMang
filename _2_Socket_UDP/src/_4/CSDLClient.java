package _4;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class CSDLClient {
    public CSDLClient() {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            sendData = "getData".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6996);
            // Gửi dữ liệu lại cho client
            clientSocket.send(sendPacket);

            // lấy dữ liệu từ packet nhận dược
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String str = new String(receivePacket.getData());

            System.out.println(str);

            clientSocket.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        new CSDLClient();
    }
}
