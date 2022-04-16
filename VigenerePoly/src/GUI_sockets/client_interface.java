package GUI_sockets;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import cryptography.algorithms;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import java.awt.Color;

public class client_interface {

	private JFrame frame;
	public JTextArea textArea_to;
	public JTextArea textArea_from;
	private int check;
	private JTextField textField_key;
	private algorithms alg;
	
	/**
	 * Create the application.
	 */
	public client_interface() {
		this.alg = new algorithms();
		initialize();
		this.check = 0;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 898, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel label_from = new JLabel("From:");
		label_from.setHorizontalAlignment(SwingConstants.LEFT);
		label_from.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_from.setBounds(10, 24, 111, 30);
		panel.add(label_from);
		
		JLabel label_to = new JLabel("To:");
		label_to.setHorizontalAlignment(SwingConstants.LEFT);
		label_to.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_to.setBounds(450, 24, 111, 30);
		panel.add(label_to);
		
		textArea_to = new JTextArea();
		textArea_to.setBounds(450, 52, 422, 232);
		panel.add(textArea_to);
		
		textArea_from = new JTextArea();
		textArea_from.setEditable(false);
		textArea_from.setBounds(10, 52, 412, 464);
		panel.add(textArea_from);
		
		/*
		JScrollBar scrollBar_v_from = new JScrollBar();
		scrollBar_v_from.setBounds(423, 52, 17, 464);
		panel.add(scrollBar_v_from);
		
		JScrollBar scrollBar_h_from = new JScrollBar();
		scrollBar_h_from.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar_h_from.setBounds(10, 514, 412, 17);
		panel.add(scrollBar_h_from);
		*/		
		
		JLabel label_key = new JLabel("Key:");
		label_key.setBounds(460, 295, 46, 14);
		panel.add(label_key);
		
		textField_key = new JTextField();
		textField_key.setBounds(498, 295, 86, 20);
		panel.add(textField_key);
		textField_key.setColumns(10);
		
		JButton button_send = new JButton("Send Message");
		button_send.setBounds(549, 387, 139, 23);
		button_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sent();
			}
		});
		panel.add(button_send);
		
		JButton button_encrypt = new JButton("Encrypt");
		button_encrypt.setBounds(450, 353, 89, 23);
		button_encrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the plaintext and key input
				String plaintext_input = textArea_to.getText();
				String key = textField_key.getText();

				//Testing
				System.out.println("Plain Text: " + plaintext_input);
				System.out.println("Key: " + key);

				// Check if plaintext and key were input
				if(plaintext_input.equals("") || key.equals("")) {
					textArea_to.setText("Please enter both plain text and key!");
					textField_key.setText("");
					textArea_to.setText("");
				}
				else {
					// Set plaintext and key values to alg object
					alg.setPlainText(plaintext_input);
					alg.setKey(key);
					alg.genKey();
					alg.mapKeyAndText();

					// Encrypt the plain text using input key 
					try {
						alg.encrypt();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// Show user the results from encryption
					textArea_to.setText(alg.getCipherText());
				}
			}
		});
		panel.add(button_encrypt);
		
		JButton button_decrypt = new JButton("Decrypt");
		button_decrypt.setBounds(450, 387, 89, 23);
		button_decrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					alg.setPlainText(textArea_to.getText());
					alg.setKey(textField_key.getText());
					alg.genKey();
					alg.mapKeyAndText();
					alg.setCipherText(textArea_to.getText());
					alg.decrypt();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textArea_to.setText(alg.getDecryptedPlainText());
			}
		});
		panel.add(button_decrypt);
		
		JLabel lblNewLabel = new JLabel("Note: to decrypt messages, paste them into the text area labeled \"To:\".");
		lblNewLabel.setBackground(Color.GRAY);
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(10, 536, 422, 14);
		panel.add(lblNewLabel);
		
		frame.setVisible(true);
	}
	
	public int check() { return this.check; }
	public void sent() { this.check = 1; }
	public void received() { this.check = 0; }
	
	public void newMessage(String message) {
		String curr_text = textArea_from.getText();
		if(curr_text.equals("")) {
			textArea_from.setText(message);
		} else {
			textArea_from.append(message);
		}
	}
	
	public String getMessage() { 
		String[] from = textArea_from.getText().split("\n-");
		return from[from.length-1];		 
	}
}
