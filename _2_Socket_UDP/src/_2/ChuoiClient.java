package _2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ChuoiClient {

    public ChuoiClient() {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] receiveData = new byte[1024 * 10];
            byte[] sendData = new byte[1024];
            Scanner sc = new Scanner(System.in);
            String temp = sc.nextLine();
            sendData = temp.getBytes();
            sc.close();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9966);
            // Gửi dữ liệu lại cho client
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String str = new String(receivePacket.getData());

            System.out.println(str);

            clientSocket.close();

            // lấy dữ liệu từ packet nhận dược

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        new ChuoiClient();
    }
}
