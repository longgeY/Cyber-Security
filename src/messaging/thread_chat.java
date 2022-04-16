package messaging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

import GUI_sockets.client_interface;

public class thread_chat implements Runnable {

	public static boolean finished = false;
	private String host;
	private int port;
	private client_interface cUI;

	public thread_chat(String host, int port, client_interface cUI) {
		this.host = host;
		this.port = port;
		this.cUI = cUI;
	}

	@SuppressWarnings("deprecation")
	public void run()
	{
		try
		{
			InetAddress group = InetAddress.getByName(this.host);
						
			MulticastSocket socket = new MulticastSocket(this.port);

			socket.setTimeToLive(1);
			
			socket.joinGroup(group);
			System.out.println("User connected!");
			
			// Start thread for reading messages from other users
			Thread t = new Thread(new thread_read(socket, group, this.port, this.cUI));
			t.start(); 

			while(true) {
				
				while(this.cUI.check() == 0) { // Wait for user to send new message to group
					Thread.sleep(200);
					continue;
				}
				
				String message = this.cUI.textArea_to.getText();
				this.cUI.textArea_to.setText("");
				this.cUI.received(); // Reset check value for next iteration
				
				if(message.equalsIgnoreCase("Exit")) {
					
					finished = true;
					socket.leaveGroup(group);
					socket.close();
					break;
					
				}
				
				byte[] buffer = message.getBytes();
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
				
				socket.send(datagram);
			}
		}
		catch(SocketException se)
		{
			System.out.println("Error creating socket");
			se.printStackTrace();
		}
		catch(IOException ie)
		{
			System.out.println("Error reading/writing from/to socket");
			ie.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
