package Hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashing_algorithms {
	
	/**
	 * Author: Grepper
	 * Link: https://www.codegrepper.com/code-examples/java/SHA-3+java
	 * 
	 * @param input
	 * @return
	 */
	public static String SHA2(String input) {
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

		return encodedhash.toString();
	}
	
	/**
	 * Author: geeksforgeeks.org
	 * Link: https://www.geeksforgeeks.org/md5-hash-in-java/
	 * 
	 * @param input
	 * @return
	 */
	 public static String MD5(String input)
	    {
	        try {
	  
	            // Static getInstance method is called with hashing MD5
	            MessageDigest md = MessageDigest.getInstance("MD5");
	  
	            // digest() method is called to calculate message digest
	            //  of an input digest() return array of byte
	            byte[] messageDigest = md.digest(input.getBytes());
	  
	            // Convert byte array into signum representation
	            BigInteger no = new BigInteger(1, messageDigest);
	  
	            // Convert message digest into hex value
	            String hashtext = no.toString(16);
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	            return hashtext;
	        } 
	  
	        // For specifying wrong message digest algorithms
	        catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String input = "Hello World!";
		System.out.println(MD5(input));
		System.out.println(SHA2(input));
		
		System.out.println();
		
		String input2 = "1Hello World!";
		System.out.println(MD5(input2));
		System.out.println(SHA2(input2));
		
	}

}
