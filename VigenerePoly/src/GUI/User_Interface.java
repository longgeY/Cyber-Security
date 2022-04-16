package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import cryptography.algorithms;
import messaging.thread_chat;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import GUI_sockets.client_interface;

public class User_Interface {

	private JFrame frame;
	private JTextField textField_key_input;
	// Declare the algorithms class
	private algorithms alg;

	/**
	 * Create the application.
	 */
	public User_Interface(algorithms alg) {
		this.alg = alg;
		initialize();
	}
	
	//Validate for punctuation
	public String verify_input(String inputText) {
		if (inputText.isEmpty()) {
			return "";
		}
		return inputText.replaceAll("\\pP|\\pS|\\pC|\\pN", "");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setAutoRequestFocus(false);
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 765, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel_main = new JPanel();
		panel_main.setBackground(Color.GRAY);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel_main, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel_main, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE).addContainerGap()));
		panel_main.setLayout(null);

		JLabel label_title = new JLabel("Vigen\u00E8re Polyalphabetic Substitution Cryptographic System");
		label_title.setForeground(Color.WHITE);
		label_title.setBounds(154, 5, 379, 31);
		label_title.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_main.add(label_title);

		JLabel lblInputPlaintext = new JLabel("Input Plaintext:");
		lblInputPlaintext.setForeground(Color.WHITE);
		lblInputPlaintext.setBounds(21, 47, 85, 14);
		panel_main.add(lblInputPlaintext);

		JLabel lblResultingCiphertext = new JLabel("Resulting Ciphertext:");
		lblResultingCiphertext.setForeground(Color.WHITE);
		lblResultingCiphertext.setBounds(21, 205, 175, 14);
		panel_main.add(lblResultingCiphertext);

		JLabel lblPlaintextAfterDecryption = new JLabel("Plaintext After Decryption:");
		lblPlaintextAfterDecryption.setForeground(Color.WHITE);
		lblPlaintextAfterDecryption.setBounds(382, 47, 250, 14);
		panel_main.add(lblPlaintextAfterDecryption);

		JTextArea textArea_plaintext_input = new JTextArea();
		textArea_plaintext_input.setBounds(21, 60, 328, 49);
		panel_main.add(textArea_plaintext_input);

		JTextArea textArea_ciphertext = new JTextArea();
		textArea_ciphertext.setBounds(21, 218, 328, 49);
		panel_main.add(textArea_ciphertext);

		JTextArea textArea_plaintext_output = new JTextArea();
		textArea_plaintext_output.setBounds(382, 60, 328, 78);
		panel_main.add(textArea_plaintext_output);
		frame.getContentPane().setLayout(groupLayout);

		JTextArea textArea_output = new JTextArea();
		textArea_output.setLineWrap(true);
		textArea_output.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea_output.setForeground(Color.RED);
		textArea_output.setBackground(Color.DARK_GRAY);
		textArea_output.setBounds(0, 417, 558, 49);
		panel_main.add(textArea_output);

		textField_key_input = new JTextField();
		textField_key_input.setBounds(21, 140, 328, 20);
		panel_main.add(textField_key_input);
		textField_key_input.setColumns(10);

		JButton button_encrypt = new JButton("Encrypt");
		button_encrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// verify the form of plaintext whether correct or not
				String plaintext_input = verify_input(textArea_plaintext_input.getText());
				if (plaintext_input.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter plain text!");
					return;
				}
				
				// verify the form of key whether correct or not
				String key = verify_input(textField_key_input.getText());
				if (key.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter the correct key!");
					return;
				}

				// Testing
				System.out.println("Plain Text: " + plaintext_input);
				System.out.println("Key: " + key);

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
				textArea_ciphertext.setText(alg.getCipherText());
			}
		});
		button_encrypt.setBounds(80, 171, 89, 23);
		panel_main.add(button_encrypt);

		JButton button_random = new JButton("Random");
		button_random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String plaintext_input = verify_input(textArea_plaintext_input.getText());
				if (plaintext_input.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter plain text!");
					return;
				}

				alg.setPlainText(plaintext_input);

				String key = "";

				for (int i = 0; i < Math.random() * alg.getPlainText().length(); i++) {
					key = key + (char) (Math.random() * 26 + 'A');
				}
				
				textField_key_input.setText(key);
			}
		});
		button_random.setBounds(200, 171, 89, 23);
		panel_main.add(button_random);

		JButton button_decrypt = new JButton("Decrypt");
		button_decrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ciphertext = textArea_ciphertext.getText();
				if (ciphertext.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please decrypt text first!");
				}
				// Decrypt the cipher text
				try {
					alg.decrypt();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textArea_plaintext_output.setText(alg.getDecryptedPlainText());
			}
		});
		button_decrypt.setBounds(140, 278, 89, 23);
		panel_main.add(button_decrypt);

		JButton button_restart = new JButton("Restart");
		button_restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Restart GUI
				frame.dispose(); // Remove old frame
				new User_Interface(new algorithms()); // Start new GUI
			}
		});
		button_restart.setBounds(382, 149, 328, 23);
		panel_main.add(button_restart);

		JButton button_stepbystep = new JButton("Step-by-step");
		button_stepbystep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				StepByStep sbs = new StepByStep(alg);
			}
		});
		button_stepbystep.setBounds(382, 182, 328, 23);
		panel_main.add(button_stepbystep);

		JLabel label_key_input = new JLabel("Enter Key:");
		label_key_input.setForeground(Color.WHITE);
		label_key_input.setBounds(21, 124, 61, 14);
		panel_main.add(label_key_input);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.WHITE);
		separator.setBounds(359, 47, 2, 339);
		panel_main.add(separator);

		JFileChooser j = new JFileChooser();
		j.setCurrentDirectory(new File("./Documents"));
		j.setBounds(577, 292, -87, -53);
		panel_main.add(j);

		JButton button_file_select = new JButton("Select File");
		button_file_select.setBounds(382, 244, 328, 23);
		button_file_select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// invoke the showsSaveDialog function to show the save dialog
				int r = j.showOpenDialog(null);
				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION) {
					// set the label to the path of the selected file
					String loc = j.getSelectedFile().getAbsolutePath();
					// System.out.println(loc);
					new file_opened(loc);
					frame.dispose();
				}
			}
		});
		panel_main.add(button_file_select);

		JButton button_chat = new JButton("Chat");
		button_chat.setBounds(382, 278, 328, 23);
		button_chat.addActionListener(new ActionListener() {
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
		panel_main.add(button_chat);

		frame.setVisible(true);

	}

}
