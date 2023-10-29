package _temp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 5776);
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            System.out.println(br.readLine());

            pw.println("Ha ha doo ngoc");
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
