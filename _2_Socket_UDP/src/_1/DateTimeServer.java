package _1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class DateTimeServer {

    public DateTimeServer() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(6996);

            System.out.println("Server is started");

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

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
                System.out.println(request);

                if (request.trim().equals("getDate")) {
                    sendData = new Date().toString().getBytes();
                } else {
                    sendData = "Servr not know what you want".getBytes();
                }

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                // Gửi dữ liệu lại cho client
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new DateTimeServer();
    }
}
