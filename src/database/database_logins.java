package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

import Hashing.hashing_algorithms;

public class database_logins {


	static String DB_URL = "jdbc:mysql://localhost:3306/vigeneresystem"; // DB url - NOTE: characterEncoding needed for system error handline
	private static String dbUser = "root"; // User name for database
	private static String passWord = "longer000"; // PW for database


	/**
	 * Method: checkCredentials
	 * Function: check the credentials of a login. Compare the user input of user name
	 * 			 and password with what is stored.
	 * 
	 * @param uname
	 * @param honeyIndex
	 * @param pw
	 * @return
	 */
	public boolean checkCredentials(String uname, String pw) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(DB_URL, dbUser, passWord);			

			int honeyIndex = getHoneyIndex(uname);
			String check = "";

			switch (honeyIndex) {
			case 1:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw1 = ?;";
				break;
			case 2:  	
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw2 = ?;";
				break;
			case 3:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw3 = ?;";
				break;
			case 4:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw4 = ?;";
				break;
			case 5:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw5 = ?;";
				break;
			case 6:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw6 = ?;";
				break;
			case 7:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw7 = ?;";
				break;
			case 8:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw8 = ?;";
				break;
			case 9:  
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw9 = ?;";
				break;
			case 10: 
				check = "SELECT * FROM vigeneresystem.login WHERE user = ? AND pw10 = ?;";
				break;
			default:
				System.out.println("Invalid honey index!");
				break;
			}

			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, uname);

			
			String hashedpw = hashing_algorithms.SHA2(pw);
			ps.setString(2, hashedpw);
			

			ResultSet rs = ps.executeQuery();

			if(rs.next())
				return true;

			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return false;
	}

	/**
	 * Method: newUser
	 * Function: Insert a new user to the database
	 * 
	 * @param uname
	 * @param pw
	 * @return
	 */
	public boolean newUser(String uname, String pw) {

		try {

			Connection conn = DriverManager.getConnection(DB_URL, dbUser, passWord);			
			String check = "INSERT INTO vigeneresystem.login (user, pw1, pw2, pw3, pw4, pw5, pw6, pw7, pw8, pw9, pw10) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, uname); //  Set user name in prepared statement

			// Random placement of pw and honey words
			Random random = new Random();
			int rand = random.nextInt(10) + 1;

			for(int i = 2; i <= 11; i++) {
				if(i == rand) {
					ps.setString(i, pw);
				} else {
					ps.setString(i, randomPw());
				}
			}

			ps.execute(); // execute insert

			ps.close();
			conn.close();

			String honeyFileWrite = "\n" + uname + " " + rand;
			try {
				Files.write(Paths.get("NotTheHoneyIndexes"), honeyFileWrite.getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
				return false;
			}	

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true; // Return true if DB insert and file insert complete
	}

	/**
	 * Method: randomPw()
	 * Function: Generate a random hashed password to insert as honey words in database
	 * 
	 * @return
	 */
	public String randomPw() {

		String charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder str = new StringBuilder();
		Random rand = new Random();
		while (str.length() < 18) { // length of the random string.
			int index = (int) (rand.nextFloat() * charList.length());
			str.append(charList.charAt(index));
		}
		String saltStr = str.toString();

		String end = hashing_algorithms.SHA2(saltStr);

		return end; // Return random password for honey word
	}

	/**
	 * Method: isUniqueUser()
	 * Function: Check the database to make sure all usernames are unique
	 * 
	 * @param uname
	 * @return ... returns false if username is not unique
	 */
	public Boolean isUniqueUser(String uname) {
		Boolean checkReturn = null;
		try {

			Connection conn = DriverManager.getConnection(DB_URL, dbUser, passWord);			

			String check = "SELECT * FROM vigeneresystem.login WHERE user = ?;";

			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, uname); //  Set user name in ps

			ResultSet rs = ps.executeQuery(); // execute insert

			if(rs.next()) {
				checkReturn = false;
			} else {
				checkReturn = true;
			}

			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return checkReturn; 	
	}

	/**
	 * Method: getHoneyIndex()
	 * Function: Get the honey index from the stored file
	 * 
	 * NOTE: The file represents a secured server a typical organization would store this index in
	 * 
	 * @param uname
	 * @return
	 */
	public int getHoneyIndex(String uname) {

		try {

			File file = new File("NotTheHoneyIndexes");
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				String data = sc.nextLine();
				//System.out.println(data);
				String[] split = data.split(" ");
				if(split[0].equals(uname)) {
					sc.close();
					return Integer.parseInt(split[1]);
				}
			}

			sc.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return 9999;
		}

		return 9999;
	}


	public static void main(String args[]) {

		database_logins db = new database_logins();
/*		boolean check = db.checkCredentials("trent", "password");
		System.out.println(check);

		System.out.println(db.isUniqueUser("trent"));
*/
		boolean checkInsert = db.newUser("trent", "password");
		System.out.println(checkInsert);
	}
}
