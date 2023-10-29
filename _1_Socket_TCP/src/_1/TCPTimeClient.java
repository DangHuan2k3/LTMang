package _1;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPTimeClient {
	public static void main(String[] args) {
		try {
			Socket client = new Socket("localhost", 7788);

			DataInputStream din = new DataInputStream(client.getInputStream());

			String time = din.readUTF();
			System.out.println(time);

			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
