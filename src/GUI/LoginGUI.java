package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import GUI_sockets.client_interface;
import messaging.thread_chat;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginGUI {

	private JFrame frame;
	private JTextField UserName_text;
	private JTextField Password_text;
	private static java.sql.Connection connection;
	
	String url = "jdbc:mysql://localhost:3306/vigeneresystem";
	String username = "root";
	String password = "longer000";

	/**
	 * Launch the application.
	 */
	
	public void testJdbc() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(url, username, password);
		Statement statement = (Statement) connection.createStatement();
		String User_Name = "select * from login";
		ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(User_Name);
		while (resultSet.next()) {
			System.out.print(resultSet.getString("user") + ":  ");
			System.out.println(resultSet.getInt("pw1"));
		}
		resultSet.close();
		((Connection) statement).close();
		connection.close();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.testJdbc();
//					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		UserName_text = new JTextField();
		UserName_text.setBounds(129, 68, 200, 36);
		panel.add(UserName_text);
		UserName_text.setColumns(10);
		
		Password_text = new JTextField();
		Password_text.setBounds(129, 129, 200, 36);
		panel.add(Password_text);
		Password_text.setColumns(10);
		
		JLabel UserName = new JLabel("USERNAME");
		UserName.setBounds(54, 67, 75, 36);
		panel.add(UserName);
		
		JLabel Password = new JLabel("PASSWORD");
		Password.setBounds(54, 128, 75, 36);
		panel.add(Password);
		
		JButton Login_Button = new JButton("Log In");
		Login_Button.setBounds(147, 199, 91, 21);
		Login_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int port = 4021;

				try {
					thread_chat chat = new thread_chat("230.0.0.0", port, new client_interface());
					Thread t = new Thread(chat);
					t.start();

					// This second thread is for demostration
					thread_chat chat2 = new thread_chat("230.0.0.0", port, new client_interface());
					Thread t2 = new Thread(chat2);
					t2.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		panel.add(Login_Button);
		
		Panel panel_1 = new Panel();
		panel_1.setBounds(229, 10, 10, 10);
		panel.add(panel_1);
		
		frame.setVisible(true);
	}
}
