package messaging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server implements Runnable {

	private ServerSocket server;
	private static int port = 4021;
	Map<Integer, java.net.Socket> clients = new HashMap<Integer, java.net.Socket> ();

	public Server(ServerSocket server) {
		this.server = server;
	}
	public void run() {
		
		try {
			
			server = new ServerSocket(port);
			String message = "";
			
			while(!message.equals("exit")) {
				
				System.out.println("Server: waiting for client request.");
				
				Socket socket = server.accept(); // Wait for client connection
				clients.put(socket.getPort(), socket);
				
				
				
				for (Iterator<Integer> iter = clients.keySet().iterator(); iter.hasNext(); )
				{
				    int key = iter.next();

				    java.net.Socket client = clients.get(key);
				    OutputStream os = client.getOutputStream();
				    OutputStreamWriter osw = new OutputStreamWriter(os);
				    BufferedWriter bw = new BufferedWriter(osw);
				    bw.write("Some message");
				    bw.flush();
				    
//					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//					
//					message = (String) ois.readObject();
//					System.out.println("Server message received: " + message);
//					
//					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//					oos.writeObject("Anonymous: " + message + "\n-\n");
//					
//					ois.close();
//					oos.close();
//					socket.close();
				}
				
			}
			server.close();
			System.out.println("Server closed.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
