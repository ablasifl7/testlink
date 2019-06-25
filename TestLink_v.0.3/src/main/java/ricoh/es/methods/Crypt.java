package ricoh.es.methods;


import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Crypt {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static KeySpec ks;
    private static SecretKeyFactory skf;
    private static Cipher cipher;
    static byte[] arrayBytes;
    private static String myEncryptionScheme;
    static SecretKey key;  
    public static String encrypt(String text, String myEncryptionKey){
        String encryptedString = null;
        text = checkString(text,false);
        myEncryptionKey = checkString(myEncryptionKey,true);
        
        try {
			myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
			ks = new DESedeKeySpec(arrayBytes);
			skf = SecretKeyFactory.getInstance(myEncryptionScheme);
			cipher = Cipher.getInstance(myEncryptionScheme);
			key = skf.generateSecret(ks);
			
	          cipher.init(Cipher.ENCRYPT_MODE, key);
	            byte[] plainText = text.getBytes(UNICODE_FORMAT);
	            byte[] encryptedText = cipher.doFinal(plainText);
	            encryptedString = new String(Base64.encodeBase64(encryptedText));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return encryptedString;
    }

    public static String decrypt(String text, String myEncryptionKey) {
        String decryptedText=null;
        text = checkString(text,false);
        myEncryptionKey = checkString(myEncryptionKey,true);

        try {
			myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
			ks = new DESedeKeySpec(arrayBytes);
			skf = SecretKeyFactory.getInstance(myEncryptionScheme);
			cipher = Cipher.getInstance(myEncryptionScheme);
			key = skf.generateSecret(ks);
	          cipher.init(Cipher.DECRYPT_MODE, key);
	            byte[] encryptedText = Base64.decodeBase64(text);
	            byte[] plainText = cipher.doFinal(encryptedText);
	            decryptedText= new String(plainText);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return decryptedText;
    }
    private static String checkString(String s,boolean isKey){
    	if(s==null){
    		s = "";
    	}
		String add = "                        ";
    	if(isKey && s.length()<add.length()){
    		s = s.concat(add);
    	}
    	return s;
    }
}
