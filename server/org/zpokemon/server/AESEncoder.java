package org.zpokemon.server;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Troy
 *
 */
public class AESEncoder {
    private static Cipher ecipher;
    private static Cipher dcipher;

    /**
     * Construct a new encrypted to encrypt and
     * decrypt packets for the client.
     * 
     * @param key
     */
    public AESEncoder(SecretKeySpec key) {
    	try {
    		ecipher = Cipher.getInstance("AES");
    		dcipher = Cipher.getInstance("AES");
    		ecipher.init(Cipher.ENCRYPT_MODE, key);
    		dcipher.init(Cipher.DECRYPT_MODE, key);
    	}catch (Exception e){}
    }

    public static String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);

            return new sun.misc.BASE64Encoder().encode(enc);
        }catch(Exception e){}
        return null;
    }

    public static String decrypt(String str) {
        try {
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");
        }catch(Exception e){}
        return null;
    }
    
    public static void main(String[] args){
    	String s = "Test test test one two three Test test test one two three Test test test one two three Test test test one two three Test test test one two three";
    	
    	 KeyGenerator kgen = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
         kgen.init(128); // 192 and 256 bits may not be available


         // Generate the secret key specs.
         SecretKey skey = kgen.generateKey();
         byte[] raw = skey.getEncoded();

         SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
         
         new AESEncoder(skeySpec);
         
         System.out.println(encrypt(s).replace("\r\n", ""));
         System.out.println(decrypt(encrypt(s).replace("\r\n", "")));
    }
    
}