package messaging;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import GUI_sockets.client_interface;

public class Client implements Runnable {
	
	private client_interface cUI;
	
	
	public Client(client_interface cUI) {
		this.cUI = cUI;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			InetAddress host = InetAddress.getLocalHost();
			Socket socket = null;
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			String message = "";
			
			//establish socket connection to server
			socket = new Socket(host.getHostName(), 4021);
			while(!message.equals("exit")) {
				
				if(this.cUI.check() == 0) {
					Thread.sleep(500);
					continue;
				}
				this.cUI.received();
				
				//write to socket using ObjectOutputStream
				oos = new ObjectOutputStream(socket.getOutputStream());
				System.out.println("Sending request to Socket Server");
				
				message = this.cUI.textArea_to.getText();
				oos.writeObject(message);
				
				this.cUI.textArea_to.setText("");
				
				//read the server response message
				ois = new ObjectInputStream(socket.getInputStream());
				String response = (String) ois.readObject();
				System.out.println("Message: " + message);
				
				if(this.cUI.textArea_from.getText().equals("")) {
					this.cUI.textArea_from.setText(response);
				} else {
					this.cUI.textArea_from.append(response);
				}
				
				//close resources
				ois.close();
				oos.close();
				Thread.sleep(100);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}
