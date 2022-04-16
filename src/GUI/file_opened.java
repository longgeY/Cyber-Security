package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import cryptography.algorithms;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;


public class file_opened {

	private JFrame frame;
	private JPanel panel;
	private JTextArea textArea_file_content;
	private JLabel label_file_name;
	private JButton button_encrypt;
	private JButton button_decrypt;
	private JButton button_save_same;
	private JTextField textField;
	private JButton button_save_new;

	/**
	 * Create the application.
	 */
	public file_opened(String file_loc) {
		initialize(file_loc);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String file_loc) {
		algorithms alg = new algorithms(); // For encryption and decryption
		String content = ""; // File content
		
		// Get file content
		try {
			content = get_file_text(file_loc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			label_file_name.setText("Unable to open/read file: " + file_loc);
		}
		
		// GUI
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 518);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(46, 54, 135, 23);
		panel.add(textField);
		textField.setColumns(10);
		
		textArea_file_content = new JTextArea();
		textArea_file_content.setBounds(10, 88, 764, 380);
		textArea_file_content.setText(content);
		panel.add(textArea_file_content);
		
		label_file_name = new JLabel("");
		label_file_name.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_file_name.setBounds(10, 11, 764, 32);
		label_file_name.setText(file_loc);
		panel.add(label_file_name);
		
		frame.getContentPane().add(panel);
		
		/**
		 * 
		 */
		button_encrypt = new JButton("Encrypt");
		button_encrypt.setBounds(191, 54, 89, 23);
		button_encrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText() == "") {
					textField.setText("Please enter key here!");
				} else {
					alg.setPlainText(textArea_file_content.getText());
					alg.setKey(textField.getText());
					alg.genKey();
					alg.mapKeyAndText();
					try {
						alg.encrypt();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					textArea_file_content.setText(alg.getCipherText());
					
				}				
			}
		});
		panel.add(button_encrypt);
		
		/**
		 * 
		 */
		button_decrypt = new JButton("Decrypt");
		button_decrypt.setBounds(290, 54, 89, 23);
		button_decrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					alg.setPlainText(textArea_file_content.getText());
					alg.setKey(textField.getText());
					alg.genKey();
					alg.mapKeyAndText();
					alg.setCipherText(textArea_file_content.getText());
					alg.decrypt();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textArea_file_content.setText(alg.getDecryptedPlainText());
			}
		});
		panel.add(button_decrypt);
		
		/**
		 * 
		 */
		button_save_same = new JButton("Save to Current File");
		button_save_same.setBounds(504, 25, 141, 23);
		button_save_same.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter writer = null;
				try {
					File new_file = new File(file_loc);
//					System.out.println(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
					writer = new FileWriter(new_file);
					writer.write(textArea_file_content.getText());
					writer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.add(button_save_same);
		
		
		/**
		 * 
		 */
		JFileChooser j = new JFileChooser();
		j.setCurrentDirectory(new File("./Documents"));
		j.setBounds(577, 292, -87, -53);
		panel.add(j);
		
		button_save_new = new JButton("Save to New File");
		button_save_new.setBounds(504, 54, 141, 23);
		button_save_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter writer = null;
				try {
					//show save dialog
					int r = j.showSaveDialog(null);
					if (r == JFileChooser.APPROVE_OPTION) {
						
						File newFile = j.getSelectedFile();
					
						newFile.getParentFile().mkdirs();
						
						//save the text in a new file
						newFile.createNewFile();
						writer = new FileWriter(newFile);
						writer.write(textArea_file_content.getText());
						writer.flush();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.add(button_save_new);
		
		/**
		 * 
		 */
		JButton button_exit = new JButton("Back");
		button_exit.setBounds(653, 54, 121, 23);
		button_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new User_Interface(new algorithms());
			}
		});
		panel.add(button_exit);
		
		JLabel label_key = new JLabel("Key:");
		label_key.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_key.setBounds(4, 58, 46, 14);
		panel.add(label_key);
		
		// 
		frame.setVisible(true);	
		
		
	}
	
	/**
	 * 
	 * @param loc
	 * @return
	 * @throws IOException
	 */
	public String get_file_text(String loc) throws IOException {
		FileReader f;
		try {
			f = new FileReader(loc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "Unable to read file. Please upload .txt and .dat files only.";
		}
		BufferedReader br = new BufferedReader(f);
		String content = "";
		String line = br.readLine();
		while(line != null) {
			content += line + "\n";
			line = br.readLine();
		}
		br.close();
		return content;
	}
}
