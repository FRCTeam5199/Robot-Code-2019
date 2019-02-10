package frc.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RemoteOutput {

	private final InetAddress address;
	private final int port;
	private DatagramSocket socket;
	private DatagramPacket packet;

	public RemoteOutput(String addr, int port) {
		//set addr to your computer's local address. (ex: "10.51.99.206")
		InetAddress address = null;
		try {
			address = InetAddress.getByName(addr);
		} catch (UnknownHostException e1) {
			System.err.println("Failed to initialize address");
			e1.printStackTrace();
			System.exit(69);
		}

		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.err.println("Failed to initialize socket");
			e.printStackTrace();
			System.exit(420);
		}
		this.port = port;
		this.address = address;
	}

	public void print(String s) {
		byte[] buf = s.getBytes();
		packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Failed to send");
		}
 
	}

	public void print(Object o) {
		print(o.toString());
	}

	public void println(String s) {
		print(s + "\n");
	}

	public void println(Object o) {
		println(o.toString());
	}
}
