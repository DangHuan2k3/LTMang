package _temp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ser = new ServerSocket(5776);
            while (true) {
                Socket sk = ser.accept();
                PrintStream ps = new PrintStream(sk.getOutputStream());

                ps.println("Da ket noi toi server");

                InputStreamReader isr = new InputStreamReader(sk.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                System.out.println(br.readLine());
                sk.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}