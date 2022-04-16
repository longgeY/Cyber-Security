package messaging;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerCheck extends java.net.Socket {

	private int port;

	public ServerCheck(int port) {
		this.port = port;
	}
	
	public boolean isLocalPortInUse() {
		try {

			new ServerSocket(this.port).close();
			return false;

		} catch(IOException e) {
			return true;
		}

	}

}
