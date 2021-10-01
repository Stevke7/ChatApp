package server;

import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args) 
	{
	Server Host = new Server();
	Host.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Zatvaranje programa
	Host.startRunning();
	}

}
