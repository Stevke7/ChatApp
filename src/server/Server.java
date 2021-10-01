package server;
import java.io.*; /** paket za komuniciranje*/
import java.net.*;/** paket za sokete (komunikacija sa serverom)*/
import java.awt.*;/** Apstract Window Toolkit, sluzi za komunikaciju sa grafickim komponentama*/
import java.awt.event.*; /** Omogucuje vrsenje promjena kod komponenti ili dogadjaja*/
import javax.swing.*; /** Omogucuje kreiranje GUI koji je nezavisan od OS-a */




public class Server extends JFrame
{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server()
	{
		
		super("AbstractThinking Server!"); /** konstruktor za podesavanje GUI komponenti*/
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						sendMessage(event.getActionCommand());
						userText.setText("");
						
					}
				}
			);
		add(userText, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		add (new JScrollPane(chatWindow));
		setSize(1366, 768);
		setVisible(true);
		
			
		
	}
	
	public void startRunning() /** Metoda za pokretanje Servera*/
	{
		try 
		{
			server = new ServerSocket(3000, 20);
			while(true)
			{
				try
				{
					Connection();
					Setup();
					Chatting();
					
				}
				catch(EOFException eofException)
				{
					showMessage("\n Server je prekinuo konekciju! ");
					
				}
				finally
				{
					closeAll();
					
				}
				
				
			}
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	private void Connection() throws IOException
	{
		showMessage("Waiting for connection...\n");
		connection = server.accept();
		showMessage(connection.getInetAddress().getHostName() + "connected.\n");
		
	}
	private void Setup() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("Sve je podeseno\n");
		
	}
	
	private void Chatting() throws IOException
	{
		String message = "Connected";
		sendMessage(message);
		canType(true);
		
		
		do
		{
			try
			{
				message = (String) input.readObject();
				showMessage(message + "\n");
				
			}
			
			catch(ClassNotFoundException e)
			{
				showMessage("Nepoznata komanda!\n");
				
			}
			
		}
		while(!message.equals("CLIENT - END"));
		
	}
	
	private void closeAll()
	{
		showMessage("Closing Connections...\n");
		canType(false);
		try
		{
			output.close();
			input.close();
			connection.close();
			
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
			
		}
	}
	
	private void sendMessage(String message)
	{
		try
		{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("SERVER - " + message + "\n");
			
		}
		catch(IOException ioException)
		{
			chatWindow.append("Message can not be sent...");
		}
	}
	
	private void showMessage(final String text)
	{
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(text);
					}
				});
	}
	
	private void canType(final boolean tof)
	{
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				});
					
	}
}
