package cryptography;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class algorithms {

	private String plaintext;
	private String ciphertext;
	private String key;
	private String split_key;
	private String plaintext_decrypted;
	
	public algorithms() {
		this.plaintext = "";
		this.key = "";
		this.ciphertext = "";
		this.split_key = "";
		this.plaintext_decrypted = "";
	}
	
	public void setKey(String k) { this.key = lowerToUpper(k); }
	public void setPlainText(String pt) { this.plaintext = lowerToUpper(pt); }
	public void setCipherText(String c) { this.ciphertext = lowerToUpper(c); }
	public String getCipherText() { return this.ciphertext; }
	public String getKey() { return this.key; }
	public String getSplitKey() { return this.split_key; }
	public String getPlainText() { return this.plaintext; }
	public String getDecryptedPlainText() { return this.plaintext_decrypted; }
	
	
	public void genKey() {
		int x = this.plaintext.length();
		
		String temp = key;
		int i = 0;
	    while(temp.length() != x)
	    {
	        temp+=(temp.charAt(i));
	        i++;
	    }
	    this.key = temp;
	}
	
	/**
	 * Method: lowerToUpper()
	 * Function: 
	 * @param str
	 * @return
	 */
	public String lowerToUpper(String str) {
		StringBuffer strb = new StringBuffer(str);
		for(int i = 0; i < str.length(); i++) {
			if(Character.isLowerCase(str.charAt(i))) {
				strb.setCharAt(i, Character.toUpperCase(str.charAt(i)));
			}
			else {
				strb.setCharAt(i,str.charAt(i));
			}
		}
		return strb.toString();
	}
	
	/**
	 * Method: mapKeyAndText()
	 * Function: repeat the key given from user until it matches the length of the plain text input
	 */
	public void mapKeyAndText() {
		StringBuffer str_k = new StringBuffer(this.key);
		int c = 0;
		for(int i = 0; i < this.plaintext.length(); i++) {
			if(Character.isWhitespace(this.plaintext.charAt(i))) {
				str_k.setCharAt(i, this.plaintext.charAt(i));
				c++;
			} else {
				str_k.setCharAt(i, this.key.charAt(((i-c) % this.key.length())));
			}
		}
		this.split_key = str_k.toString();
	}
	
	/**
	 * 
	 * @throws IOException 
	 */
	public void encrypt() throws IOException
	{
		BufferedReader br = new BufferedReader(new StringReader(this.plaintext));
		String line = null;
	    String cipher_text="";
	    int i1 = 0;

		while((line = br.readLine()) != null) {
			for (int i = 0; i < line.length(); i++) {
		    	if(Character.isWhitespace(line.charAt(i))) {
		    		cipher_text+=(char)(' ');
		    	} else {
		    		// converting in range 0-25
			        int x = (line.charAt(i) + this.key.charAt(i1)) %26;
			        // convert into alphabets(ASCII)
			        x += 'A';	 
			        cipher_text+=(char)(x);
		    	}
		    	i1++;
		    }
			cipher_text+="\n";
		}	    
	    this.ciphertext = cipher_text;
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void decrypt() throws IOException {
		String decrypted="";
		BufferedReader br = new BufferedReader(new StringReader(this.ciphertext));
		String line = null;
		int i1 = 0;
		
		while((line = br.readLine()) != null) {
			for (int i = 0 ; i < line.length() && i < key.length(); i++) {
	    		if(Character.isWhitespace(line.charAt(i))) {
	    			decrypted+=(char)(' ');
		    	} else {
		    		// converting in range 0-25
			        int x = (line.charAt(i) - key.charAt(i1) + 26) %26;
			        // convert into alphabets(ASCII)
			        x += 'A';
			        decrypted+=(char)(x);
		    	}
	    		i1++;
		    }
			decrypted+="\n";
		}
		this.plaintext_decrypted = decrypted;
	}
	
}
