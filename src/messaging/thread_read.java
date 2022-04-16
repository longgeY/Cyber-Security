package messaging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import GUI_sockets.client_interface;

public class thread_read implements Runnable {

	private MulticastSocket socket;
	private InetAddress group;
	private int port;
	private client_interface cUI;

	public thread_read(MulticastSocket socket, InetAddress group, int port, client_interface cUI) {
		this.socket = socket;
		this.group = group;
		this.port = port;
		this.cUI = cUI;
	}

	@Override
	public void run() {
		while(!thread_chat.finished)  {
	
			byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, this.port);
			
			String message;
			
			try {
				
				socket.receive(datagram);
				message = new String(buffer, 0, datagram.getLength(), "UTF-8");
				this.cUI.textArea_from.append("Anonymous: " + message + "\n-\n");
			}
			catch(IOException e)  {
				System.out.println("Socket closed!");
			}
		}
	}
}
