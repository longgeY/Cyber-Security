package cryptography;

import java.security.MessageDigest;

public class ShaAlgorithims {
	
	private String plaintext;
	private String ciphertext;
	
	public ShaAlgorithims(){
		this.plaintext = "";
		this.ciphertext = "";
	}
	
	public String getPlaintext() {
		return plaintext;
	}

	public void setPlaintext(String plaintext) {
		this.plaintext = plaintext;
	}

	public String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}

	public void encrypt(){
        if(plaintext==null||plaintext.length()==0){
            return;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(plaintext.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];      
            }
            this.ciphertext = new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return;
        }
    }
	
//	public static void main(String[] args)  {
//		ShaAlgorithims sa = new ShaAlgorithims();
//		sa.setPlaintext("LonggeYUan");
//		sa.decrypt();
//		System.out.println(sa.getCiphertext());
//	}
}
